package fr.ensicaen.lbssc.ensilink.loader;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;


import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.loader.news.MailNotificationContainer;
import fr.ensicaen.lbssc.ensilink.storage.Association;
import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.Mail;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;

import static android.R.id.message;

/**
 * @author Florian Arnould
 * @version 1.0
 */

public class ZimbraConnection {

    private Session _session;
    private Store _store;
    private static final String POP_SERVER3 = "zimbra.ensicaen.fr";

    private static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html";
    private static final String CONTENT_TYPE_MULTIPART_ALTERNATIVE = "multipart/alternative";
    private static final String CONTENT_TYPE_MULTIPART_RELATED = "multipart/related";
    private static final String CONTENT_TYPE_MULTIPART_MIXED = "multipart/mixed";
    String contentType;
    List<MailNotificationContainer> _newMails;
    private File _parent;

    public ZimbraConnection() {
       _newMails = new ArrayList<>();
    }

    public void connect(Context context) throws MessagingException {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        connect(pref.getString("email", ""), pref.getString("password", ""));
    }

    public void connect(String email, String password) throws MessagingException {
        Properties pop3Properties = new Properties();
        pop3Properties.setProperty("mail.pop3.ssl.enable", "true");
        pop3Properties.setProperty("mail.pop3.starttls.enable", "true");
        pop3Properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        pop3Properties.setProperty("mail.pop3.port", "995");
        pop3Properties.setProperty("mail.pop3.socketFactory.port", "995");

        _session = Session.getInstance(pop3Properties);
        _store = _session.getStore(new URLName("pop3://" + POP_SERVER3));
        _store.connect(email, password);
    }

    public void close() throws MessagingException {
        _store.close();
    }

    private Message[] getEmails() throws MessagingException {
        Folder folder = _store.getDefaultFolder();
        folder = folder.getFolder("INBOX");

        if (folder == null) {
            throw new MessagingException("Invalid Folder");
        }
        folder.open(Folder.READ_ONLY);
        return folder.getMessages();
    }

    void updateDatabase(SQLiteDatabase db, Context context) throws MessagingException, IOException {
        Cursor maxCursor = db.rawQuery("SELECT max(date) FROM mails;", null, null);
        Date oldMax;
        DateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.FRENCH);
        if (maxCursor.moveToFirst() && maxCursor.getString(0) != null) {
            Log.d("DEBUG", maxCursor.getString(0));
            try {
                oldMax = ft.parse(maxCursor.getString(0));
            } catch (ParseException e) {
                Calendar cal = Calendar.getInstance();
                cal.set(2000, 1, 1, 0, 0, 0);
                oldMax = cal.getTime();
            }
        } else {
            Calendar cal = Calendar.getInstance();
            cal.set(2000, 1, 1, 0, 0, 0);
            oldMax = cal.getTime();
        }
        maxCursor.close();
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        MailcapCommandMap cMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        cMap.addMailcap("text/plain; ; x-java-content-handler=com.sun.mail.handlers.text_plain");
        cMap.addMailcap("text/html; ; x-java-content-handler=com.sun.mail.handlers.text_html");
        cMap.addMailcap("text/xml; ; x-java-content-handler=com.sun.mail.handlers.text_xml");
        cMap.addMailcap("multipart/*; ; x-java-content-handler=com.sun.mail.handlers.multipart_mixed; x-java-fallback-entry=true");
        cMap.addMailcap("message/rfc822; ; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        Message[] mails = getEmails();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -2);
        Date limit = cal.getTime();
        for (int i=mails.length-1;i>0;i--) {
            Message message = mails[i];
            Log.d("DEBUG", message.getSentDate().toString());
            if (message.getSentDate().compareTo(oldMax) > 0 && message.getSentDate().compareTo(limit) > 0) {
                String email = "";
                if (message.isMimeType("multipart/*")){
                    email = getContentFromMultipart(message);
                }else if (message.isMimeType("text/plain")){
                    email += message.getContent().toString();
                }
                _parent = context.getDir("emails", Context.MODE_PRIVATE);
                parseAndInsertMailAndSave(message.getFrom()[0].toString(), message.getSubject(), ft.format(message.getSentDate()), email, db);
            }else{
                break;
            }
        }
    }

    private String getContentFromMultipart(Message message) throws IOException, MessagingException {
        Object objectContent = message.getContent();
        Multipart multipart = (Multipart) objectContent;
        String emailContent = "";
        int count = multipart.getCount();
        for (int i = 0; i < count; i++) {
            if (multipart.getBodyPart(i).getContentType().contains(this.CONTENT_TYPE_TEXT_HTML)) {
                contentType = this.CONTENT_TYPE_TEXT_HTML;
            } else if (multipart.getBodyPart(i).getContentType().contains(this.CONTENT_TYPE_TEXT_PLAIN)) {
                contentType = this.CONTENT_TYPE_TEXT_PLAIN;
                emailContent += multipart.getBodyPart(i).getContent().toString();
            } else if (multipart.getBodyPart(i).getContentType().contains(this.CONTENT_TYPE_MULTIPART_ALTERNATIVE) || multipart.getBodyPart(i).getContentType().contains(this.CONTENT_TYPE_MULTIPART_RELATED)
                    || multipart.getBodyPart(i).getContentType().contains(this.CONTENT_TYPE_MULTIPART_MIXED)) {
            }
        }
        return emailContent;
    }

    private void parseAndInsertMailAndSave(String from, String subject, String date, String email, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("date", date);
        long id = db.insert("mails", null, values);
        if(id != -1){
            new DiskSaver().execute(String.valueOf(id), from, subject, email);
            boolean finished = false;
            ContentValues values2 = new ContentValues();
            values2.put("idmail", id);
            String table = "";
            for(Union union : School.getInstance().getUnions()){
                for(Club club : union.getClubs()){
                    if(subject.toLowerCase().contains(club.getName().toLowerCase())){
                        values2.put("idclub", club.getId());
                        _newMails.add(new MailNotificationContainer(subject, email, union, club));
                        table = "club_mails";
                        finished = true;
                        break;
                    }
                }
                if(finished){
                    break;
                }
                for(String tag : union.getTags()){
                    if(subject.toLowerCase().contains(tag.toLowerCase())){
                        values2.put("idunion", union.getId());
                        _newMails.add(new MailNotificationContainer(subject, email, union));
                        table = "union_mails";
                        finished = true;
                        break;
                    }
                }
                if(finished){
                    break;
                }
            }
            if(finished) {
                db.insert(table, null, values2);
            }
        }else{
            Log.e("ERROR", "Email ignored : " + subject);
        }
    }

    List<MailNotificationContainer> getNewMails(){
        return _newMails;
    }

    private class DiskSaver extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {
            try {
                BufferedWriter wr = new BufferedWriter(new FileWriter(new File(_parent, params[0] + ".txt")));
                wr.write(params[1] + "\n");
                wr.write(params[2] + "\n");
                wr.write(params[3]);
                wr.close();
            } catch (IOException ex) {
                Log.e("ERROR", "mail file not saved");
            }
            return null;
        }
    }
}

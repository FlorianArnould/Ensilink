package fr.ensicaen.lbssc.ensilink.loader;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.SharedByteArrayInputStream;

import fr.ensicaen.lbssc.ensilink.R;

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
    private String contentType;

    public ZimbraConnection() {

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
        for (Message message : mails) {
            if (message.getSentDate().compareTo(oldMax) > 0 && message.getSentDate().compareTo(limit) > 0) {
                String email = "";
                if (message.isMimeType("multipart/*")){
                    email = getContentFromMultipart(message);
                }else if (message.isMimeType("text/plain")){
                    email += message.getContent().toString();
                }
                ContentValues values = new ContentValues();
                values.put("date", ft.format(message.getSentDate()));
                long id = db.insert("mails", null, values);
                if(id != -1){
                    File parent = context.getDir("emails", Context.MODE_PRIVATE);
                    BufferedWriter wr = new BufferedWriter(new FileWriter(new File(parent, String.valueOf(id) + ".txt")));
                    wr.write(message.getFrom()[0].toString() + "\n");
                    wr.write(message.getSubject() + "\n");
                    wr.write(email);
                    wr.close();
                }else{
                    Log.e("ERROR", "Email ignored : " + message.getSubject());
                }
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

    private long parseAndInsertMail(String from, String subject, Date date) {
        return 0;
    }
}

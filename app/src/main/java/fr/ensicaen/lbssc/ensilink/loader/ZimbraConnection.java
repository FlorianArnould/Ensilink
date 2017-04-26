/**
 * This file is part of Ensilink.
 *
 * Ensilink is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Ensilink is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Ensilink.
 * If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright, The Ensilink team :  ARNOULD Florian, ARIK Marsel, FILIPOZZI Jérémy,
 * ENSICAEN, 6 Boulevard du Maréchal Juin, 26 avril 2017
 *
 */

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
import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;

/**
 * @author Jeremy Filipozzi
 * @version 1.0
 */

/**
 * ZimbraConnection is a class to use javax.mail to connect, and get mails from the zimbra server of Ensicaen
 */
public class ZimbraConnection {

    private Store _store;
    private static final String POP_SERVER3 = "zimbra.ensicaen.fr";

    private static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html";
    private final List<MailNotificationContainer> _newMails;
    private File _parent;

    public ZimbraConnection() {
       _newMails = new ArrayList<>();
    }

    /**
     * Connect with email and password in SharedPreferences
     * @param context an activity context to use SharedPreferences
     * @throws MessagingException javax.mail Exception
     */
    public void connect(Context context) throws MessagingException {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        connect(pref.getString("email", ""), pref.getString("password", ""));
    }

    /**
     * Connect to the zimbra server
     * @param email the ensicaen email address
     * @param password the ensicaen password
     * @throws MessagingException javax.mail Exception
     */
    public void connect(String email, String password) throws MessagingException {
        Properties pop3Properties = new Properties();
        pop3Properties.setProperty("mail.pop3.ssl.enable", "true");
        pop3Properties.setProperty("mail.pop3.starttls.enable", "true");
        pop3Properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        pop3Properties.setProperty("mail.pop3.port", "995");
        pop3Properties.setProperty("mail.pop3.socketFactory.port", "995");

        Session session = Session.getInstance(pop3Properties);
        _store = session.getStore(new URLName("pop3://" + POP_SERVER3));
        _store.connect(email, password);
    }

    /**
     * Close the connection to the zimbra server
     * @throws MessagingException javax.mail Exception
     */
    public void close() throws MessagingException {
        _store.close();
    }

    /**
     * @return the mails loaded by javax.mail
     * @throws MessagingException javax.mail Exception
     */
    private Message[] getEmails() throws MessagingException {
        Folder folder = _store.getDefaultFolder();
        folder = folder.getFolder("INBOX");

        if (folder == null) {
            throw new MessagingException("Invalid Folder");
        }
        folder.open(Folder.READ_ONLY);
        return folder.getMessages();
    }

    /**
     * Update the local database with the emails
     * @param db the database
     * @param context an application context
     * @throws MessagingException javax.mail Exception
     * @throws IOException Exception when getting the content of the mail
     */
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

    /**
     * Use to get the content of a multipart mail
     * @param message the mail
     * @return the content as a string
     * @throws IOException Exception when getting the content of the mail
     * @throws MessagingException javax.mail Exception
     */
    private String getContentFromMultipart(Message message) throws IOException, MessagingException {
        Object objectContent = message.getContent();
        Multipart multipart = (Multipart) objectContent;
        String emailContent = "";
        int count = multipart.getCount();
        for (int i = 0; i < count; i++) {
            if (!multipart.getBodyPart(i).getContentType().contains(CONTENT_TYPE_TEXT_HTML) && multipart.getBodyPart(i).getContentType().contains(CONTENT_TYPE_TEXT_PLAIN)) {
                emailContent += multipart.getBodyPart(i).getContent().toString();
            }
        }
        return emailContent;
    }

    /**
     * Parse the mails and Insert them in the database
     * @param from the sender email address
     * @param subject the subject of the mail
     * @param date the sent date of the mail
     * @param email the content of the mail
     * @param db the database
     */
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
                if(from.contains(union.getEmail())){
                    values2.put("idunion", union.getId());
                    _newMails.add(new MailNotificationContainer(subject, email, union));
                    table = "union_mails";
                    finished = true;
                    break;
                }
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

    /**
     * @return the list of the new mails
     */
    List<MailNotificationContainer> getNewMails(){
        return _newMails;
    }

    /**
     * ASyncTask used to save the mails on the disk in background
     */
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

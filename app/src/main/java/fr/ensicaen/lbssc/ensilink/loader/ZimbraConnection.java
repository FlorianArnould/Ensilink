package fr.ensicaen.lbssc.ensilink.loader;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import fr.ensicaen.lbssc.ensilink.R;

/**
 * @author Florian Arnould
 * @version 1.0
 */

public class ZimbraConnection {

    private Session _session;
    private Store _store;
    private static final String POP_SERVER3 = "zimbra.ensicaen.fr";

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

    void updateDatabase(SQLiteDatabase db) throws MessagingException {
        Cursor maxCursor = db.rawQuery("SELECT max(date) FROM mails;", null, null);
        Date oldMax;
        if(maxCursor.moveToFirst() && maxCursor.getString(0) != null){
            oldMax = Date.valueOf(maxCursor.getString(0));
        }else{
            oldMax = Date.valueOf("2000-01-01");
        }
        maxCursor.close();
        for(Message message : getEmails()){
            if(message.getSentDate().compareTo(oldMax) > 0){
                ContentValues values = new ContentValues();
                values.put("subject", message.getSubject());
                Log.d("DEBUG","BEFORE");
                try {
                    Object content = message.getContent();
                    System.out.println(content);
                    Log.d("DEBUG","TRY");
                    if (content instanceof String) {
                        String body = (String) content;
                        System.out.println(body);
                    } else {
                        Log.d("DEBUG","ELSE");
                        Multipart multipart = (Multipart) content;
                        for (int j = 0; j < multipart.getCount(); j++) {
                            BodyPart bodyPart = multipart.getBodyPart(j);
                            String disposition = bodyPart.getDisposition();
                            Log.d("DEBUG","FOR");
                            if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) {
                                DataHandler handler = bodyPart.getDataHandler();
                            } else {
                                String text = bodyPart.getContent().toString();
                                System.out.println(text);
                                break;
                            }
                        }
                    }
                }catch (IOException e) {
                    Log.d("DEBUG","EXCEPTION");
                    e.printStackTrace();
                }
                Log.d("DEBUG","getemailsAFTER");
                values.put("sender", message.getFrom()[0].toString());
                SimpleDateFormat ft =
                        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                values.put("date", ft.format(message.getSentDate()));
                db.insert("mails", null, values);
            }
        }
    }
}

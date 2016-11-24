package fr.ensicaen.lbssc.ensilink.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


final class DatabaseCloner extends Thread {

    private boolean _success;
    private LocalDatabaseManager _databaseManager;
    private SQLiteDatabase _db;

    DatabaseCloner(Context context){
        _success = true;
        _databaseManager = new LocalDatabaseManager(context);
    }

    boolean succeed(){
        return _success;
    }

    @Override
    public void run(){
        InputStream in = connect();
        if(in == null){
            _success = false;
            return;
        }
        Document doc = parseResponse(in);
        if(doc == null || !openDatabase()){
            _success = false;
            return;
        }
        if(!updateDatabase(doc)){
            _success = false;
            _db.close();
        }
    }

    private InputStream connect(){
        String login = "android";
        String password = "wantToClone";
        String urlString = "http://www.ecole.ensicaen.fr/~arnould/test/interface.php";
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            String postData = "login=" + login + "&password=" + password;
            out.write(postData.getBytes());
            out.flush();
            return connection.getInputStream();
        }catch (Exception e){
            Log.d("D", "Error with the page return by php : " + e.getMessage());
        }
        return null;
    }

    private boolean openDatabase(){
        try{
            _db = _databaseManager.getWritableDatabase();
        }catch (SQLiteException e){
            Log.d("D", "Error when tried to open SQLite database : " + e.getMessage());
            return false;
        }
        return true;
    }

    private Document parseResponse(InputStream in){
        try {
            DocumentBuilder builder = (DocumentBuilderFactory.newInstance()).newDocumentBuilder();
            return builder.parse(in);
        } catch (ParserConfigurationException e){
            Log.d("D", "Error with the parser configuration : " + e.getMessage());
        } catch (SAXException e){
            Log.d("D", "Error with DOM parser : " + e.getMessage());
        } catch (IOException e){
            Log.d("D", "Error when use inputStream to parse : " + e.getMessage());
        }
        return null;
    }

    //TODO add transaction to protect the existing database
    private boolean updateDatabase(Document doc){
        clearDatabase();
        String[] tableList = LocalDatabaseManager.getTables();
        for (String table : tableList){
            NodeList list = doc.getElementsByTagName(table);
            for(int j=0;j<list.getLength();j++){
                NamedNodeMap attributes = list.item(j).getAttributes();
                ContentValues values = new ContentValues();
                for(int k=0;k<attributes.getLength();k++){
                    Node item = attributes.item(k);
                    values.put(item.getNodeName(), item.getNodeValue());
                }
                try {
                    _db.insertOrThrow(table, null, values);
                }catch (SQLiteException e){
                    Log.d("D", "Error with an insert query : " + e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    private void clearDatabase(){
        String[] tableList = LocalDatabaseManager.getTables();
        for(int i=tableList.length-1;i>=0;i--){
            _db.execSQL("TRUNCATE TABLE" + tableList[i] + ";");
        }
    }
}

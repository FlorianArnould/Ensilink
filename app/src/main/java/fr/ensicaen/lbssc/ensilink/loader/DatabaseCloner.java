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
import android.database.Cursor;
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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import fr.ensicaen.lbssc.ensilink.loader.news.DateNews;
import fr.ensicaen.lbssc.ensilink.loader.news.DayNews;
import fr.ensicaen.lbssc.ensilink.loader.news.HourNews;
import fr.ensicaen.lbssc.ensilink.loader.news.News;
import fr.ensicaen.lbssc.ensilink.loader.news.PlaceNews;
import fr.ensicaen.lbssc.ensilink.storage.Date;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * This class clone the online database in the local one.
 */
final class DatabaseCloner{

    private boolean _success;
    private final SQLiteDatabase _db;
    private final Map<String, Long> _imagesTimestamp;
    private final List<News> _news;

    /**
     * @param db the database instance
     */
    DatabaseCloner(SQLiteDatabase db){
        _success = false;
        _db = db;
        _imagesTimestamp = new HashMap<>();
        _news = new ArrayList<>();
    }

    /**
     * How to know if the cloning succeeded
     * @return true if the cloning has succeeded
     */
    boolean succeed(){
        return _success;
    }

    /**
     * Clones, parses and fills the local database
     */
    void cloneDatabase(){
        InputStream in = connect();
        if(in == null){
            _success = false;
            return;
        }
        Document doc = parseResponse(in);
        if(doc == null){
            _success = false;
            return;
        }
        if(!updateDatabase(doc)){
            _success = false;
        }
        _success = true;
    }

    /**
     * Connects to the PHP script and get his answer
     * @return an InputStream on the script's answer
     */
    private InputStream connect(){
        String login = "android";
        String password = "wantToClone";
        String urlString = "http://www.ecole.ensicaen.fr/~arnould/others/test/interface.php";
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                String postData = "login=" + login + "&password=" + password;
                out.write(postData.getBytes());
                out.flush();
                return connection.getInputStream();
            }catch (ProtocolException e){
                Log.d("D", "Error with the protocol : " + e.getMessage());
            }
        }catch (MalformedURLException e){
            Log.d("D", "Error with the url: " + e.getMessage());
        }catch (IOException e){
            Log.d("D", "Error with input/output : " + e.getMessage() + "\n" + e.toString());
        }
        return null;
    }

    /**
     * Parses the PHP script answer
     * @param in InputStream on the PHP script answer
     * @return a DOM Document which contains the XML version of the online database
     */
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

    /**
     * Updates the local database from a DOM Document
     * @param doc DOM Document with the XML version of the online database
     * @return true if the local database is updated
     */
    private boolean updateDatabase(Document doc){
        NodeList timestamps = doc.getElementsByTagName("last_update_image_file");
        for(int i=0;i<timestamps.getLength();i++){
            Node node = timestamps.item(i);
            NamedNodeMap att = node.getAttributes();
            _imagesTimestamp.put(att.getNamedItem("file").getNodeValue(), Long.valueOf(att.getNamedItem("timestamp").getNodeValue()));
        }
        checkClubNews(doc);
        clearDatabase();
        String[] tableList = LocalDatabaseManager.getTables();
        _db.beginTransaction();
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
                    _db.endTransaction();
                    return false;
                }
            }
        }
        _db.setTransactionSuccessful();
        _db.endTransaction();
        return true;
    }

    /**
     * Cleans all tables in the database
     */
    private void clearDatabase(){
        String[] tableList = LocalDatabaseManager.getTables();
        for(int i=tableList.length-1;i>=0;i--){
            _db.delete(tableList[i], null, null);
        }
    }

    /**
     * @return the timestamp of the last update of each image file
     */
    Map<String, Long> lastUpdateImages(){
        return _imagesTimestamp;
    }

    /**
     * Create the private list which contains the news which needs a notification
     * @param doc DOM document which represent the XML file
     */
    private void checkClubNews(Document doc){
        NodeList clubs = doc.getElementsByTagName("clubs");
        for(int i=0;i<clubs.getLength();i++){
            Node club = clubs.item(i);
            NamedNodeMap attributes = club.getAttributes();
            Cursor cursor = _db.query("clubs", new String[]{"idunion", "day", "date", "start_hour", "place"}, "id=?", new String[]{attributes.getNamedItem("id").getNodeValue()}, null, null, null, null);
            if(cursor.moveToFirst()){
                if(!cursor.getString(1).equals(attributes.getNamedItem("day").getNodeValue())){
                    _news.add(new DayNews(cursor.getInt(0), attributes.getNamedItem("name").getNodeValue(),  Integer.valueOf(attributes.getNamedItem("day").getNodeValue())));
                }
                if(!cursor.getString(2).equals(attributes.getNamedItem("date").getNodeValue())){
                    _news.add(new DateNews(cursor.getInt(0), attributes.getNamedItem("name").getNodeValue(), new Date(attributes.getNamedItem("date").getNodeValue())));
                }
                if(!cursor.getString(3).equals(attributes.getNamedItem("start_hour").getNodeValue())){
                    _news.add(new HourNews(cursor.getInt(0), attributes.getNamedItem("name").getNodeValue(), attributes.getNamedItem("start_hour").getNodeValue()));
                }
                if(!cursor.getString(4).equals(attributes.getNamedItem("place").getNodeValue())){
                    _news.add(new PlaceNews(cursor.getInt(0), attributes.getNamedItem("name").getNodeValue(), attributes.getNamedItem("place").getNodeValue()));
                }
            }
            cursor.close();
        }
    }

    /**
     * Give the modifications of the database
     * @return a list of News for notifications
     */
    List<News> getModifications(){
        return _news;
    }
}

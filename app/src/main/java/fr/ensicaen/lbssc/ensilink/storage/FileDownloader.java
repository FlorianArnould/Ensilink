package fr.ensicaen.lbssc.ensilink.storage;

import android.content.Context;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Class which can download files in local directory as images
 */
final class FileDownloader {

    private final Context _context;

    /**
     * The constructor
     * @param context an activity context needed to open the local file directory
     */
    FileDownloader(Context context){
        _context = context;
    }


    /**
     * @param imageNames a list of the name of the images
     */
    void downloadImages(List<String> imageNames){
        try{
            for (String imageName : imageNames){
                download(imageName);
            }
        } catch (IOException e) {
            Log.d("D", "io error : " + e.getMessage());
        }
    }

    /**
     * Download one image from the network
     * @param imageName the image name
     */
    private void download(String imageName) throws IOException{
        try {
            URL url = new URL("http://www.ecole.ensicaen.fr/~arnould/others/test/images/" + imageName);
            InputStream in = url.openStream();
            DataInputStream dis = new DataInputStream(in);
            byte[] buffer = new byte[1024];
            int length;

            FileOutputStream fos = new FileOutputStream(new File(_context.getFilesDir(), imageName + ".new"));
            while ((length = dis.read(buffer))>0) {
                fos.write(buffer, 0, length);
            }
            move(imageName+".new", imageName);
        } catch (MalformedURLException e) {
            Log.d("D", "malformed url error : " + e.getMessage());
        }
    }

    /**
     * Rename the original file to the target
     * @param original the name of the existing file
     * @param target the new name of the file
     */
    private void move(String original, String target){
        File originalFile = new File(_context.getFilesDir(), original);
        try {
            InputStream in = new FileInputStream(originalFile);
            OutputStream out = new FileOutputStream(new File(_context.getFilesDir(), target));
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            if(!originalFile.delete()){
                Log.d("D", "File " + original + " was not removed correctly");
            }
        } catch (FileNotFoundException e) {
            Log.d("D", "File not found when moving image " + target + " : " + e.getMessage());
        } catch (IOException e) {
            Log.d("D", "Read or write error when moving image " + target + " : " + e.getMessage());
        }
    }
}

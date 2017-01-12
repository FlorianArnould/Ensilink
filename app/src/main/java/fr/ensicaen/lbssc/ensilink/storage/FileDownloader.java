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
class FileDownloader {

    private Context _context;

    /**
     * The constructor
     * @param context an activity context needed to open the local file directory
     */
    FileDownloader(Context context){
        _context = context;
    }


    /**
     *
     * @param imageNames a list of the name of the images
     * @return true if all images were downloaded successfully
     */
    boolean downloadImages(List<String> imageNames){
        boolean ok = true;
        try{
            for (String imageName : imageNames){
                ok &= download(imageName);
            }
        } catch (IOException e) {
            Log.d("D", "io error : " + e.getMessage());
        }
        return ok;
    }

    /**
     *
     * @param imageName the image name
     * @return true if the image was downloaded successfully
     */
    private boolean download(String imageName) throws IOException{
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
            return move(imageName+".new", imageName);
        } catch (MalformedURLException e) {
            Log.d("D", "malformed url error : " + e.getMessage());
        }
        return false;
    }

    private boolean move(String original, String target){
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
            return originalFile.delete();
        } catch (FileNotFoundException e) {
            Log.d("D", "File not found when moving image " + target + " : " + e.getMessage());
        } catch (IOException e) {
            Log.d("D", "Read or write error when moving image " + target + " : " + e.getMessage());
        }
        return false;
    }
}

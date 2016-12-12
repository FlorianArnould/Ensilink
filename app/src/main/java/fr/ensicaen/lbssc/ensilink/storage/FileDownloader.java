package fr.ensicaen.lbssc.ensilink.storage;

import android.content.Context;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

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
     * @param imageName the image name
     * @return true if the image was downloaded successfully
     */
    boolean download(String imageName){
        try {
            URL url = new URL("http://www.ecole.ensicaen.fr/~arnould/others/test/images/" + imageName);
            InputStream in = url.openStream();
            DataInputStream dis = new DataInputStream(in);
            byte[] buffer = new byte[1024];
            int length;

            FileOutputStream fos = new FileOutputStream(new File(_context.getFilesDir(), imageName));
            while ((length = dis.read(buffer))>0) {
                fos.write(buffer, 0, length);
            }
            return true;
        } catch (MalformedURLException mue) {
            Log.e("D", "malformed url error", mue);
        } catch (IOException ioe) {
            Log.e("D", "io error", ioe);
        }
        return false;
    }
}

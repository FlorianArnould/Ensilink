package fr.ensicaen.lbssc.ensilink.loader;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Florian Arnould
 */
@RunWith(AndroidJUnit4.class)
public class FileDownloaderTest {
	@Test
	public void moveTest() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		FileOutputStream fos = new FileOutputStream(new File(context.getFilesDir(), "test.new"));
		fos.write("coucou".getBytes());
		fos.close();
		FileDownloader downloader = new FileDownloader(context);
		downloader.move("test.new", "test.txt");
		File file = new File(context.getFilesDir(), "test.txt");
		Assert.assertTrue(file.exists());
	}

	@Test
	public void moveMissingFile() throws Exception {
		FileDownloader downloader = new FileDownloader(InstrumentationRegistry.getTargetContext());
		downloader.move("missingFile.txt", "file.txt");
	}

	@Test
	public void downloadTest() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		DatabaseCloner cloner = new DatabaseCloner(new LocalDatabaseManager(context).getWritableDatabase());
		cloner.cloneDatabase();
		Map<String, Long> imagesTimestamps = cloner.lastUpdateImages();
		FileDownloader downloader = new FileDownloader(context);
		Iterator<String> iterator = imagesTimestamps.keySet().iterator();
		Assert.assertTrue(iterator.hasNext());
		String imageName = iterator.next();
		downloader.download(imageName);
		File file = new File(context.getFilesDir(), imageName);
		Assert.assertTrue(file.exists());
	}

	@Test(expected = IOException.class)
	public void downloadMissingFileTest() throws Exception {
		FileDownloader downloader = new FileDownloader(InstrumentationRegistry.getTargetContext());
		downloader.download("missing.extensionWhichDoesn'tExist");
	}
}

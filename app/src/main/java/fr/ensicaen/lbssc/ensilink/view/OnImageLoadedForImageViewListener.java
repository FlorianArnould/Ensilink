package fr.ensicaen.lbssc.ensilink.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import fr.ensicaen.lbssc.ensilink.storage.OnImageLoadedListener;

/**
 * @author Florian Arnould
 */
public class OnImageLoadedForImageViewListener implements OnImageLoadedListener {
	private final ImageView _view;
	private final Activity _activity;

	public OnImageLoadedForImageViewListener(Activity activity, ImageView view) {
		_view = view;
		_activity = activity;
	}

	@Override
	public void onImageLoaded(final Bitmap image) {
		_activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				_view.setImageBitmap(image);
			}
		});
	}
}

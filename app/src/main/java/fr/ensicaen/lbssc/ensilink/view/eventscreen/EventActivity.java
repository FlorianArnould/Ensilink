package fr.ensicaen.lbssc.ensilink.view.eventscreen;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.Event;
import fr.ensicaen.lbssc.ensilink.storage.School;

/**
 * @author Jérémy Filipozzi
 * @version 1.0
 */

/**
 * Class which displays the event selected from the homepage list. It displays an image related to
 * the event and a complete description of it.
 */
public class EventActivity extends AppCompatActivity{

    @Override
    @SuppressWarnings("deprecation") //For retro compatibility
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(getColor(R.color.colorPrimaryDark));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Event event = School.getInstance().getEvent(getIntent().getIntExtra("EVENT_ID", 0));
        TextView eventTitle = (TextView) findViewById(R.id.eventTitle);
        eventTitle.setText(event.getTitle());
        ImageView eventImage = (ImageView) findViewById(R.id.imageView);
        eventImage.setImageBitmap(event.getImage());
        TextView description = (TextView) findViewById(R.id.textViewDescription);
        description.setText(event.getMainText());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

package fr.ensicaen.lbssc.ensilink.eventscreen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.test.suitebuilder.TestMethod;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.Event;
import fr.ensicaen.lbssc.ensilink.storage.School;

/**
 * Created by jeremy on 12/11/16.
 */

public class EventActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
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

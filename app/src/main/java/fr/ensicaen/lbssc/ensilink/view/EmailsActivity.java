package fr.ensicaen.lbssc.ensilink.view;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.Mail;
import fr.ensicaen.lbssc.ensilink.storage.School;

/**
 * @author Jérémy Filipozzi
 * @version 1.0
 */

/**
 * Class which displays the mail selected from the mailpage of an union or a club.
 * It displays all important information about it : transmitter, subject, text and
 * date.
 */
public class EmailsActivity extends AppCompatActivity {


    @Override
    @SuppressWarnings("deprecation") //For retro compatibility
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emails_activity);
        if(Build.VERSION.SDK_INT >=23){
            getWindow().setStatusBarColor(getColor(R.color.colorPrimaryDark));
        }else if(Build.VERSION.SDK_INT >= 21){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Mail mail = School.getInstance().getMail(getIntent().getIntExtra("MAIL_ID",0));
        TextView mailSubject = (TextView) findViewById(R.id.mailSubject);
        mailSubject.setText(mail.getSubject());
        TextView mailDate = (TextView) findViewById(R.id.mailDate);
        mailDate.setText(mail.getDate());
        TextView mailTransmitter = (TextView) findViewById(R.id.mailTransmitter);
        mailTransmitter.setText(mail.getTransmitter());
        TextView mailText = (TextView) findViewById(R.id.mailText);
        mailText.setText(mail.getText());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

}

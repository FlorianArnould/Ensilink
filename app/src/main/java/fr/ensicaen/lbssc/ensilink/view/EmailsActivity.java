package fr.ensicaen.lbssc.ensilink.view;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

import fr.ensicaen.lbssc.ensilink.R;
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
        Message mail = School.getInstance().getMail(getIntent().getIntExtra("MAIL_ID",0));
        TextView mailSubject = (TextView) findViewById(R.id.mailSubject);
        try {
            mailSubject.setText(mail.getSubject());
        } catch (MessagingException e) {
            Log.d("DEBUG","Problème avec la récupération de l'objet du mail");
        }

        TextView mailTransmitter = (TextView) findViewById(R.id.mailTransmitter);
        try {
            mailTransmitter.setText(mail.getFrom().toString());
        } catch (MessagingException e) {
            Log.d("DEBUG","Problème avec la récupération du nom de l'expéditeur");
        }
        TextView mailText = (TextView) findViewById(R.id.mailText);
        try {
            mailText.setText(mail.getContent().toString());
        } catch (IOException e) {
            Log.d("DEBUG","Problème avec le contenu du message");
        } catch (MessagingException e) {
            Log.d("DEBUG","Problème avec la récupération du contenu du mail");
        }
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

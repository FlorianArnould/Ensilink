package fr.ensicaen.lbssc.ensilink.view;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import javax.mail.*;
import java.util.*;

import fr.ensicaen.lbssc.ensilink.R;


public class LoginActivity extends AppCompatActivity
{

        private EditText _email;
        private EditText _password;
        private Session _session;
        private Folder _folder;
        private Store _store;
        private static final String POP_SERVER3 = "zimbra.ensicaen.fr";

        /**
         * Constructor of the class which initializes three private class parameters
         */
        public LoginActivity() {
            this._session = null;
            this._store = null;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.login_screen_activity);
            _email = (EditText) findViewById(R.id.input_email);
            _password = (EditText) findViewById(R.id.input_password);
            Button button = (Button) findViewById(R.id.login_btn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validate()) {
                        connection test = new connection();
                        test.execute();
                    }
                }
            });
        }

        /**
         * This method connects the user to zimbra thanks to his username and
         * password through pop3, SSL and TLS
         *
         * @throws Exception
         */
        public void connect() throws Exception {

            Properties pop3Properties = new Properties();
            pop3Properties.setProperty("mail.pop3.ssl.enable", "true");
            pop3Properties.setProperty("mail.pop3.starttls.enable", "true");
            pop3Properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            pop3Properties.setProperty("mail.pop3.port", "995");
            pop3Properties.setProperty("mail.pop3.socketFactory.port", "995");

            String email = _email.getText().toString();
            String password = _password.getText().toString();

            _session = Session.getInstance(pop3Properties);
            _store = _session.getStore(new URLName("pop3://" + POP_SERVER3));
            _store.connect(email, password);
        }

        /**
         * Method that allows the application to make sure that the email address entered has the
         * right format for the connection to zimbra
         * @return boolean valid that allows the connection to proceed
         */
        private boolean validate(){
                boolean valid = true;
                String email = _email.getText().toString();
                String password = _password.getText().toString();
                if(email.isEmpty() || !email.contains("@ecole.ensicaen.fr")){
                    _email.setError("Email de l'ensicaen");
                    valid = false;
                }
                if(password.isEmpty()){
                    valid = false;
                }
                return valid;
            }

        public void openFolder(String folderName) throws Exception {
            _folder = _store.getDefaultFolder();
            _folder = _folder.getFolder(folderName);

            if (_folder == null) {
                throw new Exception("Invalide Folder");
            }
            try {
                _folder.open(Folder.READ_WRITE);
            } catch (MessagingException e) {
                _folder.open(Folder.READ_ONLY);
            }
        }

        /**
         * This class was created because on Android we cannot handle network related actions in the
         * UI thread
         */
        private class connection extends AsyncTask<Void, Void, Void> {

                    ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                            R.style.AppTheme_Dark_Dialog);
                    /**
                     * Method that displays the status of the connection as soon as we click on the
                     * connection button
                     */
                    @Override
                    protected void onPreExecute(){
                        super.onPreExecute();
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Authentification ...");
                        progressDialog.show();
                    }

                    /**
                     * Method that handles the connection to zimbra in the AsyncTask once we entered our
                     * user mail adress and our password
                     * @param params
                     * @return
                     */
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            LoginActivity.this.connect();
                            LoginActivity.this.openFolder("INBOX");
                        } catch (Exception e) {
                            Log.d("DEBUG","Erreur dans l'AsyncTask :" + e.getMessage());
                        }
                        return null;
                    }

                    /**
                     * Method that cancels the progressDialog at the end of the connection to zimbra
                     */
                    @Override
                    protected void onPostExecute(Void result) {
                        progressDialog.cancel();
                    }
                }
}






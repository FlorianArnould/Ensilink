package fr.ensicaen.lbssc.ensilink.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.loader.ZimbraConnection;


public class LoginActivity extends AppCompatActivity
{

        private EditText _email;
        private EditText _password;

        /**
         * Constructor of the class which initializes three private class parameters
         */
        public LoginActivity() {

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
                        test.execute(_email.getText().toString(), _password.getText().toString());
                    }
                }
            });
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

        /**
         * This class was created because on Android we cannot handle network related actions in the
         * UI thread
         */
        private class connection extends AsyncTask<String, Void, Boolean> {

            private ProgressDialog progressDialog;

            /**
             * Method that displays the status of the connection as soon as we click on the
             * connection button
             */
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                progressDialog = new ProgressDialog(LoginActivity.this,
                        R.style.AppTheme_Dark_Dialog);
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
            protected Boolean doInBackground(String... params) {
                try {
                    ZimbraConnection conn = new ZimbraConnection();
                    conn.connect(params[0], params[1]);
                    conn.close();
                    return true;
                } catch (Exception e) {
                    Log.d("DEBUG","Erreur dans l'AsyncTask :" + e.getMessage());
                    return false;
                }
            }

            /**
             * Method that cancels the progressDialog at the end of the connection to zimbra
             */
            @Override
            protected void onPostExecute(Boolean result) {
                progressDialog.cancel();
                if(result) {
                    SharedPreferences pref = LoginActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    pref.edit().putString("email", _email.getText().toString()).putString("password", _password.getText().toString()).apply();
                    LoginActivity.this.finish();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
                    builder.setTitle(R.string.error);
                    builder.setMessage(R.string.authentification_error);
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.create().show();
                }
            }
        }
}






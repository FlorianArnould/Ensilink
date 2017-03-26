package fr.ensicaen.lbssc.ensilink.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fr.ensicaen.lbssc.ensilink.R;

public class LoginActivity extends AppCompatActivity {

    private EditText _email;
    private EditText _password;

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
                login();
            }
        });
    }

    private void login(){
        if(validate()) {
            final ProgressDialog progressDialog = new ProgressDialog(this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authentification ...");
            progressDialog.show();
            //TODO Authentication by Jeremy
            //progressDialog.cancel();
        }
    }

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
}

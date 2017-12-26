/**
 * This file is part of Ensilink.
 * <p>
 * Ensilink is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * <p>
 * Ensilink is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with Ensilink.
 * If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Copyright, The Ensilink team :  ARNOULD Florian, ARIK Marsel, FILIPOZZI Jérémy,
 * ENSICAEN, 6 Boulevard du Maréchal Juin, 26 avril 2017
 */
package fr.ensicaen.lbssc.ensilink.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.loader.ZimbraConnection;
import fr.ensicaen.lbssc.ensilink.storage.School;

public class LoginActivity extends AppCompatActivity {
	private EditText _email;
	private EditText _password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login_screen_activity);
		_email = findViewById(R.id.input_email);
		_password = findViewById(R.id.input_password);
		Button button = findViewById(R.id.login_btn);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validate()) {
					Connection test = new Connection();
					test.execute(_email.getText().toString(), _password.getText().toString());
				}
			}
		});
	}

	/**
	 * Method that allows the application to make sure that the email address entered has the
	 * right format for the Connection to zimbra
	 *
	 * @return boolean valid that allows the Connection to proceed
	 */
	private boolean validate() {
		boolean valid = true;
		String email = _email.getText().toString();
		String password = _password.getText().toString();
		if (email.isEmpty() || !email.contains("@ecole.ensicaen.fr")) {
			_email.setError(getString(R.string.wrong_email_address_message));
			valid = false;
		}
		if (password.isEmpty()) {
			valid = false;
		}
		return valid;
	}

	/**
	 * This class was created because on Android we cannot handle network related actions in the
	 * UI thread
	 */
	private class Connection extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog _progressDialog;

		/**
		 * Method that displays the status of the Connection as soon as we click on the
		 * Connection button
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			_progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
			_progressDialog.setIndeterminate(true);
			_progressDialog.setMessage("Authentification ...");
			_progressDialog.show();
		}

		/**
		 * Method that handles the Connection to zimbra in the AsyncTask once we entered our
		 * user mail adress and our password
		 *
		 * @param params string array containing the email address and the password
		 * @return true if the Connection was successful
		 */
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				ZimbraConnection conn = new ZimbraConnection();
				conn.connect(params[0], params[1]);
				conn.close();
				return true;
			} catch (Exception e) {
				Log.d("doInBackground", "Erreur dans l'AsyncTask :" + e.getMessage(), e);
				return false;
			}
		}

		/**
		 * Method that cancels the _progressDialog at the end of the Connection to zimbra
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			_progressDialog.cancel();
			if (result) {
				SharedPreferences pref = LoginActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
				pref.edit().putString("email", _email.getText().toString()).putString("password", _password.getText().toString()).apply();
				School.getInstance().setConnected();
				Intent returnIntent = new Intent();
				returnIntent.putExtra("result", true);
				LoginActivity.this.setResult(Activity.RESULT_OK, returnIntent);
				LoginActivity.this.finish();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
				builder.setTitle(R.string.error);
				builder.setMessage(R.string.authentification_error);
				builder.setPositiveButton(android.R.string.ok, null);
				builder.create().show();
			}
		}
	}
}






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

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.Association;
import fr.ensicaen.lbssc.ensilink.storage.Mail;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emails_activity);
		if (Build.VERSION.SDK_INT >= 23) {
			getWindow().setStatusBarColor(getColor(R.color.colorPrimaryDark));
		} else if (Build.VERSION.SDK_INT >= 21) {
			getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
		}
		int unionId = getIntent().getIntExtra("UNION_ID", 0);
		int clubId = getIntent().getIntExtra("CLUB_ID", 0);
		Association association = School.getInstance().getUnion(unionId);
		if (clubId != -1) {
			Union union = (Union)association;
			association = union.getClub(clubId);
		}
		Mail mail = association.getMail(getIntent().getIntExtra("MAIL_ID", 0));
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(mail.getSubject());
		}
		TextView mailDate = (TextView)findViewById(R.id.mail_date);
		mailDate.setText(mail.getDate());
		TextView mailTransmitter = (TextView)findViewById(R.id.mailTransmitter);
		mailTransmitter.setText(mail.getTransmitter());
		TextView mailText = (TextView)findViewById(R.id.mailText);
		mailText.setText(mail.getText());
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

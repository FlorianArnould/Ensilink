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
package fr.ensicaen.lbssc.ensilink.view.clubscreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.view.OnImageLoadedForImageViewListener;
import fr.ensicaen.lbssc.ensilink.view.StudentAdapter;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * The fragment with the information of the club
 */
public class InformationFragment extends Fragment {
	private int _unionId;
	private int _clubId;
	private StudentAdapter _adapter;

	/**
	 * Method to use to create an instance of InformationFragment
	 *
	 * @param unionId the id of the union of the club
	 * @param clubId  the id of the club
	 * @return a new instance of InformationFragment
	 */
	public static InformationFragment newInstance(int unionId, int clubId) {
		InformationFragment informationFragment = new InformationFragment();
		Bundle args = new Bundle();
		args.putInt("UNION_ID", unionId);
		args.putInt("CLUB_ID", clubId);
		informationFragment.setArguments(args);
		return informationFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_unionId = getArguments().getInt("UNION_ID");
		_clubId = getArguments().getInt("CLUB_ID");
		_adapter = new StudentAdapter(School.getInstance().getUnion(_unionId).getClub(_clubId).getStudents(), getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.clubs_information_fragment, container, false);
		Club club = School.getInstance().getUnion(_unionId).getClub(_clubId);
		TextView text = view.findViewById(R.id.place);
		text.setText(club.getPlace());
		text = view.findViewById(R.id.date_club);
		if (club.getDayOfWeek(getContext()) != null) {
			text.setText(club.getDayOfWeek(getContext()));
		} else if (club.getDate() != null) {
			text.setText(club.getDate().toString());
		} else {
			text.setText(getString(R.string.unknown));
		}
		text = view.findViewById(R.id.hours);
		if (club.getTime() != null) {
			String str = club.getTime().toString() + "-";
			if (club.getDuration() != null) {
				str += club.getTime().add(club.getDuration()).toString();
			} else {
				str += getString(R.string.unknown);
			}
			text.setText(str);
		} else {
			text.setText(getString(R.string.unknown));
		}
		final ImageView imageView = view.findViewById(R.id.logo);
		club.loadLogo(imageView, new OnImageLoadedForImageViewListener(getActivity(), imageView));
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedStateInstance) {
		super.onActivityCreated(savedStateInstance);
		ListView list = getActivity().findViewById(R.id.list);
		list.setAdapter(_adapter);
	}
}
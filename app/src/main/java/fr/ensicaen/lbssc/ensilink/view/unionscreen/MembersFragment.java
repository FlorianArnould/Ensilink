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
package fr.ensicaen.lbssc.ensilink.view.unionscreen;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.OnImageLoadedListener;
import fr.ensicaen.lbssc.ensilink.view.AssociationFragment;
import fr.ensicaen.lbssc.ensilink.view.MainActivity;
import fr.ensicaen.lbssc.ensilink.view.StudentAdapter;

/**
 * @author Marsel Arik
 * @version 1.0
 */

/**
 * Fragment used to display the screen of a the members of the union
 */
public class MembersFragment extends AssociationFragment {
	private StudentAdapter _adapter;
	private View _view;

	/**
	 * Required empty public constructor
	 */
	public MembersFragment() {

	}

	/**
	 * Create an object MembersFragment
	 *
	 * @return the list of members of an union
	 */
	public static MembersFragment newInstance(int unionId) {
		MembersFragment membersFragment = new MembersFragment();
		AssociationFragment.newInstance(unionId, membersFragment);
		return membersFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_adapter = new StudentAdapter(getUnion().getStudents(), getActivity());
	}

	@Override
	public void update() {
		if (_adapter != null) {
			_adapter.update(getUnion().getStudents());
		}
		final String link = getUnion().getFacebookUrl();
		Log.d("maison", link);
		if (_view != null) {
			final ImageView imageView = (ImageView)_view.findViewById(R.id.photo);
			getUnion().loadPhoto(new OnImageLoadedListener() {
				@Override
				public void OnImageLoaded(final Drawable image) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							imageView.setImageDrawable(image);
						}
					});
				}
			});
			FloatingActionButton _fab = (FloatingActionButton)_view.findViewById(R.id.facebook);
			_fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
					startActivity(browserIntent);
				}
			});
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		_view = inflater.inflate(R.layout.members_fragment, container, false);
		return _view;
	}

	@Override
	public void onActivityCreated(Bundle savedStateInstance) {
		super.onActivityCreated(savedStateInstance);
		ListView list = getListView();
		list.setAdapter(_adapter);
		list.setOnScrollListener((MainActivity)getActivity());
		update();
	}

}

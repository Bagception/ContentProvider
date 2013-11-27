package com.example.bagception_database;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bagception_database.dummy.DummyContent;
import com.example.bagceptiondatabase.database.ActivityContext;
import com.example.bagceptiondatabase.database.DatabaseHandler;

/**
 * A fragment representing a single Context detail screen. This fragment is
 * either contained in a {@link ContextListActivity} in two-pane mode (on
 * tablets) or a {@link ContextDetailActivity} on handsets.
 */
public class ContextDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The content this fragment is presenting.
	 */
	private ActivityContext mItem;

	/**
	 * The UI Elements showing the details of the Context
	 */
	
	private TextView textContextName;
	private TextView textContextDes;
	private TextView textContextVis;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ContextDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = DatabaseHandler.getInstance(getActivity()).getActivityContext(getArguments().getLong(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_context_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.context_detail))
					.setText(mItem.content);
		}

		return rootView;
	}
}

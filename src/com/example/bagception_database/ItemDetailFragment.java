package com.example.bagception_database;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bagceptiondatabase.database.DatabaseHandler;
import com.example.bagceptiondatabase.database.Item;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class ItemDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Item mItem;
	private LayoutInflater mInflater;
	
	/**
	 * The UI elements showing the details of the Item
	 */
	private TextView textName;
	private TextView textDescritption;
	private Spinner spinner;
	private CharSequence stringVisibility;
	private TextView textVisibility;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			
			// Should use the ContentProvider ideally
			mItem = DatabaseHandler.getInstance(getActivity()).getItem(getArguments().getLong(ARG_ITEM_ID));
		}
	}

	Button saveButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_detail,
				container, false);
		

		if (mItem != null) {
			
			textName = ((TextView) rootView.findViewById(R.id.textName));
			textName.setText(mItem.name);
			
			textDescritption = ((TextView) rootView.findViewById(R.id.textDescription));
			textDescritption.setText(mItem.description);
			
			textVisibility = ((TextView) rootView.findViewById(R.id.textVisibility));
			textVisibility.setText(mItem.visibility);
			
		}
		
		
		saveButton = (Button) rootView.findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {

				updateItemFromUI();
			}
		});
		
		return rootView;
	}
	
	@Override
	public void onPause() {
		
		super.onPause();
		
		// Statt onPause() soll Inhalt bei ButtonClick "Speichern" gespeichert werden
		//updateItemFromUI();		
	}
	
	
	// Method to store data
	public void updateItemFromUI() {
		
		if(mItem != null) {
			
			mItem.name = textName.getText().toString();
			mItem.description = textDescritption.getText().toString();
			mItem.visibility = textVisibility.getText().toString();

			DatabaseHandler.getInstance(getActivity()).putItem(mItem);
			
			AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
			ad.setTitle("Gespeichert");
			ad.setMessage("Gegenstand wurde gespeichert");
			
			ad.setButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					Intent i = new Intent(getActivity(), ItemListActivity.class);
					startActivity(i);
				}
			});
			
			ad.setIcon(R.drawable.ic_launcher);
			ad.show();
		}
	}
}

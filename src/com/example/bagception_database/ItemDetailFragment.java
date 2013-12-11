package com.example.bagception_database;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bagceptiondatabase.database.DatabaseHandler;
import com.example.bagceptiondatabase.database.Item;

import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;

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
	de.uniulm.bagception.bundlemessageprotocol.entities.Item item;
	
	/**
	 * The UI elements showing the details of the Item
	 */
	private TextView textName;
	private TextView textDescritption;
	private Spinner spinner;
	private TextView textVisibility;
	private View rootView;
	private RadioButton radioButton;
	private RadioButton radioButton2;
	private SQLiteDatabase db;
	private static String id;
	private static Bundle tagid;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String args = getArguments().toString();
		
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			
			// Should use the ContentProvider ideally
			mItem = DatabaseHandler.getInstance(getActivity()).getItem(getArguments().getLong(ARG_ITEM_ID));
		} 

		if (getArguments().containsKey("item")){
			Log.d("MyActivity", getArguments().toString());
			String itemJsonString = getArguments().getString("item");
			try {
				JSONObject json = new JSONObject(itemJsonString);
				item = de.uniulm.bagception.bundlemessageprotocol.entities.Item.fromJSON(json);
				id = item.getIds().get(0);
				mItem = new Item();
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	Button saveButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
		

		if (mItem != null) {
			
			textName = ((TextView) rootView.findViewById(R.id.textName));
			textName.setText(mItem.name);
			
			textDescritption = ((TextView) rootView.findViewById(R.id.textDescription));
			textDescritption.setText(mItem.description);
			
			//textVisibility = ((TextView) rootView.findViewById(R.id.textVisibility));
			//textVisibility.setText(mItem.visibility);
			
			if(mItem.visibility == 0 || mItem.visibility == -1) {
				
				radioButton = (RadioButton) rootView.findViewById(R.id.visibility);
				radioButton.setChecked(true);
				
				radioButton2 = (RadioButton) rootView.findViewById(R.id.visibility2);
				radioButton2.setChecked(false);
			} else {
				radioButton = (RadioButton) rootView.findViewById(R.id.visibility);
				radioButton.setChecked(false);
				
				radioButton = (RadioButton) rootView.findViewById(R.id.visibility2);
				radioButton.setChecked(true);
			}
			
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
			
			}

			if(id != null){
				mItem.description = id;
			} else {
				mItem.description = textDescritption.getText().toString();
			}
				
				
			if(rootView.findViewById(R.id.visibility).isSelected() == true) {
				
				mItem.visibility = 0;
			} else {
				
				mItem.visibility = 1;
			}

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



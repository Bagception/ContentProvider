package com.example.bagception_database;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.bagception_database.gui.ShowFoundTags;
import com.example.bagception_database.gui.Test_GUI_1;
import com.example.bagception_database.gui.Test_GUI_2;
import com.example.bagceptiondatabase.database.DatabaseHandler;
import com.example.bagceptiondatabase.database.*;

import de.philipphock.android.lib.logging.LOG;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.protocol.bundle.constants.Command;

public class MainActivity extends Activity implements 
	BundleMessageReactor  {

	private BundleMessageActor bma;
	protected SQLiteDatabase db;
	protected Cursor cursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		bma = new BundleMessageActor(this);
		
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onResume() {
		bma.register(this);
		new BundleMessageHelper(this).sendCommandBundle(Command.TRIGGER_SCAN_DEVICES.toBundle());
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		bma.unregister(this);
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void goToItem(View view) {
		Intent item_intent = new Intent(this, ItemListActivity.class);
		startActivity(item_intent);
	}

	public void goToTestGUI_1(View view) {
		Intent place_intent = new Intent(this, Test_GUI_1.class);
		startActivity(place_intent);
	}

	public void goToTestGUI_2(View view) {
		Intent activity_intent = new Intent(this, Test_GUI_2.class);
		startActivity(activity_intent);
	}

	/**
	 * @author Bianca Strobel
	 */
	public void searchForNewBag(View view) {
		Intent search_intent = new Intent(
				"de.uniulm.bagception.bluetoothClient.UI.AddNewBagStartActivity");
		startActivity(search_intent);
	}

	public void showFoundItems(View view) {
		Intent showFoundItems = new Intent(this, ShowFoundTags.class);
		startActivity(showFoundItems);
	}

	public void showSettings(View view) {
		Intent showSettings = new Intent(
				"de.uniulm.bagception.clientSettings.UI.SettingsActivity");
		startActivity(showSettings);
	}
	
	
	// \\BundleMessageReactor//

	@Override
	public void onBundleMessageRecv(Bundle b) {
		//those messages come from the remote bluetooth device, not from the BluetoothMiddleware		

		LOG.out(this, b);
		BUNDLE_MESSAGE msg = BundleMessage.getInstance().getBundleMessageType(b);

		switch (msg){
			
		case ITEM_FOUND:
		case ITEM_NOT_FOUND:
			final Intent startActivityIntent_new = new Intent(this,ItemDetailActivity.class);
			final Intent startActivityIntent_add = new Intent(this, ItemListActivity.class);
			Item i;
			try {
				i = BundleMessage.getInstance().toItemFound(b);
				String itemJson = i.toString();
				String id = i.getName();
				id = i.getIds().get(0);
				startActivityIntent_new.putExtra("item", itemJson);
				
				String item_name = DatabaseHandler.getInstance(getApplication()).searchItem(id);
				
				if (item_name != null){
					
					AlertDialog.Builder ad = new AlertDialog.Builder(this);
					ad.setMessage("Item gefunden: " + item_name);
					
					AlertDialog alert = ad.create();
					alert.show();
				} else {
					
					
					if (msg == BUNDLE_MESSAGE.ITEM_NOT_FOUND){
						
						
						AlertDialog.Builder ad = new AlertDialog.Builder(this);
						ad.setMessage("Wie wollen Sie weiter vorgehen?");
						
						ad.setPositiveButton("Item erweitern", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								startActivity(startActivityIntent_add);							
							}
						});
						
						ad.setNegativeButton("Item anlegen", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								startActivity(startActivityIntent_new);
							}
						});
						
						AlertDialog alert = ad.create();
						alert.show();
						
					}else{
						
					}
				}
			} catch (JSONException e) {
				Toast.makeText(this, "error reading item", Toast.LENGTH_SHORT)
				.show();
			}
		break;
		
			default: break;
		}
		
	}

	@Override
	public void onBundleMessageSend(Bundle b) {
		//nothing to do here
		
	}

	@Override
	public void onResponseMessage(Bundle b) {
		//nothing to do here
		
	}

	@Override
	public void onResponseAnswerMessage(Bundle b) {
		//nothing to do here
	}

	@Override
	public void onStatusMessage(Bundle b) {
	}

	@Override
	public void onCommandMessage(Bundle b) {
		//nothing to do here
	}

	@Override
	public void onError(Exception e) {
		//??
	}
	
	//BundleMessageReactor\\

}

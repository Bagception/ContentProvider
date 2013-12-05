package com.example.bagception_database;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.bagception_database.gui.ShowFoundTags;
import com.example.bagception_database.gui.Test_GUI_1;
import com.example.bagception_database.gui.Test_GUI_2;

import de.philipphock.android.lib.logging.LOG;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.protocol.bundle.constants.Command;
import de.uniulm.bagception.protocol.bundle.constants.StatusCode;
import de.uniulm.bagception.services.ServiceNames;

public class MainActivity extends Activity implements 
	BundleMessageReactor  {

	
	private BundleMessageActor bmActor; 
	private BundleMessageHelper messengerHelper;
	
	private final Handler delayedExecutionHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		bmActor = new BundleMessageActor(this); //to recv  messages
		messengerHelper = new BundleMessageHelper(this); //to send messages
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//register on messages
		bmActor.register(this);
		
		
		//start middleware
		startService(new Intent(ServiceNames.BLUETOOTH_CLIENT_SERVICE));
		
		//wait until service is started
		delayedExecutionHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Bundle resendClientStatusCommand = Command.RESEND_STATUS.toBundle();
				messengerHelper.sendCommandBundle(resendClientStatusCommand);
			}
		}, 300);
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		bmActor.unregister(this);
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
			
			Item i;
			try {
				i = BundleMessage.getInstance().toItemFound(b);
				String itemMsgString = "Item found: "+i.getName();
				if (msg == BUNDLE_MESSAGE.ITEM_NOT_FOUND){
					itemMsgString = "unknown Tag: "+i.getIds().get(0);
				}
				Toast.makeText(this,itemMsgString , Toast.LENGTH_SHORT)
				.show();
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
		//messages from bt-middleware
		StatusCode status = StatusCode.getStatusCode(b);
		switch (status){
		case CONNECTED:
			Toast.makeText(this, "connected with bag", Toast.LENGTH_SHORT).show();
			break;
			
		case DISCONNECTED:
			Bundle scanDevicesCommand = Command.getCommandBundle(Command.TRIGGER_SCAN_DEVICES);
			messengerHelper.sendCommandBundle(scanDevicesCommand);
			break;
			
			default: break;
		}
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

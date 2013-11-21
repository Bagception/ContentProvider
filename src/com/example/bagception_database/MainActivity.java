package com.example.bagception_database;

import com.example.bagception_database.gui.Test_GUI_1;
import com.example.bagception_database.gui.Test_GUI_2;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void goToItem(View view){
    	Intent item_intent = new Intent(this, ItemListActivity.class);
    	startActivity(item_intent);
    }

	public void goToTestGUI_1(View view){
    	Intent place_intent = new Intent(this, Test_GUI_1.class);
    	startActivity(place_intent);
    }
    
    public void goToTestGUI_2(View view){
    	Intent activity_intent = new Intent(this, Test_GUI_2.class);
    	startActivity(activity_intent);
    }
}

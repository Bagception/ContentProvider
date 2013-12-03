package com.example.bagception_database.gui;

import com.example.bagception_database.R;
import com.example.bagception_database.R.layout;
import com.example.bagception_database.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ShowFoundTags extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_found_tags);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_found_tags, menu);
		return true;
	}

}

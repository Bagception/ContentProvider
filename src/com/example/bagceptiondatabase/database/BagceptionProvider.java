package com.example.bagceptiondatabase.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class BagceptionProvider extends ContentProvider {
	
	// All URIs share these parts
	public static final String AUTHORITY = "com.example.bagceptiondatabase.database.provider";
	public static final String SCHEME = "content://";
	
	// URIs
	// Used for all items
	public static final String ITEMS = SCHEME + AUTHORITY + "/item";  
	public static final Uri URI_ITEMS = Uri.parse(ITEMS);
	
	// Used for a single item, just add the id to the end
	public static final String ITEM_BASE = ITEMS + "/";
	
	public BagceptionProvider() {
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Implement this to handle requests to delete one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public String getType(Uri uri) {
		// TODO: Implement this to handle requests for the MIME type of the data
		// at the given URI.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO: Implement this to handle requests to insert a new row.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public boolean onCreate() {
		
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		Cursor result = null;
		
		// Getting all items
		if(URI_ITEMS.equals(uri)) {
			
			result = DatabaseHandler
					.getInstance(getContext())
					.getReadableDatabase()
					.query(Item.TABLE_NAME, Item.FIELDS, null, null, null, null, null);
			result.setNotificationUri(getContext().getContentResolver(), URI_ITEMS);
		} 
		// Getting a single item
		else if (uri.toString().startsWith(ITEM_BASE)) {
			
			final long id = Long.parseLong(uri.getLastPathSegment());
			result = DatabaseHandler.getInstance(getContext()).getReadableDatabase().query(Item.TABLE_NAME, Item.FIELDS, null, null, null, null, null);
			result.setNotificationUri(getContext().getContentResolver(), URI_ITEMS);
		} else {		
			throw new UnsupportedOperationException("Not yet implemented");
		}
		
		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO: Implement this to handle requests to update one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}

package com.example.bagceptiondatabase.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper{

	private static DatabaseHandler singleton;
	
	public static DatabaseHandler getInstance(final Context context) {
		if(singleton == null) {
			singleton = new DatabaseHandler(context);
		}
		return singleton;
	}
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "bagceptionDatabase";
	
	private final Context context;
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context.getApplicationContext();
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(Item.CREATE_TABLE);
		//db.execSQL(ActivityContext.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized Item getItem(final long id) {
		
		final SQLiteDatabase db = this.getReadableDatabase();
		final Cursor cursor = db.query(Item.TABLE_NAME, Item.FIELDS, Item.COL_ID + " IS ?", new String[] {String.valueOf(id)}, null, null, null, null);
		
		if(cursor == null || cursor.isAfterLast()) {
			return null;
		}
		
		Item item = null;
		if(cursor.moveToFirst()) {
			item = new Item(cursor);
		}
		
		cursor.close();
		return item;
	}
	
	// The result is the number of rows that were deleted. In this case it should never be anything except 0 or 1
	public synchronized int removeItem(final Item item) {
		
		final SQLiteDatabase db = this.getReadableDatabase();
		final int result = db.delete(Item.TABLE_NAME, Item.COL_ID + " IS ?", new String[] {Long.toString(item.id)});
		
		if(result > 0) {
			notifyProviderOnItemChange();
		}
		return result;
	}
	
	// "putItem" is both, insert and update method
	public synchronized boolean putItem(final Item item) {
		
		boolean success = false;
		int result = 0;
		final SQLiteDatabase db = this.getWritableDatabase();
		
		if(item.id > -1) {
			result += db.update(Item.TABLE_NAME, item.getContent(), Item.COL_ID + " IS ?", new String[] {String.valueOf(item.id)});
		}
		
		if(result > 0) {
			success = true;
		} else {
			// Update failed or wasn't possible, insert instead
			final long id = db.insert(Item.TABLE_NAME, null, item.getContent());
			
			if(id > 1) {
				item.id = id;
				success = true;
			}
		}
		
		if(success) {
			notifyProviderOnItemChange();
		}
		return success;
	}
	
	private void notifyProviderOnItemChange() {
		
		context.getContentResolver().notifyChange(BagceptionProvider.URI_ITEMS, null, false);
	}
	
	public String searchItem(final String id) {
		
		final SQLiteDatabase db = this.getReadableDatabase();
		final Cursor cursor = db.query(Item.TABLE_NAME, Item.FIELDS, Item.COL_TAGID + " IS ?", new String[] {String.valueOf(id)}, null, null, null, null);
		
		if(cursor == null || cursor.isAfterLast()) {
			return null;
		}
		
		String item = null;
		if(cursor.moveToFirst()) {
			Item i = new Item(cursor);
			int index = cursor.getColumnIndex("name");
			item = cursor.getString(index);
			//item = i.getContent().toString();
		}
		
		cursor.close();
		return item;
	}
	
}

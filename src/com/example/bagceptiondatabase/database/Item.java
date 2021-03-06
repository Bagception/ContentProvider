package com.example.bagceptiondatabase.database;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * 
 * @author Michael
 * A class representation of a row in table "Item".
 *
 */


public class Item {

	// SQL convention says Table name should be singular, so "Item", not "Items"
	public static final String TABLE_NAME = "Item";
	
	// Naming the id column with an underscore is good to be consistent with other Android things.
	public static final String COL_ID = "_id";
	
	// Columns of the Table
	public static final String COL_NAME = "name";
	public static final String COL_TAGID = "tagid";
	public static final String COL_VIS = "visibility";
	public static final String COL_CONTEXT = "context";
	
	// Database projection, so order is consistent
	public static final String[] FIELDS = {COL_ID, COL_CONTEXT,COL_NAME, COL_TAGID, COL_VIS};
	
	// SQL code that creates a Table for storing Items in
	public static final String CREATE_TABLE = 
			"CREATE TABLE " + TABLE_NAME + "(" 
			+ COL_ID + " INTEGER PRIMARY KEY,"
			+ COL_CONTEXT + " TEXT NOT NULL DEFAULT '',"
			+ COL_NAME + " TEXT NOT NULL DEFAULT '',"
			+ COL_TAGID + " TEXT NULL DEFAULT '',"
			+ COL_VIS + " TEXT NULL DEFAULT ''"
			+ ")";
	
	// Fields corresponding to database columns
	public long id = -1;
	public String name = "";
	public String tagid = "";
	public int visibility = -1;
	
	// No need to do anything, fields are already set to default values above
	public Item() {
	
	}
	
	// Convert informations from database into a Item object
	public Item(final Cursor cursor){
		this.id = cursor.getLong(0);
		this.name = cursor.getString(1);
		this.tagid = cursor.getString(2);
		this.visibility = cursor.getInt(3);
	}
	
	// Return the fields in a ContentValues object, suitable for insertion into the database
	public ContentValues getContent() {
		final ContentValues values = new ContentValues();
		values.put(COL_NAME, name);
		values.put(COL_TAGID, tagid);
		values.put(COL_VIS, visibility);
		
		return values;
	}
}

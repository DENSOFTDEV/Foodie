package com.iblinfotech.foodierecipe.SqLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.iblinfotech.foodierecipe.model.RecipeIngredientsData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "contactsManager";
	// Contacts table name
	private static final String TABLE_CONTACTS = "contacts";
	private static final String TABLE_INGREDIENTS = "contacts";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_PH_NO = "phone_number";
	private static final String KEY_INGREDIENTS_NAME = "ingredient_name";
	private static final String KEY_INGREDIENTS_MESURE = "ingredient_mesure";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_NAME + " TEXT,"
				+ KEY_PH_NO + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
		String CREATE_INGREDIENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_INGREDIENTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_INGREDIENTS_NAME + " TEXT,"
				+ KEY_INGREDIENTS_MESURE + " TEXT" + ")";
		db.execSQL(CREATE_INGREDIENTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	public void addContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, contact.getName()); // Contact Name
		values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone

		// Inserting Row
		db.insert(TABLE_CONTACTS, null, values);
		db.close(); // Closing database connection
	}
	public void addIngredient(Ingredients ingredients) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, ingredients.getI_name()); // Contact Name
		values.put(KEY_PH_NO, ingredients.getI_mesure()); // Contact Phone

		// Inserting Row
		db.insert(TABLE_INGREDIENTS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	Contact getContact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
				KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		// return contact
		return contact;
	}
	
	// Getting All Contacts
	public List<Contact> getAllContacts() {
		List<Contact> contactList = new ArrayList<Contact>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Contact contact = new Contact();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setName(cursor.getString(1));
				contact.setPhoneNumber(cursor.getString(2));
				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}
	public List<Ingredients> getAllIngredients() {
		List<Ingredients> ingredientsdataList = new ArrayList<Ingredients>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_INGREDIENTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Ingredients ingredients = new Ingredients();
				ingredients.setI_name(cursor.getString(1));
				ingredients.setI_mesure(cursor.getString(2));
//				ingredients.setPhoneNumber(cursor.getString(2));
				// Adding contact to list
				ingredientsdataList.add(ingredients);
			} while (cursor.moveToNext());
		}

		// return contact list
		return ingredientsdataList;
	}
//	public ArrayList<HashMap<String, String>> getAllIngredients() {
//		ArrayList<HashMap<String, String>> wordList = new ArrayList<>();
//		// Select All Query
//		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
//
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cursor = db.rawQuery(selectQuery, null);
//
//		// looping through all rows and adding to list
//		if (cursor.moveToFirst()) {
//			do {
//				Contact contact = new Contact();
//				contact.setID(Integer.parseInt(cursor.getString(0)));
//				contact.setName(cursor.getString(1));
//				contact.setPhoneNumber(cursor.getString(2));
//				// Adding contact to list
//				contactList.add(contact);
//			} while (cursor.moveToNext());
//		}
//
//		// return contact list
//		return wordList;
//	}
//	public ArrayList<HashMap<String, String>> getAllPlace() {
//		ArrayList<HashMap<String, String>> wordList;
//		wordList = new ArrayList<HashMap<String, String>>();
//		String selectQuery = "SELECT  * FROM " + tablename;
//		SQLiteDatabase database = this.getWritableDatabase();
//		Cursor cursor = database.rawQuery(selectQuery, null);
//		if (cursor.moveToFirst()) {
//			do {
//
//				HashMap<String, String> map = new HashMap<String, String>();
//				map.put("id", cursor.getString(0));
//				map.put("place", cursor.getString(1));
//				map.put("country", cursor.getString(2));
//
//				wordList.add(map);
//			} while (cursor.moveToNext());
//		}
//
//		// return contact list
//		return wordList;
//	}
	// Updating single contact
	public int updateContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, contact.getName());
		values.put(KEY_PH_NO, contact.getPhoneNumber());

		// updating row
		return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
	}

	// Deleting single contact
	public void deleteContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
		db.close();
	}


	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}

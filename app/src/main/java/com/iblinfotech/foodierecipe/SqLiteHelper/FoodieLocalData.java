/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iblinfotech.foodierecipe.SqLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FoodieLocalData {

    private final Context context;
    private SQLiteDatabase sqLiteDb;

    private static final String DATABASE_NAME = "FoodieShoppingList.db";
    private static final int DATABASE_VERSION = 1;

    // table name
    public static final String SHOPPING_CART_MASTER = "shopping_cart";
    public static final String KEY_RECIPE_ID = "recipe_id";
    public static final String KEY_PERSON = "person_no";
    public static final String KEY_RECIPE_DATA = "recipe_data";
    public static final String KEY_RECIPE_INGREDIENT_DATA = "recipe_ingredient_data";

    // Quarry for create table favorite_exhibits_table
    private static final String CREATE_TABLE_FAVORITE_EXHIBITS = "CREATE TABLE IF NOT EXISTS "
            + SHOPPING_CART_MASTER + " ("
            + KEY_RECIPE_ID + " TEXT PRIMARY KEY NOT NULL, "
            + KEY_PERSON + " TEXT, "
            + KEY_RECIPE_DATA + " TEXT, "
            + KEY_RECIPE_INGREDIENT_DATA + " TEXT);";

    public FoodieLocalData(Context context) {
        this.context = context;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_FAVORITE_EXHIBITS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDb, int oldVersion, int newVersion) {
            if (oldVersion != newVersion) {
                sqLiteDb.execSQL("DROP DATABASE IF EXISTS " + DATABASE_NAME);
                onCreate(sqLiteDb);
            }
        }
    }

    // ---opens the database---
    public FoodieLocalData open() throws SQLException {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        sqLiteDb = dbHelper.getWritableDatabase();
        sqLiteDb = dbHelper.getReadableDatabase();

        if (!sqLiteDb.isReadOnly()) {
            // Enable foreign key constraints
            sqLiteDb.execSQL("PRAGMA foreign_keys=ON;");
        }
        return this;
    }

    // ---closes the database---
    public void close() {
        if (sqLiteDb != null && sqLiteDb.isOpen()) {
//            dbHelper.close();
            sqLiteDb.close();
        }
    }

    public boolean insertData(String tableName, ContentValues values) {
        open();
        sqLiteDb.insert(tableName, null, values);
        close();
        return true;
    }

    public boolean update(String tableName, ContentValues contentValues, String selection, String[] selectionArgs) {
        open();
//        Log.e("UPDATES", " contentValues- " + contentValues);
//        Log.e("UPDATES", " selection- " + selection);
//        Log.e("UPDATES", " selectionArgs- " + selectionArgs);

        sqLiteDb.update(tableName, contentValues, selection, selectionArgs);
        close();
        return true;
    }

    public boolean delete(String tableName, String selection, String[] selectionArgs) {
        open();
        sqLiteDb.delete(tableName, selection, selectionArgs);
        close();
        return true;
    }

    public Cursor getShoppingCartList() {
        open();
        String selectRowQuery = "SELECT * FROM " + SHOPPING_CART_MASTER;
        Log.e("Row Query", " - " + selectRowQuery);

        Cursor cursor = sqLiteDb.rawQuery(selectRowQuery, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Log.e("cursor", "------");
                } while (cursor.moveToNext());
            }
        }
        close();
        return cursor;
    }

    public String getrecipeId(String recipeData) {
        open();
        String recipeId = null;
        String checkQuery = "SELECT * FROM " + SHOPPING_CART_MASTER + " WHERE " + KEY_RECIPE_DATA + "= '" + recipeData + "'";
        Cursor cursor = sqLiteDb.rawQuery(checkQuery, null);
        boolean exists = (cursor.getCount() > 0);
        if (exists) {
            cursor.moveToFirst();
            do {
            recipeId = cursor.getString(cursor.getColumnIndex(FoodieLocalData.KEY_RECIPE_ID));
            } while (cursor.moveToNext());
            return recipeId;
        } else {
            return recipeId;
        }
    }
    public String getPersonNo(String recipeID) {
        open();
        String mPerson = null;
        String checkQuery = "SELECT * FROM " + SHOPPING_CART_MASTER + " WHERE " + KEY_RECIPE_ID + "= '" + recipeID + "'";
        Cursor cursor = sqLiteDb.rawQuery(checkQuery, null);
        boolean exists = (cursor.getCount() > 0);
        if (exists) {
            cursor.moveToFirst();
            do {
                mPerson = cursor.getString(cursor.getColumnIndex(FoodieLocalData.KEY_PERSON));
            } while (cursor.moveToNext());
            return mPerson;
        } else {
            return mPerson;
        }
    }

        public boolean checkPersonNo(String personNo) {
        open();
        Cursor cursor = sqLiteDb.query(SHOPPING_CART_MASTER,
                new String[]{KEY_PERSON},
                 KEY_PERSON + " = ?",
                new String[]{personNo},
                null, null, null, null);
        if (cursor.moveToFirst())
            return true; //row exists
        else
            return false;
    }
//
//   public boolean checkEvent(String recipeData, String personNo, String ingredientData) {
//        open();
//        Cursor cursor = sqLiteDb.query(SHOPPING_CART_MASTER,
//                new String[]{KEY_RECIPE_DATA, KEY_PERSON, KEY_RECIPE_INGREDIENT_DATA},
//                KEY_RECIPE_DATA + " = ? and " + KEY_PERSON + " = ? and " + KEY_RECIPE_INGREDIENT_DATA + " = ?",
//                new String[]{recipeData, personNo, ingredientData},
//                null, null, null, null);
//        if (cursor.moveToFirst())
//            return true; //row exists
//        else
//            return false;
//    }
//
 public boolean checkDataPersonAlready(String recipeId, String mPerson) {
        open();
        Cursor cursor = sqLiteDb.query(SHOPPING_CART_MASTER,
                new String[]{KEY_RECIPE_ID, KEY_PERSON},
                KEY_RECIPE_ID + " = ? and " + KEY_PERSON + " = ?",
                new String[]{recipeId, mPerson},
                null, null, null, null);
        if (cursor.moveToFirst())
            return true; //row exists
        else
            return false;
    }
    public boolean checkDataAlreadyExists(String recipeData) {
        open();
        Cursor cursor = sqLiteDb.query(SHOPPING_CART_MASTER,
                new String[]{KEY_RECIPE_DATA},
                KEY_RECIPE_DATA + " = ?",
                new String[]{recipeData},
                null, null, null, null);
        if (cursor.moveToFirst())
            return true; //row exists
        else
            return false;
    }
    public boolean checkTokenAlreadyExists(String mSingleRecipeToken) {
        open();
        Cursor cursor = sqLiteDb.query(SHOPPING_CART_MASTER,
                new String[]{KEY_RECIPE_ID},
                KEY_RECIPE_ID + " = ?",
                new String[]{mSingleRecipeToken},
                null, null, null, null);
        if (cursor.moveToFirst())
            return true; //row exists
        else
            return false;
    }
//    public boolean checkDataPersonAlready(String recipeId, String mPerson) {
//        open();
//        Cursor cursor = sqLiteDb.query(SHOPPING_CART_MASTER,
//                new String[]{KEY_RECIPE_DATA},
//                KEY_RECIPE_DATA + " = ?",
//                new String[]{recipeData},
//                null, null, null, null);
//        if (cursor.moveToFirst())
//            return true; //row exists
//        else
//            return false;
//    }
    public boolean checkRecipeId(String recipeKey) {
        open();
        Cursor cursor = sqLiteDb.query(SHOPPING_CART_MASTER,
                new String[]{KEY_RECIPE_ID},
                KEY_RECIPE_ID + " = ?",
                new String[]{recipeKey},
                null, null, null, null);
        if (cursor.moveToFirst())
            return true; //row exists
        else
            return false;
    }
}
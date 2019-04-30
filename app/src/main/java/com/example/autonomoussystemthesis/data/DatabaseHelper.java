//package com.example.autonomoussystemthesis.data;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class DatabaseHelper extends SQLiteOpenHelper {
//
//    public static final String LOG_TAG = DatabaseHelper.class.getSimpleName();
//
//    // Name of the database file
//    private static final String DATABASE_NAME = "autonomousSystem.db";
//
//    private static final String TABLE_NAME = "personalityInfo";
//    private static final String COL1 = "_id";
//    private static final String COL2 = "personaType";
//    private static final String COL3 = "color";
//    private static final String COL4 = "musicGenre";
//    private static final String COL5 = "distance";
//
//    // Database version. If you change the database schema, you must increment the database version.
//    private static final int DATABASE_VERSION = 1;
//
//    // Constructs a new instance of {@link DatabaseHelper}.   @param context of the app
//    public DatabaseHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    // This is called when the database is created for the first time.
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        // Create a String that contains the SQL statement to create the pets table
//        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
//                + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COL2 + " TEXT NOT NULL, " + COL3 + " TEXT NOT NULL, "
//                + COL4 + " TEXT NOT NULL, " + COL5 + " INTEGER); ";
//        // Execute the SQL statement
//        db.execSQL(createTable);
//    }
//
//    // This is called when the database needs to be upgraded.
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // The database is still at version 1, so there's nothing to do be done here.
//    }
//
//    // when we want to add data
//    public boolean addData(String personaType, String color, String musicGenre, int distance) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL2, personaType);
//        contentValues.put(COL3, color);
//        contentValues.put(COL4, musicGenre);
//        contentValues.put(COL5, distance);
//
//        long result = db.insert(TABLE_NAME, null, contentValues);
//
//        return result != -1;
//    }
//
//    public Cursor showData() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//        return data;
//    }
//}
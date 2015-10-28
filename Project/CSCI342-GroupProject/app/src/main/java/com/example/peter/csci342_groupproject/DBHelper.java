package com.example.peter.csci342_groupproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "gameDB.db";
    private static final int DATABASE_VERSION = 4; //Change this when you change your database

    private static final String MAIN_CREATE = "create table " + MainTable.MainEntry.TABLE_NAME + " ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT," //for cursor adaptor to work
            + MainTable.MainEntry.COLUMN_NAME_BASE_LIVES + " integer, "
            + MainTable.MainEntry.COLUMN_NAME_BASE_DAMAGE + " real, "
            + MainTable.MainEntry.COLUMN_NAME_BASE_SPEED + " real, "
            + MainTable.MainEntry.COLUMN_NAME_PLAYER_CURRENCY + " integer, "
            + MainTable.MainEntry.COLUMN_NAME_OPTION_FX + " integer, "
            + MainTable.MainEntry.COLUMN_NAME_OPTION_MUSIC + " integer, "
            + MainTable.MainEntry.COLUMN_NAME_OPTION_SOUND + " real, "
            + MainTable.MainEntry.COLUMN_NAME_OPTION_TILT + " integer"
            + ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL(MAIN_CREATE);
    }

    @Override
    //When our database version changes, in this case, simply delete the database and recreate it:
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("DROP TABLE IF EXISTS " + MainTable.MainEntry.TABLE_NAME);
        onCreate(DB);
    }

    public boolean updateMainTable(DBHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues args = new ContentValues();
        GameData gd = GameData.getInstance();
        args.put(MainTable.MainEntry.COLUMN_NAME_BASE_LIVES, gd.getBaseLives());
        args.put(MainTable.MainEntry.COLUMN_NAME_BASE_DAMAGE, gd.getBaseDamage());
        args.put(MainTable.MainEntry.COLUMN_NAME_BASE_SPEED, gd.getBaseSpeed());
        args.put(MainTable.MainEntry.COLUMN_NAME_PLAYER_CURRENCY, gd.getCurrency());
        args.put(MainTable.MainEntry.COLUMN_NAME_OPTION_FX, gd.getSoundFX());
        args.put(MainTable.MainEntry.COLUMN_NAME_OPTION_MUSIC, gd.getMusic());
        args.put(MainTable.MainEntry.COLUMN_NAME_OPTION_SOUND, gd.getVolume());
        return db.update(MainTable.MainEntry.TABLE_NAME, args, null, null) > 0;
    }

    @Override
    public void onOpen(SQLiteDatabase DB) {
        super.onOpen(DB);
        if (!DB.isReadOnly()) {
            //Enable foreign key constraints
            DB.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}

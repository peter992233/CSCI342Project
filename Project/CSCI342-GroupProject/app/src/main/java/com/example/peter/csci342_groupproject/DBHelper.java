package com.example.peter.csci342_groupproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "gameDB.db";
    private static final int DATABASE_VERSION = 2; //Change this when you change your database

    private static final String MAIN_CREATE = "create table " + MainTable.MainEntry.TABLE_NAME + " ("
            + MainTable.MainEntry.COLUMN_NAME_BASE_LIVES + " integer, "
            + MainTable.MainEntry.COLUMN_NAME_BASE_DAMAGE + " real, "
            + MainTable.MainEntry.COLUMN_NAME_BASE_SPEED + " real, "
            + MainTable.MainEntry.COLUMN_NAME_PLAYER_CURRENCY + " integer, "
            + MainTable.MainEntry.COLUMN_NAME_OPTION_FX + " integer, "
            + MainTable.MainEntry.COLUMN_NAME_OPTION_MUSIC + " integer, "
            + MainTable.MainEntry.COLUMN_NAME_OPTION_SOUND + " real, "
            + MainTable.MainEntry.COLUMN_NAME_OPTION_TILT + " integer"
            + ");";

    private static final String HS_CREATE = "create table " + HighScoreTable.HighScoreEntry.TABLE_NAME + " ("
            + HighScoreTable.HighScoreEntry.COLUMN_NAME_UserName + " text not null, "
            + HighScoreTable.HighScoreEntry.COLUMN_NAME_HighScore + " integer"
            + ");";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL(MAIN_CREATE);
        DB.execSQL(HS_CREATE);
    }

    @Override
    //When our database version changes, in this case, simply delete the database and recreate it:
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("DROP TABLE IF EXISTS " + MainTable.MainEntry.TABLE_NAME);
        DB.execSQL("DROP TABLE IF EXISTS " + HighScoreTable.HighScoreEntry.TABLE_NAME);
        onCreate(DB);
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

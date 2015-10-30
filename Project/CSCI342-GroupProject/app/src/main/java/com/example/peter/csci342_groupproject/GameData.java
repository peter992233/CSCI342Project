package com.example.peter.csci342_groupproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Lorthris on 28/10/2015.
 */
public class GameData {

    private static GameData GD = new GameData();

    private Integer baseLives;
    private Double baseDamage;
    private Double baseSpeed;
    private Integer currency;
    private Boolean soundFX;
    private Boolean music;
    private Double volume;

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private GameData() {
        baseLives = 1;
        baseDamage = 1.0;
        baseSpeed = 1.0;
        currency = 0;
        soundFX = true;
        music = true;
        volume = 1.0;
    }

    /* Static 'instance' method */
    public static GameData getInstance() {
        return GD;
    }

    public void populateFromDB(DBHelper dbHelper) {

        SQLiteDatabase reader = dbHelper.getReadableDatabase();
        Cursor cursor = reader.rawQuery("SELECT * FROM " + MainTable.MainEntry.TABLE_NAME, null);

        if (cursor.getCount() == 0) {//if user doesnt exist, create one
            createPlayer(dbHelper);
            reader = dbHelper.getReadableDatabase();
            cursor = reader.rawQuery("SELECT * FROM " + MainTable.MainEntry.TABLE_NAME, null);
        }

        cursor.moveToFirst();
        this.baseLives = cursor.getInt(cursor.getColumnIndex(MainTable.MainEntry.COLUMN_NAME_BASE_LIVES));
        this.baseDamage = cursor.getDouble(cursor.getColumnIndex(MainTable.MainEntry.COLUMN_NAME_BASE_DAMAGE));
        this.baseSpeed = cursor.getDouble(cursor.getColumnIndex(MainTable.MainEntry.COLUMN_NAME_BASE_SPEED));
        this.currency = cursor.getInt(cursor.getColumnIndex(MainTable.MainEntry.COLUMN_NAME_PLAYER_CURRENCY));
        this.setSoundFX(cursor.getInt(cursor.getColumnIndex(MainTable.MainEntry.COLUMN_NAME_OPTION_FX)), dbHelper);
        this.setMusic(cursor.getInt(cursor.getColumnIndex(MainTable.MainEntry.COLUMN_NAME_OPTION_MUSIC)), dbHelper);
        this.volume = cursor.getDouble(cursor.getColumnIndex(MainTable.MainEntry.COLUMN_NAME_OPTION_SOUND));

        cursor.close();

    }

    private void createPlayer(DBHelper dbHelper) {
        SQLiteDatabase writter = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MainTable.MainEntry.COLUMN_NAME_BASE_LIVES, this.baseLives);
        values.put(MainTable.MainEntry.COLUMN_NAME_BASE_DAMAGE, this.baseDamage);
        values.put(MainTable.MainEntry.COLUMN_NAME_BASE_SPEED, this.baseSpeed);
        values.put(MainTable.MainEntry.COLUMN_NAME_PLAYER_CURRENCY, this.currency);
        values.put(MainTable.MainEntry.COLUMN_NAME_OPTION_FX, this.soundFX);
        values.put(MainTable.MainEntry.COLUMN_NAME_OPTION_MUSIC, this.music);
        values.put(MainTable.MainEntry.COLUMN_NAME_OPTION_SOUND, this.volume);
        values.put(MainTable.MainEntry.COLUMN_NAME_OPTION_TILT, 0);
        writter.insert(MainTable.MainEntry.TABLE_NAME, null, values);
    }

    public Integer getBaseLives() {
        return baseLives;
    }

    public boolean setBaseLives(Integer baseLives, DBHelper dbHelper) {
        this.baseLives = baseLives;
        return dbHelper.updateMainTable(dbHelper);
    }

    public Double getBaseDamage() {
        return baseDamage;
    }

    public boolean setBaseDamage(Double baseDamage, DBHelper dbHelper) {
        this.baseDamage = baseDamage;
        return dbHelper.updateMainTable(dbHelper);
    }

    public Double getBaseSpeed() {
        return baseSpeed;
    }

    public boolean setBaseSpeed(Double baseSpeed, DBHelper dbHelper) {
        this.baseSpeed = baseSpeed;
        return dbHelper.updateMainTable(dbHelper);
    }

    public Integer getCurrency() {
        return currency;
    }

    public boolean setCurrency(Integer currency, DBHelper dbHelper) {
        this.currency = currency;
        return dbHelper.updateMainTable(dbHelper);
    }

    public Boolean getSoundFX() {
        return soundFX;
    }

    public boolean setSoundFX(Integer soundFX, DBHelper dbHelper) {
        if (soundFX == 0)
            this.soundFX = false;
        else
            this.soundFX = true;
        return dbHelper.updateMainTable(dbHelper);
    }

    public Boolean getMusic() {
        return music;
    }

    public boolean setMusic(Integer music, DBHelper dbHelper) {
        if (music == 0)
            this.music = false;
        else
            this.music = true;
        return dbHelper.updateMainTable(dbHelper);
    }

    public Double getVolume() {
        return volume;
    }

    public boolean setVolume(Double volume, DBHelper dbHelper) {
        this.volume = volume;
        return dbHelper.updateMainTable(dbHelper);
    }
}

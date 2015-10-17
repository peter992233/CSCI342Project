package com.example.peter.csci342_groupproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.deleteDatabase(DBHelper.DATABASE_NAME);

        DBHelper dbHelper = new DBHelper(getApplicationContext());

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        /*****************************
         * CREATE TABLES FOR TESTING
         ****************************/

        ContentValues values1 = new ContentValues();
        values1.put(MainTable.MainEntry.COLUMN_NAME_BASE_LIVES, 1);
        values1.put(MainTable.MainEntry.COLUMN_NAME_BASE_DAMAGE, 1.5);
        values1.put(MainTable.MainEntry.COLUMN_NAME_BASE_SPEED, 2);
        values1.put(MainTable.MainEntry.COLUMN_NAME_OPTION_FX, 1);
        values1.put(MainTable.MainEntry.COLUMN_NAME_OPTION_MUSIC, 1);
        values1.put(MainTable.MainEntry.COLUMN_NAME_OPTION_SOUND, 50);
        values1.put(MainTable.MainEntry.COLUMN_NAME_OPTION_TILT, 0);
        db.insert(MainTable.MainEntry.TABLE_NAME, null, values1);

        ContentValues values2 = new ContentValues();
        values2.put(HighScoreTable.HighScoreEntry.COLUMN_NAME_UserName, "James");
        values2.put(HighScoreTable.HighScoreEntry.COLUMN_NAME_HighScore, 5670);
        db.insert(HighScoreTable.HighScoreEntry.TABLE_NAME, null, values2);


        /***********************************************************
         * READ TABLE FOR TESTING
         ************************************************************/

        SQLiteDatabase reader = dbHelper.getReadableDatabase();

        String[] projection1 = {
                MainTable.MainEntry.COLUMN_NAME_BASE_LIVES,
                MainTable.MainEntry.COLUMN_NAME_BASE_DAMAGE,
                MainTable.MainEntry.COLUMN_NAME_BASE_SPEED,
                MainTable.MainEntry.COLUMN_NAME_OPTION_FX,
                MainTable.MainEntry.COLUMN_NAME_OPTION_MUSIC,
                MainTable.MainEntry.COLUMN_NAME_OPTION_SOUND,
                MainTable.MainEntry.COLUMN_NAME_OPTION_TILT,
        };

        Cursor cursor = reader.query(
                MainTable.MainEntry.TABLE_NAME,
                projection1,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {

            int baseLives = cursor.getInt(cursor.getColumnIndex(MainTable.MainEntry.COLUMN_NAME_BASE_LIVES));
            double baseDamage = cursor.getDouble(cursor.getColumnIndex(MainTable.MainEntry.COLUMN_NAME_BASE_DAMAGE));
            double baseSpeed = cursor.getDouble(cursor.getColumnIndex(MainTable.MainEntry.COLUMN_NAME_BASE_SPEED));
            int optionFX = cursor.getInt(cursor.getColumnIndex(MainTable.MainEntry.COLUMN_NAME_OPTION_FX));
            int optionMusic = cursor.getInt(cursor.getColumnIndex(MainTable.MainEntry.COLUMN_NAME_OPTION_MUSIC));
            double optionSound = cursor.getDouble(cursor.getColumnIndex(MainTable.MainEntry.COLUMN_NAME_OPTION_SOUND));
            int optionTilt = cursor.getInt(cursor.getColumnIndex(MainTable.MainEntry.COLUMN_NAME_OPTION_TILT));

            Log.i("Lives:", String.valueOf(baseLives));
            Log.i("Damage:", String.valueOf(baseDamage));
            Log.i("Speed:", String.valueOf(baseSpeed));
            Log.i("FX:", String.valueOf(optionFX));
            Log.i("Music:", String.valueOf(optionMusic));
            Log.i("Sound:", String.valueOf(optionSound));
            Log.i("Tilt:", String.valueOf(optionTilt));
        }

        String[] projection2 = {
                HighScoreTable.HighScoreEntry.COLUMN_NAME_UserName,
                HighScoreTable.HighScoreEntry.COLUMN_NAME_HighScore,
        };

        cursor = reader.query(
                HighScoreTable.HighScoreEntry.TABLE_NAME,
                projection2,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {

            String userName = cursor.getString(cursor.getColumnIndex(HighScoreTable.HighScoreEntry.COLUMN_NAME_UserName));
            int highScore = cursor.getInt(cursor.getColumnIndex(HighScoreTable.HighScoreEntry.COLUMN_NAME_HighScore));

            Log.i("User Name:", userName);
            Log.i("High Score:", String.valueOf(highScore));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.example.peter.csci342_groupproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by jas899 on 22/10/2015.
 */

public class HighScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase reader = dbHelper.getReadableDatabase();

        String[] projection = {
                "_id",
                HighScoreTable.HighScoreEntry.COLUMN_NAME_UserName,
                HighScoreTable.HighScoreEntry.COLUMN_NAME_HighScore,
        };

        String sortOrder = HighScoreTable.HighScoreEntry.COLUMN_NAME_HighScore + " DESC";

        Cursor cursor = reader.query(
                HighScoreTable.HighScoreEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        if (cursor.getCount() > 0) {
            ListView scoresList = (ListView) findViewById(R.id.ScoresList);
            // Setup cursor adapter using cursor from last step
            HighScoreCursorAdaptor hsAdapter = new HighScoreCursorAdaptor(this, cursor);
            // Attach cursor adapter to the ListView
            scoresList.setAdapter(hsAdapter);
        } else {
            TextView message = (TextView) findViewById(R.id.Message);
            message.setText("No High Scores");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_high_scores, menu);
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

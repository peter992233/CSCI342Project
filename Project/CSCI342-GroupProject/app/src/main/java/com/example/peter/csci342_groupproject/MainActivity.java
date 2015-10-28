package com.example.peter.csci342_groupproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    GameView gmv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Useful for testing:
        //this.deleteDatabase(DBHelper.DATABASE_NAME);

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        GameData gd = GameData.getInstance();
        gd.populateFromDB(dbHelper);

        Log.d("START", "Starting Game");
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        gmv = new GameView(this,size.x,size.y);
        menuButtons();
    }


    public void menuButtons(){


        Button startGameButton = (Button) findViewById(R.id.startGameButton);
        Button UpgradeButton = (Button) findViewById(R.id.UpgradeButton);
        Button OptionsButton = (Button) findViewById(R.id.OptionsButton);
        Button HighScoreButton = (Button) findViewById(R.id.HighScoreButton);

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("START", "Starting Game");
                setContentView(gmv);
            }
        });
    }

    /** Called when the user clicks the High Scores button */
    public void viewHighScores(View view) {
        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Upgrades button */
    public void viewUpgrades(View view) {
        Intent intent = new Intent(this, UpgradesActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Upgrades button */
    public void viewOptions(View view) {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
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

    @Override
    protected void onPause() {
        super.onPause();
        gmv.Pause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        gmv.Resume();
    }

}

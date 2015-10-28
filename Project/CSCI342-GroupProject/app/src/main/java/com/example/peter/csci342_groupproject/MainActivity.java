package com.example.peter.csci342_groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Useful for testing:
        this.deleteDatabase(DBHelper.DATABASE_NAME);

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        GameData gd = GameData.getInstance();
        gd.populateFromDB(dbHelper);

        Log.d("START", "Starting Game");
    }


    public void startGame(View view) {
        Log.d("START", "Starting Game");
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }


    /**
     * Called when the user clicks the High Scores button
     */
    public void viewHighScores(View view) {
        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Upgrades button
     */
    public void viewUpgrades(View view) {
        Intent intent = new Intent(this, UpgradesActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Upgrades button
     */
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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}

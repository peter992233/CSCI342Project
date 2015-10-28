package com.example.peter.csci342_groupproject;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

public class GameActivity extends AppCompatActivity {


    Context context;

    GameView gmv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        GameData gd = GameData.getInstance();
        gd.populateFromDB(dbHelper);

        Log.d("START", "Starting Game");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        gmv = new GameView(this, size.x, size.y);

        setContentView(gmv);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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

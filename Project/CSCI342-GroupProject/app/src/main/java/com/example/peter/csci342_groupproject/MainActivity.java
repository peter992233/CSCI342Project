package com.example.peter.csci342_groupproject;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    private MediaPlayer mp = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Useful for testing:
        this.deleteDatabase(DBHelper.DATABASE_NAME);//delete me

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        GameData gd = GameData.getInstance();
        gd.populateFromDB(dbHelper);

        //gd.setVolume(0.1, dbHelper); //delete me

        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            AssetFileDescriptor afd = this.getResources().openRawResourceFd(R.raw.mainmenumusic);
            if (afd == null) return;
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.setOnErrorListener(this);
            mp.setOnPreparedListener(this);
            mp.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("START", "Starting Game");//delete me
    }

    @Override
    public void onPrepared(MediaPlayer play) {
        GameData gd = GameData.getInstance();
        play.setVolume(gd.getVolume().floatValue(), gd.getVolume().floatValue());
        play.setLooping(true);
        if (gd.getMusic()) {
            play.start();
        }
    }

    @Override
    public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
        return false;
    }


    public void startGame(View view) {
        Log.d("START", "Starting Game");//delete me
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        mp.pause();
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
        if (mp != null) {
            if (mp.isPlaying())
                mp.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mp != null) {
            GameData gd = GameData.getInstance();
            mp.setVolume(gd.getVolume().floatValue(), gd.getVolume().floatValue());
            mp.setLooping(true);
            if (gd.getMusic())
                mp.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }
}
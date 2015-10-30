package com.example.peter.csci342_groupproject;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private SoundPool sp = null;
    private boolean soundLoaded = false;
    private boolean playing = false;
    private int playID = 0;
    private int soundID = 0;
    GameData gd = GameData.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        gd.populateFromDB(dbHelper);

        sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundLoaded = true;
                GameData gd = GameData.getInstance();
                if (gd.getMusic()) {
                    playID = sp.play(soundID, gd.getVolume().floatValue(), gd.getVolume().floatValue(), 1, -1, 1f);
                    playing = true;
                }
            }
        });
        soundID = sp.load(this, R.raw.mainmenumusic, 1);
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        if (sp != null) {
            if (soundLoaded)
                sp.pause(soundID);
        }
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
        if (sp != null) {
            if (soundLoaded) {
                sp.pause(playID);
                playing = false;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((sp != null) && (soundLoaded) && (!playing) && (gd.getMusic())) {
            playing = true;
            if (playID == 0) {
                playID = sp.play(soundID, gd.getVolume().floatValue(), gd.getVolume().floatValue(), 1, -1, 1f);
            } else {
                sp.resume(playID);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sp != null) {
            sp.stop(playID);
            sp.release();
            sp = null;
        }
    }
}
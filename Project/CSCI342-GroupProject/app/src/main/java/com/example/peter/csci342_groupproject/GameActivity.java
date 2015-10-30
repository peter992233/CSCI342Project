package com.example.peter.csci342_groupproject;

import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

public class GameActivity extends AppCompatActivity {

    private SoundPool sp = null;
    private boolean soundLoaded = false;
    private boolean playing = false;
    private int playID = 0;
    private int soundID = 0;
    GameData gd = GameData.getInstance();

    GameView gmv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        gd.populateFromDB(dbHelper);

        Log.d("START", "Starting Game");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        gmv = new GameView(this, getFragmentManager(), size.x, size.y);

        setContentView(gmv);

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
        soundID = sp.load(this, R.raw.gamemusic, 1);
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
        gmv.Resume();
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

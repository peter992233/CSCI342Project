package com.example.peter.csci342_groupproject;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;

public class GameActivity extends AppCompatActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {


    Context context;

    GameView gmv;

    private MediaPlayer mp = null;

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
        gmv = new GameView(this, getFragmentManager(), size.x, size.y);

        setContentView(gmv);

        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            AssetFileDescriptor afd = this.getResources().openRawResourceFd(R.raw.gamemusic);
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
        if (mp != null) {
            if (mp.isPlaying())
                mp.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        gmv.Resume();
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

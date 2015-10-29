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
import android.widget.CheckBox;
import android.widget.SeekBar;

import java.io.IOException;

public class OptionsActivity extends AppCompatActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    MediaPlayer mp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        GameData gd = GameData.getInstance();
        CheckBox music = (CheckBox) this.findViewById(R.id.music);
        CheckBox soundFX = (CheckBox) this.findViewById(R.id.soundfx);
        SeekBar volume = (SeekBar) this.findViewById(R.id.volume);

        music.setChecked(gd.getMusic());
        soundFX.setChecked(gd.getSoundFX());
        Double vol = (gd.getVolume() * 100);
        volume.setProgress(vol.intValue());


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

        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                if (mp != null) {
                    Double vol = ((progress*1.0)/100);
                    mp.setVolume(vol.floatValue(), vol.floatValue());
                }

            }
        });
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

    public void saveOptions(View view) {
        GameData gd = GameData.getInstance();
        DBHelper dbHelper = new DBHelper(this);
        CheckBox music = (CheckBox) this.findViewById(R.id.music);
        CheckBox soundFX = (CheckBox) this.findViewById(R.id.soundfx);
        SeekBar volume = (SeekBar) this.findViewById(R.id.volume);

        if (music.isChecked()) {
            gd.setMusic(1, dbHelper);
            if (!mp.isPlaying())
                mp.start();
        } else {
            gd.setMusic(0, dbHelper);
            mp.pause();
        }

        if (soundFX.isChecked()) {
            gd.setSoundFX(1, dbHelper);
        } else {
            gd.setSoundFX(0, dbHelper);
        }
        Double vol = ((volume.getProgress()*1.0)/100);
        gd.setVolume(vol, dbHelper);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options, menu);
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

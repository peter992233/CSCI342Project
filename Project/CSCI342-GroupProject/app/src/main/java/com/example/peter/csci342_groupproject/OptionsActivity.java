package com.example.peter.csci342_groupproject;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;

public class OptionsActivity extends AppCompatActivity {

    private SoundPool sp = null;
    private boolean soundLoaded = false;
    private boolean playing = false;
    private int playID = 0;
    private int soundID = 0;
    GameData gd = GameData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        CheckBox music = (CheckBox) this.findViewById(R.id.music);
        CheckBox soundFX = (CheckBox) this.findViewById(R.id.soundfx);
        SeekBar volume = (SeekBar) this.findViewById(R.id.volume);

        music.setChecked(gd.getMusic());
        soundFX.setChecked(gd.getSoundFX());
        Double vol = (gd.getVolume() * 100);
        volume.setProgress(vol.intValue());

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
                if (sp != null) {
                    if (soundLoaded) {
                        Double vol = ((progress * 1.0) / 100);
                        sp.setVolume(soundID, vol.floatValue(), vol.floatValue());
                    }

                }

            }
        });
    }

    public void saveOptions(View view) {
        DBHelper dbHelper = new DBHelper(this);
        CheckBox music = (CheckBox) this.findViewById(R.id.music);
        CheckBox soundFX = (CheckBox) this.findViewById(R.id.soundfx);
        SeekBar volume = (SeekBar) this.findViewById(R.id.volume);

        if (music.isChecked()) {
            gd.setMusic(1, dbHelper);
            if (sp != null) {
                if (soundLoaded) {
                    if (!playing) {
                        playing = true;
                        if (playID == 0) {
                            playID = sp.play(soundID, gd.getVolume().floatValue(), gd.getVolume().floatValue(), 1, -1, 1f);
                        } else {
                            sp.resume(playID);
                        }
                    }
                }
            }
        } else {
            gd.setMusic(0, dbHelper);
            if (sp != null) {
                if (soundLoaded) {
                    sp.pause(playID);
                    playing = false;
                }
            }
        }

        if (soundFX.isChecked()) {
            gd.setSoundFX(1, dbHelper);
        } else {
            gd.setSoundFX(0, dbHelper);
        }
        Double vol = ((volume.getProgress() * 1.0) / 100);
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

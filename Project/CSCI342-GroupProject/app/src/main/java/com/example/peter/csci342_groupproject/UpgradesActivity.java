package com.example.peter.csci342_groupproject;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class UpgradesActivity extends AppCompatActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    MediaPlayer mp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrades);

        GameData gd = GameData.getInstance();

        TextView coins = (TextView) this.findViewById(R.id.Currency);
        String coinsText = "Coins: " + gd.getCurrency().toString();
        coins.setText(coinsText);
        Resources res = getResources();

        //set lives
        updateLives(gd);

        //set damage
        updateDamage(gd);

        //set speed
        updateSpeed(gd);

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

    private void updateLives(GameData gd) {
        ImageView life1 = (ImageView) this.findViewById(R.id.Lives1);
        ImageView life2 = (ImageView) this.findViewById(R.id.Lives2);
        ImageView life3 = (ImageView) this.findViewById(R.id.Lives3);
        ImageView life4 = (ImageView) this.findViewById(R.id.Lives4);
        ImageView life5 = (ImageView) this.findViewById(R.id.Lives5);

        switch (gd.getBaseLives()) {
            case 1:

                life1.setBackgroundResource(R.drawable.upgrade_true);
                break;
            case 2:
                life1.setBackgroundResource(R.drawable.upgrade_true);
                life2.setBackgroundResource(R.drawable.upgrade_true);
                break;
            case 3:
                life1.setBackgroundResource(R.drawable.upgrade_true);
                life2.setBackgroundResource(R.drawable.upgrade_true);
                life3.setBackgroundResource(R.drawable.upgrade_true);
                break;
            case 4:
                life1.setBackgroundResource(R.drawable.upgrade_true);
                life2.setBackgroundResource(R.drawable.upgrade_true);
                life3.setBackgroundResource(R.drawable.upgrade_true);
                life4.setBackgroundResource(R.drawable.upgrade_true);
                break;
            case 5:
                life1.setBackgroundResource(R.drawable.upgrade_true);
                life2.setBackgroundResource(R.drawable.upgrade_true);
                life3.setBackgroundResource(R.drawable.upgrade_true);
                life4.setBackgroundResource(R.drawable.upgrade_true);
                life5.setBackgroundResource(R.drawable.upgrade_true);
                Button buyLife = (Button) this.findViewById(R.id.BuyLife);
                buyLife.setEnabled(false);
                break;
        }
    }

    private void updateDamage(GameData gd) {
        ImageView damage1 = (ImageView) this.findViewById(R.id.Damage1);
        ImageView damage2 = (ImageView) this.findViewById(R.id.Damage2);
        ImageView damage3 = (ImageView) this.findViewById(R.id.Damage3);
        ImageView damage4 = (ImageView) this.findViewById(R.id.Damage4);
        ImageView damage5 = (ImageView) this.findViewById(R.id.Damage5);

        switch (gd.getBaseDamage().intValue()) {
            case 1:
                damage1.setBackgroundResource(R.drawable.upgrade_true);
                break;
            case 2:
                damage1.setBackgroundResource(R.drawable.upgrade_true);
                damage2.setBackgroundResource(R.drawable.upgrade_true);
                break;
            case 3:
                damage1.setBackgroundResource(R.drawable.upgrade_true);
                damage2.setBackgroundResource(R.drawable.upgrade_true);
                damage3.setBackgroundResource(R.drawable.upgrade_true);
                break;
            case 4:
                damage1.setBackgroundResource(R.drawable.upgrade_true);
                damage2.setBackgroundResource(R.drawable.upgrade_true);
                damage3.setBackgroundResource(R.drawable.upgrade_true);
                damage4.setBackgroundResource(R.drawable.upgrade_true);
                break;
            case 5:
                damage1.setBackgroundResource(R.drawable.upgrade_true);
                damage2.setBackgroundResource(R.drawable.upgrade_true);
                damage3.setBackgroundResource(R.drawable.upgrade_true);
                damage4.setBackgroundResource(R.drawable.upgrade_true);
                damage5.setBackgroundResource(R.drawable.upgrade_true);
                Button buyDamage = (Button) this.findViewById(R.id.BuyDamage);
                buyDamage.setEnabled(false);
                break;
        }
    }

    private void updateSpeed(GameData gd) {
        ImageView speed1 = (ImageView) this.findViewById(R.id.Speed1);
        ImageView speed2 = (ImageView) this.findViewById(R.id.Speed2);
        ImageView speed3 = (ImageView) this.findViewById(R.id.Speed3);
        ImageView speed4 = (ImageView) this.findViewById(R.id.Speed4);
        ImageView speed5 = (ImageView) this.findViewById(R.id.Speed5);

        switch (gd.getBaseSpeed().intValue()) {
            case 1:
                speed1.setBackgroundResource(R.drawable.upgrade_true);
                break;
            case 2:
                speed1.setBackgroundResource(R.drawable.upgrade_true);
                speed2.setBackgroundResource(R.drawable.upgrade_true);
                break;
            case 3:
                speed1.setBackgroundResource(R.drawable.upgrade_true);
                speed2.setBackgroundResource(R.drawable.upgrade_true);
                speed3.setBackgroundResource(R.drawable.upgrade_true);
                break;
            case 4:
                speed1.setBackgroundResource(R.drawable.upgrade_true);
                speed2.setBackgroundResource(R.drawable.upgrade_true);
                speed3.setBackgroundResource(R.drawable.upgrade_true);
                speed4.setBackgroundResource(R.drawable.upgrade_true);
                break;
            case 5:
                speed1.setBackgroundResource(R.drawable.upgrade_true);
                speed2.setBackgroundResource(R.drawable.upgrade_true);
                speed3.setBackgroundResource(R.drawable.upgrade_true);
                speed4.setBackgroundResource(R.drawable.upgrade_true);
                speed5.setBackgroundResource(R.drawable.upgrade_true);
                Button buySpeed = (Button) this.findViewById(R.id.BuySpeed);
                buySpeed.setEnabled(false);
                break;
        }
    }

    public void buyLife(View view) {
        GameData gd = GameData.getInstance();

        if (gd.getBaseLives() < 5) {
            DBHelper dbHelper = new DBHelper(getApplicationContext());

            if (gd.setBaseLives((gd.getBaseLives() + 1), dbHelper))
                updateLives(gd);
        }
    }

    public void buyDamage(View view) {
        GameData gd = GameData.getInstance();

        if (gd.getBaseDamage() < 5) {
            DBHelper dbHelper = new DBHelper(getApplicationContext());

            if (gd.setBaseDamage((gd.getBaseDamage() + 1), dbHelper))
                updateDamage(gd);
        }
    }

    public void buySpeed(View view) {
        GameData gd = GameData.getInstance();

        if (gd.getBaseSpeed() < 5) {
            DBHelper dbHelper = new DBHelper(getApplicationContext());

            if (gd.setBaseSpeed((gd.getBaseSpeed() + 1), dbHelper))
                updateSpeed(gd);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upgrades, menu);
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

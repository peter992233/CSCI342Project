package com.example.peter.csci342_groupproject;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UpgradesActivity extends AppCompatActivity {

    private SoundPool sp = null;
    private boolean soundLoaded = false;
    private boolean playing = false;
    private int playID = 0;
    private int soundID = 0;
    GameData gd = GameData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrades);

        showCoins(gd.getCurrency());

        //set lives
        updateLives();

        //set damage
        updateDamage();

        //set speed
        updateSpeed();

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

    private void showCoins(int coins) {
        TextView coinText = (TextView) this.findViewById(R.id.Currency);
        String coinsText = "Coins: " + coins;
        coinText.setText(coinsText);
    }

    private void updateLives() {
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

    private void updateDamage() {
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

    private void updateSpeed() {
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

        int coins = gd.getCurrency();
        if (coins >= 500){
            if (gd.getBaseLives() < 5) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());

                if (gd.setBaseLives((gd.getBaseLives() + 1), dbHelper)) {
                    updateLives();
                    gd.setCurrency((coins - 500), dbHelper);
                    showCoins((coins-500));
                }
            }
        }
    }

    public void buyDamage(View view) {

        int coins = gd.getCurrency();
        if (coins >= 500) {
            if (gd.getBaseDamage() < 5) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());

                if (gd.setBaseDamage((gd.getBaseDamage() + 1), dbHelper)) {
                    updateDamage();
                    gd.setCurrency((coins - 500), dbHelper);
                    showCoins((coins - 500));
                }
            }
        }
    }

    public void buySpeed(View view) {

        int coins = gd.getCurrency();
        if (coins >= 500) {
            if (gd.getBaseSpeed() < 5) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());

                if (gd.setBaseSpeed((gd.getBaseSpeed() + 1), dbHelper)) {
                    updateSpeed();
                    gd.setCurrency((coins - 500), dbHelper);
                    showCoins((coins - 500));
                }
            }
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

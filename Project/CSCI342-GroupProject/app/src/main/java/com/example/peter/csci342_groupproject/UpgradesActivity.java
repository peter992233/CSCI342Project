package com.example.peter.csci342_groupproject;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class UpgradesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrades);

        GameData gd = GameData.getInstance();

        //set coins up
        TextView coins = (TextView) this.findViewById(R.id.Currency);
        String coinsText = "Coins: " + gd.getCurrency().toString();
        coins.setText(coinsText);
        Resources res = getResources();

        //set lives
        updateLives(gd);

        //set damage
        updateDamage(gd);


    }

    private void updateLives(GameData gd) {
        ImageView life1 = (ImageView) this.findViewById(R.id.Lives1);
        ImageView life2 = (ImageView) this.findViewById(R.id.Lives2);
        ImageView life3 = (ImageView) this.findViewById(R.id.Lives3);
        ImageView life4 = (ImageView) this.findViewById(R.id.Lives4);
        ImageView life5 = (ImageView) this.findViewById(R.id.Lives5);

        switch (gd.getBaseLives().intValue()) {
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
                break;
        }
    }

    private void updateDamage(GameData gd) {
        ImageView damage1 = (ImageView) this.findViewById(R.id.Damage1);
        ImageView damage2 = (ImageView) this.findViewById(R.id.Damage2);
        ImageView damage3 = (ImageView) this.findViewById(R.id.Damage3);
        ImageView damage4 = (ImageView) this.findViewById(R.id.Damage4);
        ImageView damage5 = (ImageView) this.findViewById(R.id.Damage5);

        switch (gd.getBaseLives().intValue()) {
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
                damage5.setBackgroundResource(R.drawable.upgrade_true);
                break;
            case 5:
                damage1.setBackgroundResource(R.drawable.upgrade_true);
                damage2.setBackgroundResource(R.drawable.upgrade_true);
                damage3.setBackgroundResource(R.drawable.upgrade_true);
                damage4.setBackgroundResource(R.drawable.upgrade_true);
                damage5.setBackgroundResource(R.drawable.upgrade_true);
                break;
        }
    }
/*
    private void updateSpeed(GameData gd) {
        ImageView speed1 = (ImageView) this.findViewById(R.id.Damage1);
        ImageView speed2 = (ImageView) this.findViewById(R.id.Damage2);
        ImageView damage3 = (ImageView) this.findViewById(R.id.Damage3);
        ImageView damage4 = (ImageView) this.findViewById(R.id.Damage4);
        ImageView damage5 = (ImageView) this.findViewById(R.id.Damage5);

        switch (gd.getBaseLives().intValue()) {
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
                damage5.setBackgroundResource(R.drawable.upgrade_true);
                break;
            case 5:
                damage1.setBackgroundResource(R.drawable.upgrade_true);
                damage2.setBackgroundResource(R.drawable.upgrade_true);
                damage3.setBackgroundResource(R.drawable.upgrade_true);
                damage4.setBackgroundResource(R.drawable.upgrade_true);
                damage5.setBackgroundResource(R.drawable.upgrade_true);
                break;
        }
    }
    */

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
}

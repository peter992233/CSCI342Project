package com.example.peter.csci342_groupproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

/**
 * Created by pb864 on 17/10/15.
 */
public class Projectile {

    private float x;
    private float y;

    private RectF rect;
    private Bitmap bmp = null;

    public final int UP = 0;
    public final int DOWN = 1;
    public final int LEFT = 2;
    public final int RIGHT = 3;

    private int width;
    private int height;

    int heading = -1;
    float speed = 350;

    public boolean isActive;
    boolean isEnemy = false;

    int weaponType;
    int weaponLevel;

    public Projectile(Context context, int screenY, int screenX, int wType, int wLevel) {

        width = screenX / 90;
        height = screenY / 30;

        isActive = false;
        rect = new RectF();

        weaponType = wType;
        weaponLevel = wLevel;

        if(weaponType == 0) {
            //Create the Bitmap
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_bullet);
            bmp = Bitmap.createScaledBitmap(bmp, (int) width, (int) height, false);
        }
        if(weaponType == 1) {
            //Create the Bitmap
            width = screenX/30;
            height = screenY/30;
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_orb);
            bmp = Bitmap.createScaledBitmap(bmp, (int) width, (int) height, false);
        }
        if(weaponType == 2) {
            for(int i = 0; i < 3; i++) {
                width = (screenX/40) * (weaponLevel + 1);
            }
            height = screenY/25;
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_lazer);
            bmp = Bitmap.createScaledBitmap(bmp, (int) width, (int) height, false);
        }
    }

    public Projectile(Context context, int screenY, int screenX, boolean enemy, int bulletType) {

        width = screenX / 90;
        height = screenY / 30;
        isActive = false;
        rect = new RectF();
        isEnemy = enemy;

        if (enemy == true) {
            heading = DOWN;
            //Create the Bitmap
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_bullet);
            bmp = Bitmap.createScaledBitmap(bmp, (int) width, (int) height, false);
        } else {
            heading = UP;
            //Create the Bitmap
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_bullet);
            bmp = Bitmap.createScaledBitmap(bmp, (int) width, (int) height, false);
        }

    }


    public RectF getRect() {
        return rect;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public boolean getStatus() {
        return isActive;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getHeight() {
        return height;
    }

    public void setInactive() {
        isActive = false;
    }

    public float getImpactPointY() {
        //If the projectile is heading down we want to get its next impact point
        //If it is standing still we need to get its current location
        if (heading == DOWN) {
            return y + height;
        } else if (heading == UP || heading == LEFT || heading == RIGHT) {
            return y - height;
        } else {
            return y;
        }
    }

    public boolean shoot(float startX, float startY, int direction) {

        if (!isActive) {
            x = startX;
            y = startY;
            heading = direction;
            isActive = true;
            return true;
        }
        return false;
    }

    public void update(long fps) {

        if (heading == UP || heading == LEFT || heading == RIGHT) {
            y = y - speed / fps;
        }
        else {
            y = y + speed / fps;
        }

        if(heading == LEFT) {
            x = x - speed/fps;
        }
        if(heading == RIGHT) {
            x = x + speed/fps;
        }

        //update the rect
        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;
    }
}

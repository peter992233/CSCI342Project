package com.example.peter.csci342_groupproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

/**
 * Created by pudgimusprime on 28/10/2015.
 */
public class Powerup {
    private float x;
    private float y;

    private RectF rect;
    private Bitmap bmp = null;

    private int width;
    private int height;

    float speed = 100;

    public boolean isActive;

    int powerupType;

    public Powerup(Context context, int screenY, int screenX, int pType) {
        width = screenX / 20;
        height = screenY / 20;

        isActive = false;
        rect = new RectF();

        powerupType = pType;

        if(pType == 0) {
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.powerup_bullet);
            bmp = Bitmap.createScaledBitmap(bmp, (int) width, (int) height, false);
        }
        else if(pType == 1) {
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.powerup_orb);
            bmp = Bitmap.createScaledBitmap(bmp, (int) width, (int) height, false);
        }
        else if(pType == 2) {
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.powerup_lazer);
            bmp = Bitmap.createScaledBitmap(bmp, (int) width, (int) height, false);
        }
    }

    public int getPowerupType() {
        return powerupType;
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

    public boolean spawn(float startX, float startY) {
        if (!isActive) {
            x = startX;
            y = startY;
            isActive = true;
            return true;
        }
        return false;
    }

    public void update(long fps) {
        y = y + speed / fps;

        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;
    }
}

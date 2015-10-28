package com.example.peter.csci342_groupproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

/**
 * Created by pudgimusprime on 27/10/2015.
 */
public class Coin {
    private float x;
    private float y;

    private RectF rect;
    private Bitmap bmp = null;

    private int width;
    private int height;

    float speed = 100;

    public boolean isActive;

    public Coin(Context context, int screenY, int screenX) {
        width = screenX / 25;
        height = screenY / 25;

        isActive = false;
        rect = new RectF();

        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin);
        bmp = Bitmap.createScaledBitmap(bmp, (int) width, (int) height, false);
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

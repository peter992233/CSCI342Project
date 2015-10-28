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

    public Powerup(Context context, int screenY, int screenX) {
        width = screenX/25;
        height = screenY/25;

        isActive = false;
        rect = new RectF();

        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.powerup_bullet);
        bmp = Bitmap.createScaledBitmap(bmp, (int) width, (int) height, false);
    }


}

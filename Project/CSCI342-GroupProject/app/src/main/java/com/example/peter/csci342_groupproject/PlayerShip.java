package com.example.peter.csci342_groupproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by pb864 on 17/10/15.
 */
public class PlayerShip {


    //Image display Details
    private RectF rect;
    private Bitmap bmp = null;
    private float height, width;

    //Far left (X) and Top (Y)
    private float x , y ;

    //Movement Speed of the Ship
    private float shipSpeed;

    //Directional options for the ship to move
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    private int shipMoving = STOPPED;



    public PlayerShip(Context context, int screenX, int screenY){

        rect = new RectF();
        width = screenX / 15;
        height = screenY / 8;

        x = screenX / 2;
        y = screenY - height*2;

        //Create the Bitmap
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        bmp = Bitmap.createScaledBitmap(bmp, (int) width, (int) height, false);


        //How fast is the ship in pixels Per Second
        shipSpeed = 475;

    }

    public RectF getRect() {
        return rect;
    }

    public Bitmap getBmp() { return bmp; }

    public float getX() {
        return x;
    }

    public float getWidth() {
        return width;
    }

    public void setMovementState(int state){
        shipMoving = state;
    }

    public float getHeight(){
        return height;
    }

    public void update(long fps){

        //Move the Ship X&Y coords based on direction and Speed
        if(shipMoving == LEFT){
            x = x-shipSpeed/fps;
        }
        if(shipMoving == RIGHT){
            x = x+shipSpeed/fps;
        }

        //Update Rect
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x+width;



    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

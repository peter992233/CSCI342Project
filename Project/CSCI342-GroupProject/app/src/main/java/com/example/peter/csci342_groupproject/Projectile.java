package com.example.peter.csci342_groupproject;

import android.graphics.RectF;

/**
 * Created by pb864 on 17/10/15.
 */
public class Projectile {

    private float x;
    private float y;

    private RectF rect;

    public final int UP = 0;
    public final int DOWN = 1;

    private int width = 1;
    private int height;

    int heading = -1;
    float speed = 350;

    public boolean isActive;


    public Projectile(int screenY){
        height = screenY /20;
        isActive = false;
        rect = new RectF();

    }


    public RectF getRect(){
        return rect;
    }

    public boolean getStatus(){
        return isActive;
    }

    public void setInactive(){
        isActive = false;
    }

    public float getImpactPointY(){

        //If the projectile is heading down we want to get its next impact point
        //If it is standing still we need to get its current location
        if(heading == DOWN){
            return y+height;
        }else{
            return y;
        }
    }

    public boolean shoot(float startX, float startY, int direction){
        if(!isActive){
            x = startX;
            y = startY;
            heading = direction;
            isActive = true;
            return true;
        }
        return false;
    }

    public void update(long fps){

        if(heading == UP){
            y = y-speed/fps;
        }else{
            y=y+speed /fps;
        }


        //update the rect
        rect.left = x;
        rect.right = x+width;
        rect.top = y;
        rect.bottom = y+height;


    }




}

package com.example.peter.csci342_groupproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by pb864 on 17/10/15.
 */
public class GameView extends SurfaceView implements Runnable {



    Context context;

    //This is the game thread
    private Thread gameThread = null;

    //Locks the surface on which to draw graphics
    private SurfaceHolder ourHolder;

    //Boolean to set/unset whether the game is running
    private volatile boolean playing;

    //Set the default mode to be paused at the start
    private boolean paused = true;

    //Canvas and paint object for drawing
    private Canvas canvas;
    private Paint paint;

    //FPS Rate and the Variable tacking how many frames have passed
    private long fps;
    private long timeThisFrame;

    //Screen Dimensions
    private int screenX, screenY;

    private PlayerShip pShip;


    private Projectile projectile;
    private ArrayList<Projectile> playerBullets = new ArrayList<Projectile>();
    private int maxPlayerProjectiles = 10; //Maximum number of projectiles the player can shoot
    private boolean shotTap = false; //Has the last tap fired a shot
    /*
    private Projectile[] enemyBullets = new Projectile[200];
    private int NextProjectile;
    private int MaxProjectile = 10;
    */


    EnemyShip[] enemies = new EnemyShip[60];
    int numEnemies = 0;

    int score = 0;
    private int lives = 0;



    public GameView(Context context,int x, int y) {
        super(context);

        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;


        try{

            //Load Sound Files

        }catch(Exception e){
            Log.d("CERROR", "GameView Contructor");
            e.printStackTrace();

        }


        prepareLevel();
    }



    private void prepareLevel(){

        //Init game Objects

        //Make a Player Ship
        pShip = new PlayerShip(context,screenX,screenY);

        //Prepare Bullets

        projectile = new Projectile(screenY);

        //Init bullet Array
        //Build Enemy array

    }


    @Override
    public void run() {


        while(playing){
            long startFrameTime = System.currentTimeMillis();

            //If not pause the game should update
            if(!paused){
                update();
            }

            //Draw the Frame
            draw();


            //Update the elapsed time
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if(timeThisFrame >= 1){
                fps = 1000/timeThisFrame;
            }


        }



    }


    private void update(){

        boolean bumped = false;
        boolean lost = false;


        //Move the player ship
        pShip.update(fps);

        //Update the Enemies

        //Update the Bullets
        for(Projectile p : playerBullets ) {
            if (p.getStatus()) {
                p.update(fps);
            }
        }

        //Check Collision

        if(lost){
            prepareLevel();
        }

        //Update player bullets
        //Check the player bullet collisions (top & bottom of Screen + enemies)
        //Check the enemies bullet collisions (top & bottom of Screen + player)

    }


    private void draw(){

        if(ourHolder.getSurface().isValid()){

            canvas = ourHolder.lockCanvas();

            canvas.drawColor(Color.argb(255,26,128,182));
            paint.setColor(Color.argb(255,255,255,255));

            //Draw the Player Ship
            canvas.drawBitmap(pShip.getBmp(), pShip.getX(),screenY-(pShip.getHeight()*2),paint);

            //Draw the Enemies

            //Draw the Active Bullets

            for(Projectile p : playerBullets ) {
                if(p.getStatus()){
                    canvas.drawRect(p.getRect(),paint);
                }
            }

            //Draw Score & Lives

            paint.setColor(Color.argb(255,249,129,0));

            paint.setTextSize(40);
            canvas.drawText("Score: " + score + "\t\t\tLives: " + lives, 10 , 50 , paint);

            ourHolder.unlockCanvasAndPost(canvas);



        }

    }


    public void Pause(){

        playing = false;
        try{
            gameThread.join();
        }catch(Exception e){
            Log.d("CERROR","Error Pause()");
            e.printStackTrace();
        }

    }



    public void Resume(){
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }



    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){


            switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){


                case MotionEvent.ACTION_DOWN:{
                    paused = false;
                    pShip.setMovementState(pShip.STOPPED);
                    //If the touch is in the top 7/8ths of the screen it is counted as movement
                    if (motionEvent.getY() > screenY - screenY / 4) {
                        if (motionEvent.getX() > screenX / 2) {
                            pShip.setMovementState(pShip.RIGHT);
                        } else {
                            pShip.setMovementState(pShip.LEFT);
                        }
                    }

                    if(shotTap == false) {
                        if (motionEvent.getY() < screenY - screenY / 4) {
                            if (projectile.shoot(pShip.getX() + pShip.getWidth() / 2, screenY, projectile.UP));

                            shotTap = true;
                            playerBullets.add(projectile);
                            projectile = new Projectile(screenY); // reset projectile
                        }
                    }


                    break;
                }

                case MotionEvent.ACTION_POINTER_DOWN:{
                    int index = motionEvent.getActionIndex();
                    int xPos = (int) MotionEventCompat.getX(motionEvent, index);
                    int yPos = (int) MotionEventCompat.getY(motionEvent, index);


                    paused = false;
                    pShip.setMovementState(pShip.STOPPED);
                    //If the touch is in the top 7/8ths of the screen it is counted as movement
                    if (yPos > screenY - screenY / 4) {
                        if (xPos > screenX / 2) {
                            pShip.setMovementState(pShip.RIGHT);
                        } else {
                            pShip.setMovementState(pShip.LEFT);
                        }
                    }

                    if(shotTap == false) {
                        if (motionEvent.getY() < screenY - screenY / 4) {
                            if (projectile.shoot(pShip.getX() + pShip.getWidth() / 2, screenY, projectile.UP));

                            shotTap = true;
                            playerBullets.add(projectile);
                            projectile = new Projectile(screenY); // reset projectile
                        }
                    }
                    break;
                }


                case MotionEvent.ACTION_UP:{
                    paused = false;
                    pShip.setMovementState(pShip.STOPPED);
                    shotTap = false;
                    break;
                }


        }
        return true;
    }

}

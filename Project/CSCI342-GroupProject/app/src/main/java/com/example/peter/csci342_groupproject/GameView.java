package com.example.peter.csci342_groupproject;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Random;

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
    private ArrayList<Projectile> enemyBullets = new ArrayList<Projectile>();

    private Coin coin;
    private ArrayList<Coin> coins = new ArrayList<Coin>();

    private long nextShot = 250; //When is the next shot
    private long nextEnemy = 2500;//When to display the next enemy


    final AnimationDrawable animDraw = createAnimationDrawable();

    ArrayList<EnemyShip> EnemyList= new ArrayList<EnemyShip>();
    int maxEnemies = 40;

    int score = 0;
    private int lives = 0;

    ImageView backGround;
    int currBgFrame = 0;
    int GameLevel = 0;
    boolean waitRestart = false;

    public GameView(Context context,int x, int y) {
        super(context);

        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        //initial Lives
        lives = 3;

        try{

            //Load Sound Files

        }catch(Exception e){
            Log.d("CERROR", "GameView Contructor");
            e.printStackTrace();

        }

        try{
            backGround = new ImageView(context);
            backGround.setImageDrawable(animDraw);

        }catch(Exception e) {
            e.printStackTrace();
        }


        prepareLevel(GameLevel);
    }


    private AnimationDrawable createAnimationDrawable(){

        AnimationDrawable newAnim = new AnimationDrawable();
        Resources res = getResources();
        newAnim.addFrame(res.getDrawable(R.drawable.background_1),200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_2),200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_3),200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_4),200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_5),200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_6),200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_7),200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_8),200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_9), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_10), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_11), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_12), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_13), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_14), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_15), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_16), 200);


        newAnim.setOneShot(false);
        return newAnim;
    }


    private void prepareLevel(int Level){

        //Init game Objects
        GameLevel++;
        Log.d("LEVELSTART","" + GameLevel);


        respawnPlayer();
        respawnEnemies();
        playing = true;

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



            //Update the Events handler variables by the elapsed time
            nextShot -= timeThisFrame;
            nextEnemy -= timeThisFrame;

        }


    }


    public void respawnEnemies(){

        //Build Enemy array
        for(int i = 0; i < GameLevel*maxEnemies; i++){

            EnemyShip newEnemy = new EnemyShip(context,screenX,screenY);

            //Start with 3 Enemies on screen
            newEnemy.setIsVisible(false);

            //This adds the enemy to the List
            EnemyList.add(newEnemy);
        }

    }

    public void respawnPlayer(){

        //Make a Player Ship
        pShip = new PlayerShip(context,screenX,screenY);

        //Prepare Bullets

        projectile = new Projectile(context, screenY, screenX);

    }


    private void update() {

        boolean lost = false;

        if (paused != true) {

            if (nextEnemy < 0) {
                Random nextEnemyRand = new Random();
                //Spawn a new enemy every 0.5 of a second to 3.5 seconds
                nextEnemy = nextEnemyRand.nextInt(3500) - 500;

                if (EnemyList.size() > 0) {
                    EnemyShip es = EnemyList.get(nextEnemyRand.nextInt(EnemyList.size()));

                    //Prevent enemy collisions
                    for (int i = 0; i < EnemyList.size(); i++) {
                        if (EnemyList.get(i).isVisible()) {
                            if (RectF.intersects(es.getRect(), EnemyList.get(i).getRect())) {
                                nextEnemy = 0; //Try again
                            }
                        }
                    }

                    if (nextEnemy != 0) {
                        es.setIsVisible(true);
                    }


                } else {
                    GameLevel++;
                    Log.d("LEVELUP", "YOU DID IT!");
                }
            }


            //Move the player ship
            pShip.update(fps);

            //Update the Enemies
            for (int i = 0; i < EnemyList.size(); i++) {
                EnemyShip e = EnemyList.get(i);

                if (e.isVisible()) {
                    e.update(fps);

                    if (e.getY() > 0) {
                        if (e.shouldShoot(pShip.getX(), pShip.getWidth())) {
                            Projectile p = new Projectile(context, screenY, screenX, true, 0);
                            p.shoot(e.getX() + e.getLength() / 2, e.getRect().bottom, projectile.DOWN);
                            enemyBullets.add(p);
                        }
                    }

                    //Check if time to remove enemy objects
                    if (e.getY() < -e.getHeight() * 2 || e.getY() > screenY + e.getHeight() * 2) {
                        EnemyList.remove(e);

                        //Decrease score if enemy gets past player
                        if(score != 0) {
                            score -= 100;
                        }
                    }


                    if (RectF.intersects(e.getRect(), pShip.getRect())) {
                        lives--;
                        if (lives == 0) {
                            gameOver();
                        }
                        EnemyList.remove(i);
                        Log.d("Crash", "Hope You Have Insurance Buddy");
                    }


                }
            }


            //Update the Bullets

            for (int i = 0; i < playerBullets.size(); i++) {
                Projectile p = playerBullets.get(i);
                if (p.getStatus()) {
                    p.update(fps);
                    //Check if time to remove bullet objects
                    if (p.getImpactPointY() < -p.getHeight() * 2 || p.getImpactPointY() > screenY + p.getHeight() * 2) {
                        p.setInactive();
                        playerBullets.remove(p);
                    }


                    for (int j = 0; j < EnemyList.size(); j++) {
                        EnemyShip e = EnemyList.get(j);
                        if (e.isVisible()) {
                            if (RectF.intersects(p.getRect(), e.getRect())) {
                                Log.d("BOOM", "Enemy Ship Killed");
                                e.setIsVisible(false);
                                EnemyList.remove(e);
                                p.setInactive();
                                playerBullets.remove(p);
                                score += 100;
                            }
                        }
                    }
                }
            }


            for (int i = 0; i < enemyBullets.size(); i++) {
                Projectile p = enemyBullets.get(i);
                if (p.getStatus()) {
                    p.update(fps);
                }

                if (p.getImpactPointY() < -p.getHeight() * 2 || p.getImpactPointY() > screenY + p.getHeight() * 2) {
                    p.setInactive();
                    enemyBullets.remove(p);
                }

                if (RectF.intersects(p.getRect(), pShip.getRect())) {
                    Log.d("POW", "You got Hit");
                    p.setInactive();
                    enemyBullets.remove(p);
                    lives--;
                    if (lives <= 0) {
                        gameOver();
                    }
                }


            }

            if (lives <= 0) {
                gameOver();
            }


            currBgFrame++;
            if (currBgFrame > animDraw.getNumberOfFrames() - 1) {
                currBgFrame = 0;
            }

        }
    }





    private void draw(){


        if(waitRestart == false) {
            if (ourHolder.getSurface().isValid()) {

                canvas = ourHolder.lockCanvas();


                Drawable d = animDraw.getFrame(currBgFrame);
                d.setBounds(0, 0, screenX, screenY);
                d.draw(canvas);

                paint.setColor(Color.argb(255, 255, 255, 255));

                //Draw the Active Bullets

                for (int i = 0; i < playerBullets.size(); i++) {
                    Projectile p = playerBullets.get(i);
                    if (p.getStatus()) {
                        canvas.drawBitmap(p.getBmp(), p.getX(), p.getY(), paint);
                    }
                }

                for (int i = 0; i < enemyBullets.size(); i++) {
                    Projectile p = enemyBullets.get(i);
                    if (p.getStatus()) {
                        canvas.drawBitmap(p.getBmp(), p.getX(), p.getY(), paint);
                    }
                }


                //Draw the Player Ship
                canvas.drawBitmap(pShip.getBmp(), pShip.getX(), screenY - (pShip.getHeight() * 2), paint);

                //Draw the Enemies

                for (int i = 0; i < EnemyList.size(); i++) {
                    EnemyShip e = EnemyList.get(i);
                    if (e.isVisible() == true) {
                        canvas.drawBitmap(e.getEnemyBMP(), e.getX(), e.getY(), paint);

                    }
                }


                //Draw Score & Lives

                paint.setColor(Color.argb(255, 249, 129, 0));
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(40);
                canvas.drawText("Score: " + score + "\t\t\tLives: " + lives, 10, 50, paint);


                ourHolder.unlockCanvasAndPost(canvas);


            }
        }else{
            if(ourHolder.getSurface().isValid()){

                canvas = ourHolder.lockCanvas();


                Drawable d = animDraw.getFrame(0);
                d.setBounds(0, 0, screenX, screenY);
                d.draw(canvas);

                paint.setColor(Color.argb(255, 255, 255, 255));

                //Draw the Player Ship
                canvas.drawBitmap(pShip.getBmp(), pShip.getX(), screenY - (pShip.getHeight() * 2), paint);


                //Draw Score & Lives
                paint.setColor(Color.WHITE);

                paint.setTextSize(50);

                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over, You Scored: " + score, canvas.getWidth() / 2,
                        (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)), paint);

                ourHolder.unlockCanvasAndPost(canvas);



            }
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

                //You are not waiting to restart
                if(waitRestart != false){
                    waitRestart = false;
                    score = 0;
                }

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

                if(nextShot < 0) {
                    projectile = new Projectile(context, screenY, screenX); // reset projectile
                    if (motionEvent.getY() < screenY - screenY / 4) {
                        projectile.shoot(pShip.getX() + pShip.getWidth() / 2, pShip.getRect().top, projectile.UP);
                        playerBullets.add(projectile);
                    }
                    nextShot = 250;
                }


                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN:{
                int index = motionEvent.getActionIndex();
                int xPos = (int) MotionEventCompat.getX(motionEvent, index);
                int yPos = (int) MotionEventCompat.getY(motionEvent, index);


                paused = false;
                //If the touch is in the top 7/8ths of the screen it is counted as movement
                if (yPos > screenY - screenY / 4) {
                    if (xPos > screenX / 2) {
                        pShip.setMovementState(pShip.RIGHT);
                    } else {
                        pShip.setMovementState(pShip.LEFT);
                    }
                }


                if(nextShot < 0) {
                    if (yPos < screenY - screenY / 4) {
                        projectile = new Projectile(context, screenY, screenX); // reset projectile
                        if (projectile.shoot(pShip.getX() + pShip.getWidth()/2, pShip.getRect().top, projectile.UP)) {
                            playerBullets.add(projectile);
                        }
                    }
                    nextShot = 250;

                }
                break;
            }



            case MotionEvent.ACTION_UP:{
                paused = false;
                pShip.setMovementState(pShip.STOPPED);
                break;
            }


        }
        return true;
    }



    public void gameOver(){


        //Display highscore



        //Ask restart



        try{
            //Do Restart
            playing = false;
            paused = true;
            EnemyList.clear();
            enemyBullets.clear();
            playerBullets.clear();
            nextShot = 0; //When is the next shot
            nextEnemy = 5000;//When to display the next enemy

            lives = 3;
            currBgFrame = 0;
            GameLevel = 0;
            waitRestart = true;



        }catch(Exception e){

        }finally{
            prepareLevel(0);
        }
    }





}

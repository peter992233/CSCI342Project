package com.example.peter.csci342_groupproject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by pb864 on 17/10/15.
 */
public class GameView extends SurfaceView implements Runnable {


    Context context;

    //sound effects music player
    private MediaPlayer mp = null;

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

    private Powerup powerup;
    private ArrayList<Powerup> powerups = new ArrayList<Powerup>();

    private long nextShot = 250; //When is the next shot
    private long nextEnemy = 2500;//When to display the next enemy


    final AnimationDrawable animDraw = createAnimationDrawable();

    final ArrayList<AnimationDrawable> explDraws = new ArrayList<AnimationDrawable>();
    ArrayList<Integer> explX = new ArrayList<Integer>();
    ArrayList<Integer> explY = new ArrayList<Integer>();
    ArrayList<Integer> currExFrames = new ArrayList<Integer>();

    ArrayList<EnemyShip> EnemyList = new ArrayList<EnemyShip>();
    int maxEnemies = 40;

    int score = 0;
    private int lives = 0;
    int currency = 0;

    ImageView backGround;
    int currBgFrame = 0;


    int GameLevel = 0;
    boolean waitRestart = false;

    int pWeaponType = 0;
    int pWeaponLevel = 0;
    int pWeaponDamage = 0;

    GameData gd = GameData.getInstance();

    public GameView(Context context, int x, int y) {
        super(context);

        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        //initial Lives
        lives = gd.getBaseLives() + 3;
        currency = gd.getCurrency();
        pWeaponDamage = gd.getBaseDamage().intValue();

        try {

            //Load Sound Files

        } catch (Exception e) {
            Log.d("CERROR", "GameView Contructor");
            e.printStackTrace();

        }

        try {
            backGround = new ImageView(context);
            backGround.setImageDrawable(animDraw);

        } catch (Exception e) {
            e.printStackTrace();
        }

        prepareLevel();
    }


    private AnimationDrawable createAnimationDrawable() {

        AnimationDrawable newAnim = new AnimationDrawable();
        Resources res = getResources();
        newAnim.addFrame(res.getDrawable(R.drawable.background_1), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_2), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_3), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_4), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_5), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_6), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_7), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.background_8), 200);
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

    private AnimationDrawable createExplosionDrawable() {

        AnimationDrawable newAnim = new AnimationDrawable();
        Resources res = getResources();
        newAnim.addFrame(res.getDrawable(R.drawable.explosion_1), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.explosion_2), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.explosion_3), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.explosion_4), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.explosion_5), 200);
        newAnim.addFrame(res.getDrawable(R.drawable.explosion_6), 200);

        newAnim.setOneShot(true);
        return newAnim;
    }


    private void prepareLevel() {

        //Init game Objects
        GameLevel++;
        Log.d("LEVELSTART", "" + GameLevel);


        respawnPlayer();
        respawnEnemies();
        playing = true;

    }


    @Override
    public void run() {


        while (playing) {


            long startFrameTime = System.currentTimeMillis();

            //If not pause the game should update
            if (!paused) {
                update();
            }

            //Draw the Frame
            draw();


            //Update the elapsed time
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

            //Update the Events handler variables by the elapsed time
            nextShot -= timeThisFrame;
            nextEnemy -= timeThisFrame;
        }
    }


    public void respawnEnemies() {

        //Build Enemy array
        for (int i = 0; i < GameLevel * maxEnemies; i++) {

            Random randEnemy = new Random();
            int enemyCheck = randEnemy.nextInt(3);

            if(enemyCheck == 0 || enemyCheck == 2) {
                EnemyShip newEnemy = new EnemyShip(context, screenX, screenY, 3, enemyCheck);
                //Start with 3 Enemies on screen
                newEnemy.setIsVisible(false);
                //This adds the enemy to the List
                EnemyList.add(newEnemy);
            }
            if(enemyCheck == 1) {
                EnemyShip newEnemy = new EnemyShip(context, screenX, screenY, 2, enemyCheck);
                //Start with 3 Enemies on screen
                newEnemy.setIsVisible(false);
                //This adds the enemy to the List
                EnemyList.add(newEnemy);
            }
        }

    }

    public void respawnPlayer() {

        //Make a Player Ship
        pShip = new PlayerShip(context, screenX, screenY, gd.getBaseSpeed());

        //Prepare Bullets

        projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);

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
                    nextEnemy = 2000;
                    prepareLevel();
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
                        if (e.shouldShoot(pShip.getX(), pShip.getWidth()) && e.getEnemyType() != 2) {
                            if (gd.getSoundFX()) {
                                mp = MediaPlayer.create(context, R.raw.bullet);
                                mp.start();
                            }
                            Projectile p = new Projectile(context, screenY, screenX, true, 0);
                            p.shoot(e.getX() + e.getLength() / 2, e.getRect().bottom, projectile.DOWN);
                            enemyBullets.add(p);
                        }
                    }

                    //Check if time to remove enemy objects
                    if (e.getY() < -e.getHeight() * 2 || e.getY() > screenY + e.getHeight() * 2) {
                        EnemyList.remove(e);

                        Log.d("Oops", "Enemy ship has gotten past you");

                        //Decrease score if enemy gets past player
                        if (score != 0) {
                            score -= 100;
                        }
                    }

                    if(e.getEnemyType() == 1) {
                        if(e.getX() < -e.getLength() * 2 || e.getX() > screenX + e.getLength() * 2) {
                            if(e.getEnemyDirection() == 1) {
                                EnemyList.remove(e);

                                Log.d("Oops", "Enemy ship has gotten past you");

                                //Decrease score if enemy gets past player
                                if (score != 0) {
                                    score -= 100;
                                }
                            }
                        }

                        if(e.getX() > -e.getLength() * 2 || e.getX() < screenX + e.getLength() * 2) {
                            if(e.getEnemyDirection() == 2) {
                                EnemyList.remove(e);

                                Log.d("Oops", "Enemy ship has gotten past you");

                                //Decrease score if enemy gets past player
                                if (score != 0) {
                                    score -= 100;
                                }
                            }
                        }
                    }


                    if (RectF.intersects(e.getRect(), pShip.getRect())) {
                        if (gd.getSoundFX()) {
                            mp = MediaPlayer.create(context, R.raw.explosion);
                            mp.start();
                        }
                        lives--;
                        if (lives == 0) {
                            gameOver();
                        }
                        AnimationDrawable explDraw = createExplosionDrawable();
                        explDraws.add(explDraw);

                        currExFrames.add(0);

                        explX.add((int) EnemyList.get(i).getX());
                        explY.add((int) EnemyList.get(i).getY());

                        EnemyList.remove(i);
                        Log.d("Crash", "Hope You Have Insurance Buddy");
                    }
                }
            }

            //Update the Coins

            for (int i = 0; i < coins.size(); i++) {
                Coin c = coins.get(i);
                if (c.getStatus()) {
                    c.update(fps);
                }
            }

            for (int i = 0; i < powerups.size(); i++) {
                Powerup u = powerups.get(i);
                if (u.getStatus()) {
                    u.update(fps);
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

                                e.setEnemyLives();

                                if(e.getEnemyLives() == 0) {
                                    if (gd.getSoundFX()) {
                                        mp = MediaPlayer.create(context, R.raw.explosion);
                                        mp.start();
                                    }

                                    Random randCoin = new Random();
                                    int coinCheck = randCoin.nextInt(3);

                                    if (coinCheck == 0) {
                                        Coin c = new Coin(context, screenY, screenX);
                                        c.spawn(e.getX(), e.getY());
                                        coins.add(c);
                                    }

                                    Random randPowerup = new Random();
                                    int powerupCheck = randPowerup.nextInt(5);

                                    for(int k = 0; k < 3; k++) {
                                        if(powerupCheck == k) {
                                            Powerup u = new Powerup(context, screenY, screenX, powerupCheck);
                                            u.spawn(e.getX(), e.getY());
                                            powerups.add(u);
                                        }
                                    }

                                    AnimationDrawable explDraw = createExplosionDrawable();
                                    explDraws.add(explDraw);

                                    currExFrames.add(0);

                                    explX.add((int) EnemyList.get(j).getX());
                                    explY.add((int) EnemyList.get(j).getY());

                                    e.setIsVisible(false);
                                    EnemyList.remove(e);
                                }
                                p.setInactive();
                                playerBullets.remove(p);
                                score += 100;
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < coins.size(); i++) {
                Coin c = coins.get(i);
                if (c.getStatus()) {
                    c.update(fps);
                }

                if (c.getY() < -c.getHeight() * 2 || c.getY() > screenY + c.getHeight() * 2) {
                    c.setInactive();
                    coins.remove(c);
                }

                if (RectF.intersects(c.getRect(), pShip.getRect())) {
                    c.setInactive();
                    coins.remove(c);
                    if (gd.getSoundFX()) {
                        mp = MediaPlayer.create(context, R.raw.coin);
                        mp.start();
                    }
                    currency = currency + 10;
                }
            }

            for (int i = 0; i < powerups.size(); i++) {
                Powerup u = powerups.get(i);
                if (u.getStatus()) {
                    u.update(fps);
                }

                if (u.getY() < -u.getHeight() * 2 || u.getY() > screenY + u.getHeight() * 2) {
                    u.setInactive();
                    powerups.remove(u);
                }

                if (RectF.intersects(u.getRect(), pShip.getRect())) {
                    u.setInactive();
                    if (gd.getSoundFX()) {
                        mp = MediaPlayer.create(context, R.raw.powerup);
                        mp.start();
                    }
                    if(pWeaponType == powerups.get(i).getPowerupType()) {
                        if(pWeaponLevel < 2) {
                            pWeaponLevel++;
                        }
                    }
                    pWeaponType = powerups.get(i).getPowerupType();
                    powerups.remove(u);
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

            for (int i = 0; i < currExFrames.size(); i++) {
                currExFrames.set(i, currExFrames.get(i) + 1);
                if (currExFrames.get(i) == 6) {
                    explDraws.remove(i);
                    explX.remove(i);
                    explY.remove(i);
                    currExFrames.remove(i);
                }
            }
        }
    }


    private void draw() {


        if (waitRestart == false) {
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

                //Draw the Active coins

                for (int i = 0; i < coins.size(); i++) {
                    Coin c = coins.get(i);
                    if (c.getStatus()) {
                        canvas.drawBitmap(c.getBmp(), c.getX(), c.getY(), paint);
                    }
                }

                //Draw the Active powerups

                for (int i = 0; i < powerups.size(); i++) {
                    Powerup u = powerups.get(i);
                    if (u.getStatus()) {
                        canvas.drawBitmap(u.getBmp(), u.getX(), u.getY(), paint);
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

                for (int i = 0; i < explDraws.size(); i++) {
                    if (currExFrames.get(i) < explDraws.get(i).getNumberOfFrames()) {
                        Drawable ex = explDraws.get(i).getFrame(currExFrames.get(i));
                        ex.setBounds(explX.get(i), explY.get(i), explX.get(i) + 100, explY.get(i) + 100);
                        ex.draw(canvas);
                    }
                }


                //Draw Score & Lives

                paint.setColor(Color.argb(255, 249, 129, 0));
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(40);
                canvas.drawText("Score: " + score + "\t\t\tLives: " + lives + "\t\t\tCoins: " + currency, 10, 50, paint);


                ourHolder.unlockCanvasAndPost(canvas);


            }
        } else {
            if (ourHolder.getSurface().isValid()) {

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


    public void Pause() {

        playing = false;
        try {
            gameThread.join();
        } catch (Exception e) {
            Log.d("CERROR", "Error Pause()");
            e.printStackTrace();
        }

    }


    public void Resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {


        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {


            case MotionEvent.ACTION_DOWN: {

                //You are not waiting to restart
                if (waitRestart != false) {
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

                if (nextShot < 0) {
                    projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel); // reset projectile
                    if (motionEvent.getY() < screenY - screenY / 4) {
                        if (gd.getSoundFX()) {
                            mp = MediaPlayer.create(context, R.raw.bullet);
                            mp.start();
                        }
                        if(pWeaponLevel == 0 || pWeaponType == 2) {
                            projectile.shoot(pShip.getX() + pShip.getWidth() / 2, pShip.getRect().top, projectile.UP);
                            playerBullets.add(projectile);
                        }
                        if(pWeaponType == 0 && pWeaponLevel == 1) {
                            projectile.shoot(pShip.getX() + pShip.getWidth() / 2 - 50, pShip.getRect().top, projectile.UP);
                            playerBullets.add(projectile);
                            projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                            projectile.shoot((pShip.getX() + pShip.getWidth() / 2) + 50, pShip.getRect().top, projectile.UP);
                            playerBullets.add(projectile);
                        }
                        if(pWeaponType == 0 && pWeaponLevel == 2) {
                            projectile.shoot(pShip.getX() + pShip.getWidth() / 2, pShip.getRect().top, projectile.UP);
                            playerBullets.add(projectile);
                            projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                            projectile.shoot((pShip.getX() + pShip.getWidth() / 2) - 100, pShip.getRect().top, projectile.UP);
                            playerBullets.add(projectile);
                            projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                            projectile.shoot((pShip.getX() + pShip.getWidth() / 2) + 100, pShip.getRect().top, projectile.UP);
                            playerBullets.add(projectile);
                        }
                        if(pWeaponType == 1 && pWeaponLevel == 1) {
                            projectile.shoot(pShip.getX() + pShip.getWidth() / 2 - 50, pShip.getRect().top, projectile.LEFT);
                            playerBullets.add(projectile);
                            projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                            projectile.shoot((pShip.getX() + pShip.getWidth() / 2) + 50, pShip.getRect().top, projectile.RIGHT);
                            playerBullets.add(projectile);
                        }
                        if(pWeaponType == 1 && pWeaponLevel == 2) {
                            projectile.shoot(pShip.getX() + pShip.getWidth() / 2, pShip.getRect().top, projectile.UP);
                            playerBullets.add(projectile);
                            projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                            projectile.shoot((pShip.getX() + pShip.getWidth() / 2) - 100, pShip.getRect().top, projectile.LEFT);
                            playerBullets.add(projectile);
                            projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                            projectile.shoot((pShip.getX() + pShip.getWidth() / 2) + 100, pShip.getRect().top, projectile.RIGHT);
                            playerBullets.add(projectile);
                        }
                    }
                    nextShot = 250;

                    if(pWeaponType == 0) {
                        nextShot = 450;
                    }
                    if(pWeaponType == 1) {
                        nextShot = 500;
                    }
                    if(pWeaponType == 3) {
                        nextShot = 400;
                    }

                    nextShot = nextShot - (pWeaponDamage * 25);
                }


                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
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


                if (nextShot < 0) {
                    if (yPos < screenY - screenY / 4) {
                        projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel); // reset projectile
                        if (projectile.shoot(pShip.getX() + pShip.getWidth() / 2, pShip.getRect().top, projectile.UP)) {
                            playerBullets.add(projectile);
                        }

                    }
                    nextShot = 250;

                }
                break;
            }


            case MotionEvent.ACTION_UP: {
                paused = false;
                pShip.setMovementState(pShip.STOPPED);
                break;
            }


        }
        return true;
    }


    public void gameOver() {


        //Display highscore


        //Ask restart
        DBHelper db = new DBHelper(getContext());

        gd.setCurrency(currency, db);


        try {
            //Do Restart
            playing = false;
            paused = true;
            EnemyList.clear();
            enemyBullets.clear();
            playerBullets.clear();
            nextShot = 0; //When is the next shot
            nextEnemy = 5000;//When to display the next enemy

            coins.clear();
            powerups.clear();

            explDraws.clear();
            explX.clear();
            explY.clear();
            currExFrames.clear();

            pWeaponType = 0;
            pWeaponLevel = 0;
            lives = gd.getBaseLives() + 3;
            currBgFrame = 0;
            GameLevel = 0;
            waitRestart = true;


        } catch (Exception e) {

        } finally {
            prepareLevel();
        }
    }


}

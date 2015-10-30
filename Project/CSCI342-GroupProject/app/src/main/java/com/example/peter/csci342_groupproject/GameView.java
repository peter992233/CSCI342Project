package com.example.peter.csci342_groupproject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by pb864 on 17/10/15.
 */
public class GameView extends SurfaceView implements Runnable {

    private Context context;

    //This is the game thread
    private Thread gameThread = null;

    //Locks the surface on which to draw graphics
    private SurfaceHolder ourHolder;

    //Boolean to set/unset whether the game is running
    private volatile boolean playing;

    //Set the default mode to be paused at the start
    public boolean paused = true;

    private boolean firstTap = true;

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
    int maxEnemies = 20;

    public int score = 0;
    private int lives = 0;
    int currency = 0;

    ImageView backGround;
    int currBgFrame = 0;

    int GameLevel = 0;
    public boolean waitRestart = false;

    int pWeaponType = 0;
    int pWeaponLevel = 0;
    int pWeaponDamage = 0;

    private GameData gd = GameData.getInstance();

    //the sound pool that holds all the game sounds
    private SoundPool soundPool = null;
    private int soundCount = 0;

    //coin sound
    private boolean coinLoaded = false;
    private int coinID = 0;

    //player bullet sound
    private boolean playerBulletLoaded = false;
    private int playerBulletID = 0;

    //explosion sound
    private boolean explosionLoaded = false;
    private int explosionID = 0;

    //playerHit sound
    private boolean playerHitLoaded = false;
    private int playerHitID = 0;

    //enemy bullet sound
    private boolean enemyBulletLoaded = false;
    private int enemyBulletID = 0;

    //power up sound
    private boolean powerLoaded = false;
    private int powerID = 0;

    boolean spawnedBoss = false;

    private FragmentManager fragmentManager = null;

    public GameView(Context context, FragmentManager fm, int x, int y) {
        super(context);

        this.context = context;

        this.fragmentManager = fm;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        //initial Lives
        lives = gd.getBaseLives() + 3;
        currency = gd.getCurrency();
        pWeaponDamage = gd.getBaseDamage().intValue();

        // Load the sounds
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                switch (soundCount) {
                    case 0:
                        coinLoaded = true;
                        break;
                    case 1:
                        playerBulletLoaded = true;
                        break;
                    case 2:
                        explosionLoaded = true;
                        break;
                    case 3:
                        powerLoaded = true;
                        break;
                    case 4:
                        playerHitLoaded = true;
                        break;
                    case 5:
                        enemyBulletLoaded = true;
                        break;
                }
                soundCount++;
            }
        });
        coinID = soundPool.load(context, R.raw.coin, 1);
        playerBulletID = soundPool.load(context, R.raw.bullet, 1);
        explosionID = soundPool.load(context, R.raw.explosion, 1);
        powerID = soundPool.load(context, R.raw.powerup, 1);
        playerHitID = soundPool.load(context, R.raw.hit, 1);
        enemyBulletID = soundPool.load(context, R.raw.enemy_bullet, 1);

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
                if (EnemyList.size() == 0 && spawnedBoss == false && GameLevel == 1) {
                    EnemyShip newEnemy = new EnemyShip(context, screenX, screenY, 10, 3);
                    newEnemy.setIsVisible(false);
                    EnemyList.add(newEnemy);
                    spawnedBoss = true;
                }
                if (EnemyList.size() == 0 && spawnedBoss == false && GameLevel == 2) {
                    EnemyShip newEnemy = new EnemyShip(context, screenX, screenY, 20, 4);
                    newEnemy.setIsVisible(false);
                    EnemyList.add(newEnemy);
                    spawnedBoss = true;
                }
                if (EnemyList.size() == 0 && spawnedBoss == false && GameLevel == 3) {
                    EnemyShip newEnemy = new EnemyShip(context, screenX, screenY, 30, 5);
                    newEnemy.setIsVisible(false);
                    EnemyList.add(newEnemy);
                    spawnedBoss = true;
                }
                if (EnemyList.size() == 0 && spawnedBoss == false && GameLevel == 4) {
                    EnemyShip newEnemy = new EnemyShip(context, screenX, screenY, 40, 6);
                    newEnemy.setIsVisible(false);
                    EnemyList.add(newEnemy);
                    spawnedBoss = true;
                }
                if (EnemyList.size() == 0 && spawnedBoss == false && GameLevel == 5) {
                    EnemyShip newEnemy = new EnemyShip(context, screenX, screenY, 50, 7);
                    newEnemy.setIsVisible(false);
                    EnemyList.add(newEnemy);
                    spawnedBoss = true;
                }
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

            if (enemyCheck == 0 || enemyCheck == 2) {
                EnemyShip newEnemy = new EnemyShip(context, screenX, screenY, 3, enemyCheck);
                //Start with 3 Enemies on screen
                newEnemy.setIsVisible(false);
                //This adds the enemy to the List
                EnemyList.add(newEnemy);
            }
            if (enemyCheck == 1) {
                EnemyShip newEnemy = new EnemyShip(context, screenX, screenY, 2, enemyCheck);
                //Start with 3 Enemies on screen
                newEnemy.setIsVisible(false);
                //This adds the enemy to the List
                EnemyList.add(newEnemy);
            }
        }
        spawnedBoss = false;
    }

    public void respawnPlayer() {

        //Make a Player Ship
        pShip = new PlayerShip(context, screenX, screenY, gd.getBaseSpeed());

        //Prepare Bullets
        projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
    }


    private void update() {

        if (!paused) {

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
                            if ((gd.getSoundFX() && (enemyBulletLoaded)))
                                soundPool.play(enemyBulletID, gd.getVolume().floatValue(), gd.getVolume().floatValue(), 1, 0, 1f);
                            if (e.getEnemyType() <= 3) {
                                Projectile p = new Projectile(context, screenY, screenX, true, 0);
                                p.shoot(e.getX() + e.getLength() / 2, e.getRect().bottom, projectile.DOWN);
                                enemyBullets.add(p);
                            }
                            if (e.getEnemyType() == 4) {
                                Projectile p = new Projectile(context, screenY, screenX, true, 0);
                                p.shoot(e.getX() + e.getLength() / 2 - 75, e.getRect().bottom, projectile.DOWN);
                                enemyBullets.add(p);
                                p = new Projectile(context, screenY, screenX, true, 0);
                                p.shoot(e.getX() + e.getLength() / 2 + 75, e.getRect().bottom, projectile.DOWN);
                                enemyBullets.add(p);
                            }
                            if (e.getEnemyType() == 5) {
                                Projectile p = new Projectile(context, screenY, screenX, true, 0);
                                p.shoot(e.getX() + e.getLength() / 2, e.getRect().bottom, projectile.DOWN);
                                enemyBullets.add(p);
                                p = new Projectile(context, screenY, screenX, true, 0);
                                p.shoot(e.getX() + e.getLength() / 2 - 150, e.getRect().bottom - 75, projectile.DOWN);
                                enemyBullets.add(p);
                                p = new Projectile(context, screenY, screenX, true, 0);
                                p.shoot(e.getX() + e.getLength() / 2 + 150, e.getRect().bottom - 75, projectile.DOWN);
                                enemyBullets.add(p);
                            }
                            if (e.getEnemyType() == 6) {
                                Projectile p = new Projectile(context, screenY, screenX, true, 0);
                                p.shoot(e.getX() + e.getLength() / 2 - 225, e.getRect().bottom, projectile.DOWN);
                                enemyBullets.add(p);
                                p = new Projectile(context, screenY, screenX, true, 0);
                                p.shoot(e.getX() + e.getLength() / 2 - 175, e.getRect().bottom, projectile.DOWN);
                                enemyBullets.add(p);
                                p = new Projectile(context, screenY, screenX, true, 0);
                                p.shoot(e.getX() + e.getLength() / 2 + 225, e.getRect().bottom, projectile.DOWN);
                                enemyBullets.add(p);
                                p = new Projectile(context, screenY, screenX, true, 0);
                                p.shoot(e.getX() + e.getLength() / 2 + 175, e.getRect().bottom, projectile.DOWN);
                                enemyBullets.add(p);
                            }
                            if (e.getEnemyType() == 7) {
                                Random randBullet = new Random();
                                int bulletCheck = randBullet.nextInt(6);
                                bulletCheck = bulletCheck - 3;

                                Projectile p = new Projectile(context, screenY, screenX, true, 0);
                                p.shoot(e.getX() + e.getLength() / 2 + (bulletCheck * 100), e.getRect().bottom, projectile.DOWN);
                                enemyBullets.add(p);
                            }
                        }
                    }

                    //Check if time to remove enemy objects
                    if (e.getY() < -e.getHeight() * 2 || e.getY() > screenY + e.getHeight() * 2) {
                        EnemyList.remove(e);

                        //Decrease score if enemy gets past player
                        if (score != 0) {
                            score -= 100;
                        }
                    }

                    if (e.getEnemyType() == 1) {
                        if (e.getX() < -e.getLength() * 2 || e.getX() > screenX + e.getLength() * 2) {
                            if (e.getEnemyDirection() == 1) {
                                EnemyList.remove(e);

                                //Decrease score if enemy gets past player
                                if (score != 0) {
                                    score -= 100;
                                }
                            }
                        }

                        if (e.getX() > -e.getLength() * 2 || e.getX() < screenX + e.getLength() * 2) {
                            if (e.getEnemyDirection() == 2) {
                                EnemyList.remove(e);

                                //Decrease score if enemy gets past player
                                if (score != 0) {
                                    score -= 100;
                                }
                            }
                        }
                    }

                    if (RectF.intersects(e.getRect(), pShip.getRect())) {
                        if ((gd.getSoundFX() && (explosionLoaded)))
                            soundPool.play(explosionID, gd.getVolume().floatValue(), gd.getVolume().floatValue(), 1, 0, 1f);
                        lives--;
                        if (lives == 0) {
                            waitRestart = true;
                            gameOver();
                        }
                        AnimationDrawable explDraw = createExplosionDrawable();
                        explDraws.add(explDraw);

                        currExFrames.add(0);

                        explX.add((int) EnemyList.get(i).getX());
                        explY.add((int) EnemyList.get(i).getY());

                        EnemyList.remove(i);
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
                                e.setEnemyLives();
                                if (e.getEnemyLives() == 0) {
                                    if ((gd.getSoundFX() && (explosionLoaded)))
                                        soundPool.play(explosionID, gd.getVolume().floatValue(), gd.getVolume().floatValue(), 1, 0, 1f);
                                    Random randCoin = new Random();
                                    int coinCheck = randCoin.nextInt(3);

                                    if (coinCheck == 0) {
                                        Coin c = new Coin(context, screenY, screenX);
                                        c.spawn(e.getX(), e.getY());
                                        coins.add(c);
                                    }

                                    Random randPowerup = new Random();
                                    int powerupCheck = randPowerup.nextInt(10);

                                    for (int k = 0; k < 3; k++) {
                                        if (powerupCheck == k) {
                                            Powerup u = new Powerup(context, screenY, screenX, powerupCheck);
                                            u.spawn(e.getX(), e.getY());
                                            powerups.add(u);
                                        }
                                    }

                                    if (e.getEnemyType() <= 2) {
                                        AnimationDrawable explDraw = createExplosionDrawable();
                                        explDraws.add(explDraw);

                                        currExFrames.add(0);

                                        explX.add((int) EnemyList.get(j).getX());
                                        explY.add((int) EnemyList.get(j).getY());
                                    }

                                    e.setIsVisible(false);
                                    EnemyList.remove(e);

                                    score += 100;
                                } else {
                                    if ((gd.getSoundFX() && (playerHitLoaded)))
                                        soundPool.play(playerHitID, gd.getVolume().floatValue(), gd.getVolume().floatValue(), 1, 0, 1f);
                                }
                                p.setInactive();
                                playerBullets.remove(p);
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
                    if ((gd.getSoundFX() && (coinLoaded)))
                        soundPool.play(coinID, gd.getVolume().floatValue(), gd.getVolume().floatValue(), 1, 0, 1f);
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
                    if ((gd.getSoundFX() && (powerLoaded)))
                        soundPool.play(powerID, gd.getVolume().floatValue(), gd.getVolume().floatValue(), 1, 0, 1f);
                    if (pWeaponType == powerups.get(i).getPowerupType()) {
                        if (pWeaponLevel < 2) {
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
                    if ((gd.getSoundFX() && (playerHitLoaded)))
                        soundPool.play(playerHitID, gd.getVolume().floatValue(), gd.getVolume().floatValue(), 1, 0, 1f);
                    p.setInactive();
                    enemyBullets.remove(p);
                    lives--;
                    if (lives <= 0) {
                        waitRestart = true;
                        gameOver();
                    }
                }
            }

            if (lives <= 0) {
                waitRestart = true;
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

        if (!waitRestart) {
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
                    if (e.isVisible()) {
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

                ourHolder.unlockCanvasAndPost(canvas);
            }
        }
    }


    public void Pause() {

        playing = false;
        try {
            gameThread.join();
        } catch (Exception e) {
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

                if ((nextShot < 0) && (!firstTap)) {
                    projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel); // reset projectile
                    if (motionEvent.getY() < screenY - screenY / 4) {
                        if ((gd.getSoundFX() && (playerBulletLoaded)))
                            soundPool.play(playerBulletID, gd.getVolume().floatValue(), gd.getVolume().floatValue(), 1, 0, 1f);
                        if (pWeaponLevel == 0 && pWeaponType != 0) {
                            projectile.shoot(pShip.getX() + pShip.getWidth() / 2 - 30, pShip.getRect().top, projectile.UP);
                            playerBullets.add(projectile);
                        }
                        if (pWeaponLevel == 0 && pWeaponType == 0) {
                            projectile.shoot(pShip.getX() + pShip.getWidth() / 2 - 8, pShip.getRect().top, projectile.UP);
                            playerBullets.add(projectile);
                        }
                        if (pWeaponType == 0 && pWeaponLevel == 1) {
                            projectile.shoot(pShip.getX() + pShip.getWidth() / 2 - 58, pShip.getRect().top, projectile.UP);
                            playerBullets.add(projectile);
                            projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                            projectile.shoot((pShip.getX() + pShip.getWidth() / 2) + 42, pShip.getRect().top, projectile.UP);
                            playerBullets.add(projectile);
                        }
                        if (pWeaponType == 0 && pWeaponLevel == 2) {
                            projectile.shoot(pShip.getX() + pShip.getWidth() / 2 - 8, pShip.getRect().top, projectile.UP);
                            playerBullets.add(projectile);
                            projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                            projectile.shoot((pShip.getX() + pShip.getWidth() / 2) - 108, pShip.getRect().top, projectile.UP);
                            playerBullets.add(projectile);
                            projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                            projectile.shoot((pShip.getX() + pShip.getWidth() / 2) + 92, pShip.getRect().top, projectile.UP);
                            playerBullets.add(projectile);
                        }
                        if (pWeaponType == 1 && pWeaponLevel == 1) {
                            projectile.shoot(pShip.getX() + pShip.getWidth() / 2 - 80, pShip.getRect().top, projectile.LEFT);
                            playerBullets.add(projectile);
                            projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                            projectile.shoot((pShip.getX() + pShip.getWidth() / 2) + 20, pShip.getRect().top, projectile.RIGHT);
                            playerBullets.add(projectile);
                        }
                        if (pWeaponType == 1 && pWeaponLevel == 2) {
                            projectile.shoot(pShip.getX() + pShip.getWidth() / 2 - 30, pShip.getRect().top, projectile.UP);
                            playerBullets.add(projectile);
                            projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                            projectile.shoot((pShip.getX() + pShip.getWidth() / 2) - 130, pShip.getRect().top, projectile.LEFT);
                            playerBullets.add(projectile);
                            projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                            projectile.shoot((pShip.getX() + pShip.getWidth() / 2) + 70, pShip.getRect().top, projectile.RIGHT);
                            playerBullets.add(projectile);
                        }
                        if (pWeaponType == 2 && pWeaponLevel == 1) {
                            projectile.shoot((pShip.getX() + pShip.getWidth() / 2) - 62, pShip.getRect().top, projectile.UP);
                            playerBullets.add(projectile);
                        }
                        if (pWeaponType == 2 && pWeaponLevel == 2) {
                            projectile.shoot((pShip.getX() + pShip.getWidth() / 2) - 92, pShip.getRect().top, projectile.UP);
                            playerBullets.add(projectile);
                        }
                    }
                    nextShot = 250;

                    if (pWeaponType == 0) {
                        nextShot = 450;
                    }
                    if (pWeaponType == 1) {
                        nextShot = 500;
                    }
                    if (pWeaponType == 3) {
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

                if ((nextShot < 0) && (!firstTap)) {
                    if (yPos < screenY - screenY / 4) {
                        projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel); // reset projectile
                        if (projectile.shoot(pShip.getX() + pShip.getWidth() / 2, pShip.getRect().top, projectile.UP)) {
                            if ((gd.getSoundFX() && (playerBulletLoaded)))
                                soundPool.play(playerBulletID, gd.getVolume().floatValue(), gd.getVolume().floatValue(), 1, 0, 1f);
                            if (pWeaponLevel == 0 && pWeaponType != 0) {
                                projectile.shoot(pShip.getX() + pShip.getWidth() / 2 - 30, pShip.getRect().top, projectile.UP);
                                playerBullets.add(projectile);
                            }
                            if (pWeaponLevel == 0 && pWeaponType == 0) {
                                projectile.shoot(pShip.getX() + pShip.getWidth() / 2 - 8, pShip.getRect().top, projectile.UP);
                                playerBullets.add(projectile);
                            }
                            if (pWeaponType == 0 && pWeaponLevel == 1) {
                                projectile.shoot(pShip.getX() + pShip.getWidth() / 2 - 58, pShip.getRect().top, projectile.UP);
                                playerBullets.add(projectile);
                                projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                                projectile.shoot((pShip.getX() + pShip.getWidth() / 2) + 42, pShip.getRect().top, projectile.UP);
                                playerBullets.add(projectile);
                            }
                            if (pWeaponType == 0 && pWeaponLevel == 2) {
                                projectile.shoot(pShip.getX() + pShip.getWidth() / 2 - 8, pShip.getRect().top, projectile.UP);
                                playerBullets.add(projectile);
                                projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                                projectile.shoot((pShip.getX() + pShip.getWidth() / 2) - 108, pShip.getRect().top, projectile.UP);
                                playerBullets.add(projectile);
                                projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                                projectile.shoot((pShip.getX() + pShip.getWidth() / 2) + 92, pShip.getRect().top, projectile.UP);
                                playerBullets.add(projectile);
                            }
                            if (pWeaponType == 1 && pWeaponLevel == 1) {
                                projectile.shoot(pShip.getX() + pShip.getWidth() / 2 - 80, pShip.getRect().top, projectile.LEFT);
                                playerBullets.add(projectile);
                                projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                                projectile.shoot((pShip.getX() + pShip.getWidth() / 2) + 20, pShip.getRect().top, projectile.RIGHT);
                                playerBullets.add(projectile);
                            }
                            if (pWeaponType == 1 && pWeaponLevel == 2) {
                                projectile.shoot(pShip.getX() + pShip.getWidth() / 2 - 30, pShip.getRect().top, projectile.UP);
                                playerBullets.add(projectile);
                                projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                                projectile.shoot((pShip.getX() + pShip.getWidth() / 2) - 130, pShip.getRect().top, projectile.LEFT);
                                playerBullets.add(projectile);
                                projectile = new Projectile(context, screenY, screenX, pWeaponType, pWeaponLevel);
                                projectile.shoot((pShip.getX() + pShip.getWidth() / 2) + 70, pShip.getRect().top, projectile.RIGHT);
                                playerBullets.add(projectile);
                            }
                            if (pWeaponType == 2 && pWeaponLevel == 1) {
                                projectile.shoot((pShip.getX() + pShip.getWidth() / 2) - 62, pShip.getRect().top, projectile.UP);
                                playerBullets.add(projectile);
                            }
                            if (pWeaponType == 2 && pWeaponLevel == 2) {
                                projectile.shoot((pShip.getX() + pShip.getWidth() / 2) - 92, pShip.getRect().top, projectile.UP);
                                playerBullets.add(projectile);
                            }
                        }
                    }
                    nextShot = 250;

                    if (pWeaponType == 0) {
                        nextShot = 450;
                    }
                    if (pWeaponType == 1) {
                        nextShot = 500;
                    }
                    if (pWeaponType == 3) {
                        nextShot = 400;
                    }

                    nextShot = nextShot - (pWeaponDamage * 25);
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                paused = false;
                pShip.setMovementState(pShip.STOPPED);
                break;
            }
        }
        firstTap = false;
        return true;
    }


    public void gameOver() {
        restartGame();

        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("gcd");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        GameCompleteDialog gcd = GameCompleteDialog.newInstance(score);
        gcd.show(ft, "gcd");
    }

    public void doPlayAgainClick(GameCompleteDialog dialog) {
        EditText et = (EditText) dialog.getDialog().findViewById(R.id.username);
        if (!et.getText().toString().equals("")) {
            new SendHighScore().execute(et.getText().toString(), Integer.toString(score));
        }
        dialog.getDialog().dismiss();
    }

    public void doMenuClick(GameCompleteDialog dialog) {
        EditText et = (EditText) dialog.getDialog().findViewById(R.id.username);
        if (!et.getText().toString().equals("")) {
            new SendHighScore().execute(et.getText().toString(), Integer.toString(score));
        }

        dialog.getDialog().dismiss();
        ((Activity) context).finish();
    }

    public void restartGame() {

        DBHelper db = new DBHelper(getContext());
        gd.setCurrency(currency, db);

        firstTap = true;
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

        prepareLevel();
    }

    private class SendHighScore extends AsyncTask<String, Void, String> {

        String urlString = "http://203.143.84.128/csci342/addScore.php";
        String urlParameters = "";

        @Override
        protected String doInBackground(String... data) {

            try {
                urlParameters = "uName=" + URLEncoder.encode(data[0], "UTF-8") + "&score=" + URLEncoder.encode(data[1], "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }

            URL url;
            HttpURLConnection connection = null;
            try {
                //Create connection
                url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

                connection.setRequestProperty("Content-Length", "" +
                        Integer.toString(urlParameters.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");

                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return urlParameters;
        }

        @Override
        protected void onPostExecute(String jString) {
            super.onPostExecute(jString);
        }
    }
}
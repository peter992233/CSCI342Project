package com.example.peter.csci342_groupproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by pb864 on 17/10/15.
 */
public class EnemyShip {

    //The Containing rect data for the enemy Ship
    RectF rect;

    //Random Shooting Generator (To Choose when the Enemy Should Shoot)
    Random shootGen = new Random();

    //Enemy Bitmap Data
    private Bitmap enemyBMP;

    //length and height data
    private float length;
    private float height;

    //The far left X of the Enemy
    private float x;

    //The top Y coordinate of the Enemy
    private float y;

    //This is the Movement speed of the enemy ship class
    private float shipSpeed;

    private int shipMoved = 25;

    //Movement types for the enemy
    public final int EMPTY = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;
    public final int DOWN = 3;
    public final int UP = 4;

    //The Dual Coordinate Movement allowing for movement on Diagonals as well as XY
    private int[] shipMoving = {EMPTY, DOWN};

    int enemyType;

    //Is the Enemy visible
    boolean isVisible;

    int enemyLives;

    int SY;

    boolean shipSet = false;

    private long lastShot = System.currentTimeMillis();

    public long getLastShot() {
        return lastShot;
    }

    public void setLastShot(long lastShot) {
        this.lastShot = lastShot;
    }

    public EnemyShip(Context context, int screenX, int screenY, int eLives, int eType) {

        // Initialize a blank RectF
        rect = new RectF();

        //Set the length of the Ship
        length = screenX / 16;
        height = screenY / 12;

        SY = screenY;

        //Set the ship to display as visible
        isVisible = true;

        enemyLives = eLives;
        enemyType = eType;

        if (enemyType == 0) {
            enemyBMP = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_vertical);
            enemyBMP = Bitmap.createScaledBitmap(enemyBMP, (int) length, (int) height, false);
            Random r = new Random();
            int pushX = (int) length * 2 + r.nextInt(screenX - (int) length * 3 + 1);
            x = pushX - length;
        }
        if (enemyType == 1) {
            length = screenX / 20;
            height = screenX / 26;

            Random randDirection = new Random();
            int checkDirection = randDirection.nextInt(2);
            if (checkDirection == 0) {
                shipMoving[0] = LEFT;
            }
            if (checkDirection == 1) {
                shipMoving[0] = RIGHT;
            }
            shipMoving[1] = EMPTY;

            enemyBMP = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_horizontal);
            enemyBMP = Bitmap.createScaledBitmap(enemyBMP, (int) length, (int) height, false);

            Random r = new Random();
            int pushY = (int) height * 2 + r.nextInt((screenY - screenY / 3) - (int) height * 3 + 1);
            y = pushY - height;
            if (shipMoving[0] == LEFT) {
                x = screenX;
            }
        }
        if (enemyType == 2) {
            length = screenX / 16;
            height = screenY / 12;

            Random randDirection = new Random();
            int checkDirection = randDirection.nextInt(2);

            if (checkDirection == 0) {
                shipMoving[0] = LEFT;
            }
            if (checkDirection == 1) {
                shipMoving[0] = RIGHT;
            }

            enemyBMP = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_diagonal);
            enemyBMP = Bitmap.createScaledBitmap(enemyBMP, (int) length, (int) height, false);

            Random r = new Random();
            int pushX = (int) length * 2 + r.nextInt(screenX - (int) length * 3 + 1);
            x = pushX - length;
        }
        if (enemyType == 3) {
            length = screenX / 20;
            height = screenY / 4;

            enemyBMP = BitmapFactory.decodeResource(context.getResources(), R.drawable.boss_1);
            enemyBMP = Bitmap.createScaledBitmap(enemyBMP, (int) length, (int) height, false);

            shipMoved = 100;

            x = screenX / 2;
            y = 0 - height;
        }
        if (enemyType == 4) {
            length = screenX / 6;
            height = screenY / 4;

            enemyBMP = BitmapFactory.decodeResource(context.getResources(), R.drawable.boss_2);
            enemyBMP = Bitmap.createScaledBitmap(enemyBMP, (int) length, (int) height, false);

            shipMoved = 100;

            x = screenX / 2;
            y = 0 - height;
        }
        if (enemyType == 5) {
            length = screenX / 6;
            height = screenY / 4;

            enemyBMP = BitmapFactory.decodeResource(context.getResources(), R.drawable.boss_3);
            enemyBMP = Bitmap.createScaledBitmap(enemyBMP, (int) length, (int) height, false);
            shipMoved = 100;

            x = screenX / 2;
            y = 0 - height;
        }
        if (enemyType == 6) {
            length = screenX / 4;
            height = screenY / 4;

            enemyBMP = BitmapFactory.decodeResource(context.getResources(), R.drawable.boss_4);
            enemyBMP = Bitmap.createScaledBitmap(enemyBMP, (int) length, (int) height, false);
            shipMoved = 100;

            x = screenX / 2;
            y = 0 - height;
        }
        if (enemyType == 7) {
            length = screenX / 3;
            height = screenY / 4;

            enemyBMP = BitmapFactory.decodeResource(context.getResources(), R.drawable.boss_5);
            enemyBMP = Bitmap.createScaledBitmap(enemyBMP, (int) length, (int) height, false);
            shipMoved = 100;

            x = screenX / 2;
            y = 0 - height;
        }

        //Set The Enemy Movespeed
        shipSpeed = 100;
    }

    public void update(long fps) {

        if (shipMoved > 50 && enemyType == 2) {
            if (shipMoving[0] == LEFT) {
                shipMoving[0] = RIGHT;
            } else if (shipMoving[0] == RIGHT) {
                shipMoving[0] = LEFT;
            }
            shipMoved = 0;
        }
        if (shipMoved > 200 && enemyType >= 3) {
            if (shipMoving[0] == LEFT) {
                shipMoving[0] = RIGHT;
            } else if (shipMoving[0] == RIGHT) {
                shipMoving[0] = LEFT;
            }
            shipMoved = 0;
        }

        //Movement Left, Right, Up and Down Movement
        if (shipMoving[0] == LEFT) {
            x = x - shipSpeed / fps;
        }

        if (shipMoving[0] == RIGHT) {
            x = x + shipSpeed / fps;
        }

        if (shipMoving[1] == UP) {
            y = y - shipSpeed / fps;
        }

        if (shipMoving[1] == DOWN) {
            y = y + shipSpeed / fps;
        }

        // Update rect which is used to detect hits
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

        if (enemyType == 2) {
            shipMoved++;
        }
        if (enemyType >= 3 && y > SY / 12 && shipSet == false) {
            Random randDirection = new Random();
            int checkDirection = randDirection.nextInt(2);

            if (checkDirection == 0) {
                shipMoving[0] = LEFT;
            }
            if (checkDirection == 1) {
                shipMoving[0] = RIGHT;
            }
            shipMoving[1] = EMPTY;

            shipSet = true;
        }
        if (enemyType >= 3 && shipMoving[1] == EMPTY) {
            shipMoved++;
        }
    }


    public boolean shouldShoot(float playerShipX, float playerShipLength) {


        if (System.currentTimeMillis() - lastShot > 2000) {

            int randomNumber = -1;

            // If near the player run a random generator with a 1/100 chance to shoot
            // This takes into account the large amount of updates being sent through for the enemy
            if ((playerShipX + playerShipLength > x && playerShipX + playerShipLength < x + length) ||
                    (playerShipX > x && playerShipX < x + length)) {

                //The random Gen with 1 in 50 chance to shoot
                if (enemyType <= 2) {
                    randomNumber = shootGen.nextInt(30);
                    if (randomNumber == 0) {
                        lastShot = System.currentTimeMillis();
                        return true;
                    }
                } else if (enemyType == 7) {
                    randomNumber = shootGen.nextInt(3);
                    if (randomNumber == 0) {
                        lastShot = System.currentTimeMillis();
                        return true;
                    }
                } else {
                    randomNumber = shootGen.nextInt(10);
                    if (randomNumber == 0) {
                        lastShot = System.currentTimeMillis();
                        return true;
                    }
                }

                //Fire Randomly regardless of player (1/1500)
                if (enemyType <= 2) {
                    randomNumber = shootGen.nextInt(300);
                    if (randomNumber == 0) {
                        lastShot = System.currentTimeMillis();
                        return true;
                    }
                } else if (enemyType == 7) {
                    randomNumber = shootGen.nextInt(30);
                } else {
                    randomNumber = shootGen.nextInt(100);
                    if (randomNumber == 0) {
                        lastShot = System.currentTimeMillis();
                        return true;
                    }
                }
            }
        }


        return false;

    }


    // - - - - - - - Getters & Setters - - - - - - -

    public int getEnemyLives() {
        return enemyLives;
    }

    public void setEnemyLives() {
        enemyLives--;
    }

    public int getEnemyType() {
        return enemyType;
    }

    public int getEnemyDirection() {
        return shipMoving[0];
    }

    public RectF getRect() {
        return rect;
    }

    public void setRect(RectF rect) {
        this.rect = rect;
    }

    public Bitmap getEnemyBMP() {
        return enemyBMP;
    }

    public void setEnemyBMP(Bitmap enemyBMP) {
        this.enemyBMP = enemyBMP;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getShipSpeed() {
        return shipSpeed;
    }

    public void setShipSpeed(float shipSpeed) {
        this.shipSpeed = shipSpeed;
    }

    public int[] getShipMoving() {
        return shipMoving;
    }

    public void setShipMoving(int[] shipMoving) {
        this.shipMoving = shipMoving;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
}

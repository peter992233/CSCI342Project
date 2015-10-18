package com.example.peter.csci342_groupproject;

import android.provider.BaseColumns;

public class MainTable {

    public MainTable(){}

    public static abstract class MainEntry implements BaseColumns {

        public static final String TABLE_NAME = "MainTable";
        public static final String COLUMN_NAME_BASE_LIVES = "baseLives";
        public static final String COLUMN_NAME_BASE_DAMAGE = "baseDamage";
        public static final String COLUMN_NAME_BASE_SPEED = "baseSpeed";
        public static final String COLUMN_NAME_OPTION_FX = "optionFX";
        public static final String COLUMN_NAME_OPTION_MUSIC = "optionMusic";
        public static final String COLUMN_NAME_OPTION_SOUND = "optionSound";
        public static final String COLUMN_NAME_OPTION_TILT = "optionTilt";
    }
}

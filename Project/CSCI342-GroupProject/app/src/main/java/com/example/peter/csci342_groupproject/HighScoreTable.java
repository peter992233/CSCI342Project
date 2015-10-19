package com.example.peter.csci342_groupproject;

import android.provider.BaseColumns;

public class HighScoreTable{

    public HighScoreTable(){}

    public static abstract class HighScoreEntry implements BaseColumns {

        public static final String TABLE_NAME = "HighScoreTable";
        public static final String COLUMN_NAME_UserName= "userName";
        public static final String COLUMN_NAME_HighScore = "highScore";
    }
}

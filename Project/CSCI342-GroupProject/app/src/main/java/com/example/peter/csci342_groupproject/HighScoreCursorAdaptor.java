package com.example.peter.csci342_groupproject;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by jas899 on 22/10/2015.
 */
public class HighScoreCursorAdaptor extends CursorAdapter {
    public HighScoreCursorAdaptor(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.highscorelistitem, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView userNameText = (TextView) view.findViewById(R.id.userName);
        TextView scoreText = (TextView) view.findViewById(R.id.Score);
        // Extract properties from cursor
        String userName = cursor.getString(cursor.getColumnIndexOrThrow(HighScoreTable.HighScoreEntry.COLUMN_NAME_UserName));
        int score = cursor.getInt(cursor.getColumnIndexOrThrow(HighScoreTable.HighScoreEntry.COLUMN_NAME_HighScore));
        // Populate fields with extracted properties
        userNameText.setText(userName);
        scoreText.setText(String.valueOf(score));
    }
}
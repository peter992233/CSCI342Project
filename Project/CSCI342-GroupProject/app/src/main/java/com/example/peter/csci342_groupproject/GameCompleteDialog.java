package com.example.peter.csci342_groupproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Lorthris on 29/10/2015.
 */
public class GameCompleteDialog extends DialogFragment {

    public static GameCompleteDialog newInstance(int score) {
        GameCompleteDialog frag = new GameCompleteDialog();
        Bundle args = new Bundle();
        args.putInt("score", score);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int score = getArguments().getInt("score");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View myView = inflater.inflate(R.layout.dialog_gamecomplete, null);
        builder.setView(myView);

        TextView tv = (TextView) myView.findViewById(R.id.ScoreDialog);
        String s = "" + score;
        tv.setText(("Score: " + s));

        Button mainMenu = (Button) myView.findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                ((GameActivity) getActivity()).gmv.doMenuClick(GameCompleteDialog.this);
            }
        });

        Button playAgain = (Button) myView.findViewById(R.id.playagain);
        playAgain.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                ((GameActivity) getActivity()).gmv.doPlayAgainClick(GameCompleteDialog.this);
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        ((GameActivity) getActivity()).gmv.score = 0;
        ((GameActivity) getActivity()).gmv.waitRestart = false;
    }
}

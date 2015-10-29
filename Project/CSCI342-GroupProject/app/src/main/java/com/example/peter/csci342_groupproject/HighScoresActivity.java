package com.example.peter.csci342_groupproject;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by jas899 on 22/10/2015.
 */

public class HighScoresActivity extends AppCompatActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    MediaPlayer mp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        GameData gd = GameData.getInstance();

        new GetHighScores().execute("");

        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            AssetFileDescriptor afd = this.getResources().openRawResourceFd(R.raw.mainmenumusic);
            if (afd == null) return;
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.setOnErrorListener(this);
            mp.setOnPreparedListener(this);
            mp.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer play) {
        GameData gd = GameData.getInstance();
        play.setVolume(gd.getVolume().floatValue(), gd.getVolume().floatValue());
        play.setLooping(true);
        if (gd.getMusic()) {
            play.start();
        }
    }

    @Override
    public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_high_scores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class GetHighScores extends AsyncTask<String, Void, JSONArray> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONArray doInBackground(String... urls) {
            JSONArray jsonArray = null;
            try {
                URL url = new URL("http://203.143.84.128/csci342/getScores.php");
                URLConnection urlConnection = url.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String data = readStream(br);
                br.close();
                in.close();

                if (data != null) {
                    jsonArray = new JSONArray(data);
                }
            } catch (Exception e) {
            }
            return jsonArray;
        }

        private String readStream(BufferedReader br) {
            StringBuilder total = new StringBuilder();
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    total.append(line);
                }
                return total.toString();
            } catch (Exception e) {
            }
            return null;
        }


        protected void onPostExecute(JSONArray result) {

            if (result == null) {
                TextView tv = (TextView) findViewById(R.id.Message);
                tv.setText("No High Scores Available");

            } else {

                ArrayList<JSONObject> tempArray = new ArrayList<>();
                try {
                    for (int i = 0; i < result.length(); i++) {
                        tempArray.add(i, result.getJSONObject(i));
                    }
                } catch (Exception e) {
                }
                ListView lv = (ListView) findViewById(R.id.ScoresList);
                HSAdapter hsAdapter = new HSAdapter(getApplicationContext(), tempArray);
                lv.setAdapter(hsAdapter);
            }

        }

        private class HSAdapter extends ArrayAdapter<JSONObject> {
            public HSAdapter(Context context, ArrayList<JSONObject> highscores) {
                super(context, 0, highscores);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                JSONObject row = getItem(position);
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.highscorelistitem, parent, false);
                }

                TextView uName = (TextView) convertView.findViewById(R.id.userName);
                TextView score = (TextView) convertView.findViewById(R.id.Score);

                try {
                    uName.setText(row.getString("uName"));
                    score.setText(row.getString("score"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return convertView;
            }
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        if (mp != null) {
            if (mp.isPlaying())
                mp.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mp != null) {
            GameData gd = GameData.getInstance();
            mp.setVolume(gd.getVolume().floatValue(), gd.getVolume().floatValue());
            mp.setLooping(true);
            if (gd.getMusic())
                mp.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }
}
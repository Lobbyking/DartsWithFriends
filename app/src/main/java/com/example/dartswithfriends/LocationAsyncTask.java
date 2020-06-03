package com.example.dartswithfriends;


import android.location.Location;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class LocationAsyncTask extends AsyncTask<String, Integer, Match>
{
    private static final String API_KEY = "caa7aebafc4ecb";
    private HashMap<Player,Integer> game;
    Double longitude;
    Double latitude;

    public LocationAsyncTask(HashMap<Player, Integer> game)
    {
        this.game = game;
    }

    @Override
    protected Match doInBackground(String... strings)
    {
        return null;
    }

}

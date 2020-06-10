package com.example.dartswithfriends;

import java.util.HashMap;
import java.util.Map;

public class Match {
    HashMap<Player,Integer> game;
    Double longitude;
    Double latitude;

    public Match(HashMap<Player, Integer> game, Double longitude, Double latitude) {
        this.game = game;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}

package com.example.dartswithfriends;

import java.util.HashMap;
import java.util.Map;

public class Match {
    HashMap<Player,Double> game;
    Double longitude;
    Double latitude;
    String adresse;

    public Match(HashMap<Player, Double> game, Double longitude, Double latitude) {
        this.game = game;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Match(HashMap<Player, Double> game, Double longitude, Double latitude, String adresse) {
        this.game = game;
        this.longitude = longitude;
        this.latitude = latitude;
        this.adresse=adresse;
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

    public String toString(){
        String print = "";

        for(Player p : game.keySet()){
            print += p.toString() + ";" + game.get(p) +";";
        }

        print += longitude + ";" + latitude;

        return print;
    }
}

package com.example.dartswithfriends;

public class Player {

    public String name;
    public double average;
    public int counter;

    public Player(String n, int c){
        name = n;
        counter = c;
    }

    public String getName(){
        return name;
    }

    public void setAverage(double average){
        this.average = average;
    }

    public double getAvereage(){
        return average;
    }

    public int getCounter(){
        return counter;
    }

    public void increaseCounter(){
        counter++;
    }

    @Override
    public String toString(){
        return name+";"+average+";"+counter;
    }
}

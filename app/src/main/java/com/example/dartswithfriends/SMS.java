package com.example.dartswithfriends;

public class SMS {
    String msg;
    String number;

    public SMS(String msg, String number){
        this.msg = msg;
        this.number = number;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return msg + ";" + number;
    }
}

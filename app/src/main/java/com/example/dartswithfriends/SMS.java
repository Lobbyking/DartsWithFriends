package com.example.dartswithfriends;

public class SMS {
    String msg;
    String number;
    boolean note;

    public SMS(String msg, String number, boolean note){
        this.msg = msg;
        this.number = number;
        this.note = note;
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

    public boolean isNote() {
        return note;
    }

    public void setNote(boolean note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return msg + ";" + number + ";" + note;
    }
}

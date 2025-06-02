package com.example.cafe_harmony;

public class Message {
    public String sender;
    public String text;
    public String time;

    public Message() {} // Needed for Firebase

    public Message(String sender, String text, String time) {
        this.sender = sender;
        this.text = text;
        this.time = time;
    }
}


package com.app.abcdapp.chat.fcmmodels;

public class Sender {
    private final Data data;
    private final String to;

    public Sender(Data data, String to) {
        this.data = data;
        this.to = to;
    }
}

package com.app.abcdapp.chat.models;


import java.io.Serializable;

public class ChatSupport implements Serializable {
    private boolean support;

    public boolean isSupport() {
        return support;
    }

    public void setSupport(boolean support) {
        this.support = support;
    }
}

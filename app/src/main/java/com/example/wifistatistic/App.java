package com.example.wifistatistic;

import android.app.Application;

public class App extends Application {
    private String history;

    public void setHistory(String history){
        this.history = history;
    }

    public String getHistory(){
        return history;
    }

}

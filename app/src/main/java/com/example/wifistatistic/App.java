package com.example.wifistatistic;

import android.app.Application;

public class App extends Application {
    private String history;
    public static App mInstance;

    public void setHistory(String history){
        this.history = history;
    }

    public String getHistory(){
        return history;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}

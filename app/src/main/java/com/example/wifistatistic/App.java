package com.example.wifistatistic;

import android.app.Application;

import com.example.wifistatistic.Classes.Area;
import com.example.wifistatistic.Classes.ObserverPoint;

import java.util.ArrayList;

public class App extends Application {
    private String history;
    private Area mArea;
    public static App mInstance;
    private ArrayList<ObserverPoint> observerPoints;

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
        mArea = new Area();
    }

    public ArrayList<ObserverPoint> getObserverPoints() {
        return observerPoints;
    }

    public void setObserverPoints(ArrayList<ObserverPoint> observerPoints) {
        this.observerPoints = observerPoints;
    }

    public Area getmArea() {
        return mArea;
    }

    public void setmArea(Area mArea) {
        this.mArea = mArea;
    }
}

package com.example.wifistatistic.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class OneMeasureObserverPoint implements Parcelable {
    private String name;
    private String logFile;
    private ArrayList<WiFiPoint> points;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public ArrayList<WiFiPoint> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<WiFiPoint> points) {
        this.points = points;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.logFile);
        dest.writeTypedList(this.points);
    }

    public OneMeasureObserverPoint() {
    }

    protected OneMeasureObserverPoint(Parcel in) {
        this.name = in.readString();
        this.logFile = in.readString();
        this.points = in.createTypedArrayList(WiFiPoint.CREATOR);
    }

    public static final Parcelable.Creator<OneMeasureObserverPoint> CREATOR = new Parcelable.Creator<OneMeasureObserverPoint>() {
        @Override
        public OneMeasureObserverPoint createFromParcel(Parcel source) {
            return new OneMeasureObserverPoint(source);
        }

        @Override
        public OneMeasureObserverPoint[] newArray(int size) {
            return new OneMeasureObserverPoint[size];
        }
    };
}

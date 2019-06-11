package com.example.wifistatistic.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ObserverPoint implements Parcelable {
    private String name;
    private String logFile;
    private ArrayList<Measurement> measurements;

    public ArrayList<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(ArrayList<Measurement> measurements) {
        this.measurements = measurements;
    }

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

    public ObserverPoint() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.logFile);
        dest.writeList(this.measurements);
    }

    protected ObserverPoint(Parcel in) {
        this.name = in.readString();
        this.logFile = in.readString();
        this.measurements = new ArrayList<Measurement>();
        in.readList(this.measurements, Measurement.class.getClassLoader());
    }

    public static final Creator<ObserverPoint> CREATOR = new Creator<ObserverPoint>() {
        @Override
        public ObserverPoint createFromParcel(Parcel source) {
            return new ObserverPoint(source);
        }

        @Override
        public ObserverPoint[] newArray(int size) {
            return new ObserverPoint[size];
        }
    };
}

package com.example.wifistatistic.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class OneMeasure implements Parcelable {
    private int number;
    private ArrayList<OneMeasureObserverPoint> oneMeasureObserverPoints;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ArrayList<OneMeasureObserverPoint> getOneMeasureObserverPoints() {
        return oneMeasureObserverPoints;
    }

    public void setOneMeasureObserverPoints(ArrayList<OneMeasureObserverPoint> oneMeasureObserverPoints) {
        this.oneMeasureObserverPoints = oneMeasureObserverPoints;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.number);
        dest.writeTypedList(this.oneMeasureObserverPoints);
    }

    public OneMeasure() {
    }

    protected OneMeasure(Parcel in) {
        this.number = in.readInt();
        this.oneMeasureObserverPoints = in.createTypedArrayList(OneMeasureObserverPoint.CREATOR);
    }

    public static final Parcelable.Creator<OneMeasure> CREATOR = new Parcelable.Creator<OneMeasure>() {
        @Override
        public OneMeasure createFromParcel(Parcel source) {
            return new OneMeasure(source);
        }

        @Override
        public OneMeasure[] newArray(int size) {
            return new OneMeasure[size];
        }
    };
}

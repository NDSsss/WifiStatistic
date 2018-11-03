package com.example.wifistatistic.Classes;

import com.example.wifistatistic.R;
import com.example.wifistatistic.Utils.FieldConverter;

import java.util.ArrayList;
import java.util.List;

public class Measurement {
    private int index;
    private List<WiFiPoint> points;

    public Measurement(){
        points = new ArrayList<>();
    }

    public List<WiFiPoint> getPoints() {
        return points;
    }

    public boolean setPoints(String data){
        WiFiPoint point;
        String[] pointsString = data.split(FieldConverter.getString(R.string.devider));
        for(int i = 0; i<pointsString.length-1;i++){
            point = new WiFiPoint();
            if(point.setPoint(pointsString[i])) {
                points.add(point);
            }else {
                return false;
            }
        }
        return true;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}

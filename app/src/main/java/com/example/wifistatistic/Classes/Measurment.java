package com.example.wifistatistic.Classes;

import com.example.wifistatistic.R;
import com.example.wifistatistic.Utils.FieldConverter;

import java.util.List;

public class Measurment {
    List<WiFiPoint> points;

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
}

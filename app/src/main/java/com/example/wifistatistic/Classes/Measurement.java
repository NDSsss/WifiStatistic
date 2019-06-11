package com.example.wifistatistic.Classes;

import android.util.Log;

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
        String[] tempForSplit = data.split("Security");
        String clearData = "";
        if(tempForSplit.length>1) {
            clearData =data.split("Security")[1];
        } else {
            clearData =data.split("Security")[0];
        }
        String[] pointsString = clearData.split("qqqqq");
        for(int i = 0; i<pointsString.length-1;i++){
            point = new WiFiPoint();
            if(point.setPoint(pointsString[i])) {
                points.add(point);
            }else {
                return false;
            }
        }
        try {
            index = Integer.parseInt(pointsString[pointsString.length-1]);
        } catch (Exception e){
            Log.e("Measurement", "setPoints: ", e);
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

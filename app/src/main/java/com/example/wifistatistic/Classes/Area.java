package com.example.wifistatistic.Classes;

import java.util.ArrayList;

public class Area {

    private int measurments;
    private ArrayList<ObserverPoint> observerPoints;

    public Area(){
        this.measurments = 0;
        observerPoints = new ArrayList<>();
    }

    public ArrayList<ObserverPoint> getObserverPoints() {
        return observerPoints;
    }

    public void setObserverPoints(ArrayList<ObserverPoint> observerPoints) {
        this.observerPoints = observerPoints;
    }

    public void addObserverPoint(ObserverPoint observerPoint){
        if(observerPoints.size() == 0){
            measurments = observerPoint.getMeasurements().size();
        } else {
            if(observerPoint.getMeasurements().size() < measurments){
                measurments = observerPoint.getMeasurements().size();
            }
        }
        observerPoints.add(observerPoint);
    }

    public OneMeasure getOneMeasure(int number){
        if(number<measurments) {
            OneMeasure oneMeasure = new OneMeasure();
            oneMeasure.setNumber(number);
            ArrayList<OneMeasureObserverPoint> oneMeasureObserverPoints = new ArrayList<>();
            for(ObserverPoint observerPoint: observerPoints){
                OneMeasureObserverPoint oneObsPoint = new OneMeasureObserverPoint();
                oneObsPoint.setName(observerPoint.getName());
                oneObsPoint.setLogFile(observerPoint.getLogFile());
                oneObsPoint.setPoints(new ArrayList<WiFiPoint>(observerPoint.getMeasurements().get(number).getPoints()));
                oneMeasureObserverPoints.add(oneObsPoint);
            }
            oneMeasure.setOneMeasureObserverPoints(oneMeasureObserverPoints);
            return oneMeasure;
        } else{
            return null;
        }
    }

    public int getMeasurments() {
        return measurments;
    }

    public void setMeasurments(int measurments) {
        this.measurments = measurments;
    }
}

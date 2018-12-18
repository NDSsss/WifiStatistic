package com.example.wifistatistic.Classes;

public class WiFiPoint {
    public static final int WIFI_POINT_STATS_COUNT = 11;
    private String timeStamp;
    private String ssid;
    private String bssid;
    private String strengh;
    private String primaryChannel;
    private String primaryFrequency;
    private String centerChannel;
    private String centerFrequency;
    private String range;
    private String distance;
    private String securuty;
    private int timesUsed = 0;

    public int getTimesUsed() {
        return timesUsed;
    }

    public void setTimesUsed(int timesUsed) {
        this.timesUsed = timesUsed;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getStrengh() {
        return strengh;
    }

    public void setStrengh(String strengh) {
        this.strengh = strengh;
    }

    public String getPrimaryChannel() {
        return primaryChannel;
    }

    public void setPrimaryChannel(String primaryChannel) {
        this.primaryChannel = primaryChannel;
    }

    public String getPrimaryFrequency() {
        return primaryFrequency;
    }

    public void setPrimaryFrequency(String primaryFrequency) {
        this.primaryFrequency = primaryFrequency;
    }

    public String getCenterChannel() {
        return centerChannel;
    }

    public void setCenterChannel(String centerChannel) {
        this.centerChannel = centerChannel;
    }

    public String getCenterFrequency() {
        return centerFrequency;
    }

    public void setCenterFrequency(String centerFrequency) {
        this.centerFrequency = centerFrequency;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSecuruty() {
        return securuty;
    }

    public void setSecuruty(String securuty) {
        this.securuty = securuty;
    }

    public void incrementTimeUsed(){
        timesUsed++;
    }

    public boolean setPoint(String data) {
        String[] stats = data.split("\\|");
//        Time Stamp|SSID|BSSID|Strength|Primary Channel|Primary Frequency|Center Channel|Center Frequency|Width (Range)|Distance|Security
        if (stats.length == WIFI_POINT_STATS_COUNT) {
            timeStamp = stats[0];
            ssid = stats[1];
            bssid = stats[2];
            strengh = stats[3];
            primaryChannel = stats[4];
            primaryFrequency = stats[5];
            centerChannel = stats[6];
            centerFrequency = stats[7];
            range = stats[8];
            distance = stats[9];
            securuty = stats[10];
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return ssid.equals(((WiFiPoint)obj).ssid);
    }
}

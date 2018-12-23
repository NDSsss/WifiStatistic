package com.example.wifistatistic.Classes;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class WiFiPoint implements Parcelable, Comparable<WiFiPoint> {
    public static final int WIFI_POINT_STATS_COUNT = 11;
    public static final int TYPE_UNDEFINED = -1;
    public static final int TYPE_STATIONARY = 0;
    public static final int TYPE_MOBILE = 1;
    private String timeStamp;
    private String ssid;
    private String bssid;
    private String strengh;
    private int primaryChannel;
    private String primaryFrequency;
    private int centerChannel;
    private String centerFrequency;
    private String range;
    private String distance;
    private String securuty;
    private int timesUsed = 0;
    private int type=TYPE_UNDEFINED;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public int getPrimaryChannel() {
        return primaryChannel;
    }

    public void setPrimaryChannel(int primaryChannel) {
        this.primaryChannel = primaryChannel;
    }

    public String getPrimaryFrequency() {
        return primaryFrequency;
    }

    public void setPrimaryFrequency(String primaryFrequency) {
        this.primaryFrequency = primaryFrequency;
    }

    public int getCenterChannel() {
        return centerChannel;
    }

    public void setCenterChannel(int centerChannel) {
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

    public void incrementTimeUsed() {
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
            primaryChannel = Integer.valueOf(stats[4]);
            primaryFrequency = stats[5];
            centerChannel = Integer.valueOf(stats[6]);
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
        return ssid.equals(((WiFiPoint) obj).ssid);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.timeStamp);
        dest.writeString(this.ssid);
        dest.writeString(this.bssid);
        dest.writeString(this.strengh);
        dest.writeInt(this.primaryChannel);
        dest.writeString(this.primaryFrequency);
        dest.writeInt(this.centerChannel);
        dest.writeString(this.centerFrequency);
        dest.writeString(this.range);
        dest.writeString(this.distance);
        dest.writeString(this.securuty);
        dest.writeInt(this.timesUsed);
        dest.writeInt(this.type);
    }

    public WiFiPoint() {
    }

    protected WiFiPoint(Parcel in) {
        this.timeStamp = in.readString();
        this.ssid = in.readString();
        this.bssid = in.readString();
        this.strengh = in.readString();
        this.primaryChannel = in.readInt();
        this.primaryFrequency = in.readString();
        this.centerChannel = in.readInt();
        this.centerFrequency = in.readString();
        this.range = in.readString();
        this.distance = in.readString();
        this.securuty = in.readString();
        this.timesUsed = in.readInt();
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<WiFiPoint> CREATOR = new Parcelable.Creator<WiFiPoint>() {
        @Override
        public WiFiPoint createFromParcel(Parcel source) {
            return new WiFiPoint(source);
        }

        @Override
        public WiFiPoint[] newArray(int size) {
            return new WiFiPoint[size];
        }
    };

    @Override
    public int compareTo(@NonNull WiFiPoint o) {
        if (o.timesUsed > timesUsed) {
            return 1;
        } else if (o.timesUsed < timesUsed) {
            return -1;
        } else {
            return 0;
        }
    }
}

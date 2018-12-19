package com.example.wifistatistic;

public interface IMainProgress {
    void startprogress();
    void completeProgress();
    void errorProgress(String message);
}

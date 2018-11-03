package com.example.wifistatistic;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.wifistatistic.drawer.DrawerFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ITakeStatistic {

    Toolbar toolbar;
    String mStatistic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void setStat(String text) {
        mStatistic = text;
    }

    @Override
    public String getStat() {
        return mStatistic==null?"":mStatistic;
    }

    @Override
    protected void onResume() {
        DrawerFragment drawer;
        super.onResume();
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(int i = 0; i < fragments.size(); i++){
         if(fragments.get(i) instanceof DrawerFragment){
             drawer = (DrawerFragment) fragments.get(i);
             drawer.showInitScreen();
         }
        }

    }
}

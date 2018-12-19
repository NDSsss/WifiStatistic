package com.example.wifistatistic;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.wifistatistic.Filter.FilterFragment;
import com.example.wifistatistic.drawer.DrawerFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ITakeStatistic,IMainProgress {

    Toolbar toolbar;
    String mStatistic;
    private FilterFragment filterFragment;
    private BottomNavigationView bnv;
    private RelativeLayout rvProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bnv = (BottomNavigationView)findViewById(R.id.bnv_main);
        rvProgress = (RelativeLayout) findViewById(R.id.rl_progress_main);
        filterFragment = new FilterFragment();
        filterFragment.setTakeStatisticCallBack(this);
        filterFragment.setmMainProgress(this);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_stat:
                        openFragment(filterFragment);
                        break;
                    case R.id.bottom_stat_share:
                        return false;

                }
                return true;
            }
        });
        bnv.setSelectedItemId(R.id.bottom_stat);
    }

    private void openFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container_main,fragment).commit();
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
//        DrawerFragment drawer;
        super.onResume();
//        List<Fragment> fragments = getSupportFragmentManager().getFragments();
//        for(int i = 0; i < fragments.size(); i++){
//         if(fragments.get(i) instanceof DrawerFragment){
//             drawer = (DrawerFragment) fragments.get(i);
//             drawer.showInitScreen();
//         }
//        }

    }

    @Override
    public void startprogress() {
        rvProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void completeProgress() {
        rvProgress.setVisibility(View.GONE);
    }

    @Override
    public void errorProgress(String message) {
        rvProgress.setVisibility(View.GONE);
        Dialog dialog = new Dialog(this);
        dialog.setTitle(message);
        dialog.show();
    }
}

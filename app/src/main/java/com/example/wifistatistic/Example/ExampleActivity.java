package com.example.wifistatistic.Example;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.wifistatistic.R;

public class ExampleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openFragment();
    }

    private void openFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ExampleFragment()).commitAllowingStateLoss();
    }
}

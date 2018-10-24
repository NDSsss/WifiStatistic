package com.example.wifistatistic.drawer;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.wifistatistic.Adding.AddingFragment;
import com.example.wifistatistic.Filter.FilterFragment;
import com.example.wifistatistic.ITakeStatistic;
import com.example.wifistatistic.R;

public class DrawerFragment extends Fragment implements View.OnClickListener,ITakeStatistic {

    Button btnAdding;
    Button btnFilter;
    String mStatisitic;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_menu,container,false);
        btnAdding = (Button) view.findViewById(R.id.btn_drawer_ading);
        btnFilter = (Button) view.findViewById(R.id.btn_drawer_filter);
        btnAdding.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_drawer_ading:
                getFragmentManager().beginTransaction().replace(R.id.container,new AddingFragment(this)).commitAllowingStateLoss();
                break;
            case R.id.btn_drawer_filter:
                getFragmentManager().beginTransaction().replace(R.id.container,new FilterFragment(this)).commitAllowingStateLoss();
                break;
        }

//        Intent intent = new Intent(getContext(),ExampleActivity.class);
//        startActivity(intent);
//        v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    public void setStat(String text) {
        mStatisitic = text;
    }

    @Override
    public String getStat() {
        return mStatisitic;
    }
}

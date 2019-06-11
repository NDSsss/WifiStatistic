package com.example.wifistatistic.fastest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.wifistatistic.App;
import com.example.wifistatistic.Classes.Area;
import com.example.wifistatistic.Classes.ObserverPoint;
import com.example.wifistatistic.Classes.OneMeasure;
import com.example.wifistatistic.Classes.OneMeasureObserverPoint;
import com.example.wifistatistic.Classes.WiFiPoint;
import com.example.wifistatistic.R;

import java.util.ArrayList;
import java.util.List;

public class FastestFragment extends Fragment {

    private static final String TAG = "FastestFragment";

    private Spinner spMEasure;
    private LinearLayout llPointsContainer;
    private ArrayAdapter measureAdapter;
    private Area mArea;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fastest, container, false);
        spMEasure = view.findViewById(R.id.sp_fragment_fastest_measure);
        llPointsContainer = view.findViewById(R.id.ll_fragment_fastest_points_container);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");

        spMEasure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                measureSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSpinner(){
        this.mArea = App.mInstance.getmArea();
        ArrayList<String> names = new ArrayList<>();
        for(int i = 0; i < mArea.getMeasurments(); i++){
            names.add("Measure "+i);
        }
        spMEasure.setAdapter(new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,names));
    }

    @Override
    public void onResume() {
        super.onResume();
        initSpinner();
    }

    private void measureSelected(int number){
        OneMeasure measure = mArea.getOneMeasure(number);
        llPointsContainer.removeAllViews();
        for(OneMeasureObserverPoint oneMeasureObserverPoint: measure.getOneMeasureObserverPoints()){
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_fastest_point, null);
            TextView textView = view.findViewById(R.id.tv_view_fastest_point_values);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(oneMeasureObserverPoint.getName());
            for(WiFiPoint point: oneMeasureObserverPoint.getPoints()){
                stringBuilder.append("\n"+point.getSsid());
            }
            textView.setText(stringBuilder.toString());
            llPointsContainer.addView(view);
        }
    }
}

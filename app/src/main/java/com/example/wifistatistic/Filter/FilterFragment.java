package com.example.wifistatistic.Filter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wifistatistic.Classes.Measurement;
import com.example.wifistatistic.Classes.WiFiPoint;
import com.example.wifistatistic.ITakeStatistic;
import com.example.wifistatistic.R;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment {
    ITakeStatistic mTakeStatistic;
    List<Measurement> mMeasurements;
    RecyclerView rvPoints;
    FilterAdapter filterAdapter;
    ArrayList<WiFiPoint> uniquePoints;

    public FilterFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ITakeStatistic) {
            mTakeStatistic = (ITakeStatistic) context;
            uniquePoints = new ArrayList<>();
            filter();
        }else{
            Toast.makeText(context,"NoListener",Toast.LENGTH_LONG);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        rvPoints = (RecyclerView) view.findViewById(R.id.rv_filtred_points);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPoints.setLayoutManager(linearLayoutManager);
        filterAdapter = new FilterAdapter(uniquePoints);
        rvPoints.setAdapter(filterAdapter);
    }

    public void filter(){
        Measurement measure;
        mMeasurements = new ArrayList<>();
        String[] rawMeasurements = mTakeStatistic.getStat().split("---");
        for (int i = 0; i < rawMeasurements.length; i++){
            measure = new Measurement();
            if(!measure.setPoints(rawMeasurements[i])){
                break;
            } else {
                mMeasurements.add(measure);
            }
        }
        for (int i =0; i<mMeasurements.size(); i++){
            for(int j = 0; j < mMeasurements.get(i).getPoints().size();j++) {
                if (!uniquePoints.contains(mMeasurements.get(i).getPoints().get(j))){
                    uniquePoints.add(mMeasurements.get(i).getPoints().get(j));
                } else {
                    uniquePoints.get(uniquePoints.indexOf(mMeasurements.get(i).getPoints().get(j))).incrementTimeUsed();
                }
            }
        }
    }

}

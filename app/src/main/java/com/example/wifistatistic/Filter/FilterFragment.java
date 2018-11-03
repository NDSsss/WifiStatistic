package com.example.wifistatistic.Filter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifistatistic.Classes.Measurement;
import com.example.wifistatistic.ITakeStatistic;
import com.example.wifistatistic.R;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment {
    TextView tvExample;
    Button btnFilter;
    ITakeStatistic mTakeStatistic;
    List<Measurement> mMeasurements;

    public FilterFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ITakeStatistic) {
            mTakeStatistic = (ITakeStatistic) context;
        }else{
            Toast.makeText(context,"NoListener",Toast.LENGTH_LONG);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        tvExample = (TextView) view.findViewById(R.id.et_filter);
        btnFilter = (Button) view.findViewById(R.id.btn_filter_filter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(v);
            }
        });
        return view;
    }

    public void filter(View view){
        Measurement measure;
        mMeasurements = new ArrayList<>();
        String[] rawMeasurements = mTakeStatistic.getStat().split("---");
        for (int i = 0; i < rawMeasurements.length; i++){
            measure = new Measurement();
            if(!measure.setPoints(rawMeasurements[i])){
                break;
            }
        }
        tvExample.setText(String.valueOf(mMeasurements.size()));
    }

}

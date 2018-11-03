package com.example.wifistatistic.Filter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifistatistic.ITakeStatistic;
import com.example.wifistatistic.R;

public class FilterFragment extends Fragment {
    TextView tvExample;
    ITakeStatistic mTakeStatistic;

    public FilterFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
        tvExample.setText(mTakeStatistic.getStat());
        return view;
    }
}

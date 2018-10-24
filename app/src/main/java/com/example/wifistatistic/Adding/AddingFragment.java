package com.example.wifistatistic.Adding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.wifistatistic.ITakeStatistic;
import com.example.wifistatistic.R;

public class AddingFragment extends Fragment implements View.OnClickListener, ITakeStatistic {

    EditText etInputPath;
    Button btnChoose;
    ITakeStatistic mTakeStatistic;

    public AddingFragment(){

    }

    public AddingFragment(ITakeStatistic takeStatistic){
        mTakeStatistic = takeStatistic;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adding,container,false);
        etInputPath = (EditText) view.findViewById(R.id.et_adding_path);
        btnChoose = (Button) view.findViewById(R.id.btn_adding_choose);
        btnChoose.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        mTakeStatistic.setStat(etInputPath.getText().toString());
    }

    @Override
    public void setStat(String text) {

    }

    @Override
    public String getStat() {
        return null;
    }
}
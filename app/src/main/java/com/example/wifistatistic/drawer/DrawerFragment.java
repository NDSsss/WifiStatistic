package com.example.wifistatistic.drawer;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wifistatistic.Example.ExampleActivity;
import com.example.wifistatistic.Example.ExampleFragment;
import com.example.wifistatistic.R;

public class DrawerFragment extends Fragment implements View.OnClickListener {

    Button someActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_menu,container,false);
        someActivity = (Button) view.findViewById(R.id.drawer_menu_some_button);
        someActivity.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        getFragmentManager().beginTransaction().replace(R.id.container,new ExampleFragment()).commitAllowingStateLoss();
//        Intent intent = new Intent(getContext(),ExampleActivity.class);
//        startActivity(intent);
//        v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }
}

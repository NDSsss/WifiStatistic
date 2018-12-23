package com.example.wifistatistic.Filter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifistatistic.Classes.Measurement;
import com.example.wifistatistic.Classes.WiFiPoint;
import com.example.wifistatistic.IMainProgress;
import com.example.wifistatistic.ITakeStatistic;
import com.example.wifistatistic.R;
import com.example.wifistatistic.filepickerdialog.FilePickerDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FilterFragment extends Fragment implements FilePickerDialog.OnFilePick {
    public static final String UNIQUE_POINTS = "UNIQUE_POINTS";
    public static final String COUNT_POINTS = "COUNT_POINTS";
    public static final String CHANNEL_POINTS = "CHANNEL_POINTS";
    private ITakeStatistic mTakeStatistic;
    private IMainProgress mMainProgress;
    private List<Measurement> mMeasurements;
    private RecyclerView rvPoints,rvPointsByCount,rvPointsByChannel;
    private FilterAdapter filterAdapter,filterAdapterByCount,filterAdapterByChannel;
    private ArrayList<WiFiPoint> uniquePoints,pointsFiltredByCount,pointsFiltredByChannel;
    private TextView tvChooseFile;
    private FilePickerDialog dialog;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String TAG = "FilterFragment";
    private Handler handler;
    private TabHost tabHost;


    public FilterFragment() {
    }

    public void setTakeStatisticCallBack(ITakeStatistic takeStatisticCallBack){
        mTakeStatistic = takeStatisticCallBack;
    }

    public void setmMainProgress(IMainProgress mMainProgress) {
        this.mMainProgress = mMainProgress;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        rvPoints = (RecyclerView) view.findViewById(R.id.rv_filtred_points);
        rvPointsByCount = (RecyclerView) view.findViewById(R.id.rv_filtred_points_by_count);
        rvPointsByChannel = (RecyclerView) view.findViewById(R.id.rv_filtred_points_by_channel);
        tvChooseFile = (TextView) view.findViewById(R.id.tv_filter_choose_file);
        tvChooseFile.setOnClickListener(v->chooseFile());
        tabHost = view.findViewById(R.id.th_fragment_filter);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec(UNIQUE_POINTS);

        tabSpec.setContent(R.id.rv_filtred_points);
        tabSpec.setIndicator("Все");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(COUNT_POINTS);
        tabSpec.setContent(R.id.rv_filtred_points_by_count);
        tabSpec.setIndicator("По количеству");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(CHANNEL_POINTS);
        tabSpec.setContent(R.id.rv_filtred_points_by_channel);
        tabSpec.setIndicator("По ширине канала");
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);
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
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPointsByCount.setLayoutManager(linearLayoutManager2);
        filterAdapterByCount = new FilterAdapter(pointsFiltredByCount);
        rvPointsByCount.setAdapter(filterAdapterByCount);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPointsByChannel.setLayoutManager(linearLayoutManager3);
        filterAdapterByChannel = new FilterAdapter(pointsFiltredByChannel);
        rvPointsByChannel.setAdapter(filterAdapterByChannel);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                filterAdapter.setPoints(uniquePoints);
                filterAdapterByCount.setPoints(pointsFiltredByCount);
                filterAdapterByChannel.setPoints(pointsFiltredByChannel);
                setTime();
                mMainProgress.completeProgress();
            }
        };
    }

    private void setTime(){
        String time = mMeasurements.get(0).getPoints().get(0).getTimeStamp().concat("\n")
                .concat(mMeasurements.get(mMeasurements.size()-1).getPoints().get(0).getTimeStamp());
        tvChooseFile.setText(time);
    }

    private void chooseFile(){
        verifyStoragePermissions(getActivity());
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()+"/wifiAnalyzer");
        if(!file.exists()){
            file.mkdir();
        }
        dialog = new FilePickerDialog(getContext(),file,this::fileClicked);
        dialog.show();
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void filter(){
        Measurement measure;
        mMeasurements = new ArrayList<>();
        uniquePoints = new ArrayList<>();
        String[] rawMeasurements = mTakeStatistic.getStat().replaceAll("\n","").split("---");
        for (int i = 0; i < rawMeasurements.length; i++){
            measure = new Measurement();
            if(!measure.setPoints(rawMeasurements[i])){
                break;
            } else {
                if(measure.getPoints()!=null&&measure.getPoints().size()>0) {
                    mMeasurements.add(measure);
                }
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
        Collections.sort(uniquePoints);
        pointsFiltredByCount = new ArrayList<>();
        cloneList(uniquePoints,pointsFiltredByCount);
        int enoughForStationary = mMeasurements.size()/3*2;
        for (int i = 0; i < pointsFiltredByCount.size(); i++){
            if(pointsFiltredByCount.get(i).getTimesUsed()>=enoughForStationary){
                pointsFiltredByCount.get(i).setType(WiFiPoint.TYPE_STATIONARY);
            } else {
                pointsFiltredByCount.get(i).setType(WiFiPoint.TYPE_MOBILE);
            }
        }
        pointsFiltredByChannel = new ArrayList<>();
        cloneList(uniquePoints,pointsFiltredByChannel);
        for(int i = 0; i < pointsFiltredByChannel.size(); i++){
            if(pointsFiltredByChannel.get(i).getPrimaryChannel()-pointsFiltredByChannel.get(i).getCenterChannel()>=2){
                pointsFiltredByChannel.get(i).setType(WiFiPoint.TYPE_STATIONARY);
            } else {
                pointsFiltredByChannel.get(i).setType(WiFiPoint.TYPE_MOBILE);
            }
        }
        Collections.sort(pointsFiltredByChannel, new Comparator<WiFiPoint>() {
            @Override
            public int compare(WiFiPoint o1, WiFiPoint o2) {
                if(o1.getType()<o2.getType()){
                    return -1;
                } else if(o1.getType()>o2.getType()){
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }

    private void cloneList(ArrayList<WiFiPoint> original, ArrayList<WiFiPoint> copied){
        for(int i = 0; i < original.size(); i++){
            WiFiPoint npoint = new WiFiPoint();
            WiFiPoint oldPoint = original.get(i);
            npoint.setTimeStamp(oldPoint.getTimeStamp());
            npoint.setSsid(oldPoint.getSsid());
            npoint.setBssid(oldPoint.getBssid());
            npoint.setStrengh(oldPoint.getStrengh());
            npoint.setPrimaryChannel(oldPoint.getPrimaryChannel());
            npoint.setPrimaryFrequency(oldPoint.getPrimaryFrequency());
            npoint.setCenterChannel(oldPoint.getCenterChannel());
            npoint.setCenterFrequency(oldPoint.getCenterFrequency());
            npoint.setRange(oldPoint.getRange());
            npoint.setDistance(oldPoint.getDistance());
            npoint.setSecuruty(oldPoint.getSecuruty());
            npoint.setTimesUsed(oldPoint.getTimesUsed());
            npoint.setType(oldPoint.getType());
            copied.add(npoint);

        }
    }


    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    @Override
    public void fileClicked(File pickedFile) {
        dialog.dismiss();
        verifyStoragePermissions(getActivity());
        try {
            mTakeStatistic.setStat(getStringFromFile(pickedFile.getAbsolutePath()));
        } catch (IOException e){
            Log.d(TAG, "onActivityResult: parsing error");
            Dialog dialog = new Dialog(getContext());
            dialog.setTitle(e.getLocalizedMessage());
            dialog.show();
            return;
        }catch (Exception e){
            Log.d(TAG, "onActivityResult: parsingeror");
            Dialog dialog = new Dialog(getContext());
            dialog.setTitle(e.getLocalizedMessage());
            dialog.show();
            return;
        }
        mMainProgress.startprogress();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                filter();
                handler.sendEmptyMessage(1);
            }
        };
        new Thread(runnable).start();

    }
}

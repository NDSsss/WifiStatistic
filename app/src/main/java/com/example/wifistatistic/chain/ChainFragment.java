package com.example.wifistatistic.chain;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wifistatistic.Classes.Measurement;
import com.example.wifistatistic.Classes.ObserverPoint;
import com.example.wifistatistic.Classes.WiFiPoint;
import com.example.wifistatistic.IMainProgress;
import com.example.wifistatistic.R;
import com.example.wifistatistic.filepickerdialog.FilePickerDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ChainFragment extends Fragment {

    private static final String TAG = "ChainFragment";

    private static final int MESSAGE_POINT_ADDED = 1;
    private static final int MESSAGE_RESULTS_COMPLETE = 2;

    private RecyclerView rvChain;
    private ChainAdapter adapter;
    private ArrayList<ObserverPoint> points;
    private FilePickerDialog dialog;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Handler handler;
    private IMainProgress mMainProgress;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chain, container,false);
        rvChain = view.findViewById(R.id.rv_fragment_chain);
        swipeRefreshLayout = view.findViewById(R.id.srl_fragment_chain_results);
        tvResult = view.findViewById(R.id.tv_fragment_chain_results);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reresh();
            }
        });
        points = new ArrayList<>();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.arg1){
                    case MESSAGE_POINT_ADDED:
                        adapter.setPoints(points);
                        mMainProgress.completeProgress();
                        break;
                    case MESSAGE_RESULTS_COMPLETE:
                        tvResult.setText((String)msg.obj);
                        swipeRefreshLayout.setRefreshing(false);
                        mMainProgress.completeProgress();
                        break;
                }
            }
        };
        rvChain.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChainAdapter(points, this::addPoint);
        rvChain.setAdapter(adapter);
    }

    public void setmMainProgress(IMainProgress mMainProgress) {
        this.mMainProgress = mMainProgress;
    }

    private void addPoint(){
        chooseFile();
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
    public void fileClicked(File pickedFile) {
        dialog.dismiss();
        verifyStoragePermissions(getActivity());
        String rawData;
        try {
            rawData = getStringFromFile(pickedFile.getAbsolutePath());
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
                createObserverPointFromRaw(rawData,pickedFile.getName());
                Message message = new Message();
                message.arg1 = MESSAGE_POINT_ADDED;
                handler.sendMessage(message);
            }
        };
        new Thread(runnable).start();

    }

    private void createObserverPointFromRaw(String rawData, String fileName){
        Measurement measure;
        ArrayList<Measurement> mMeasurements = new ArrayList<>();
        String[] rawMeasurements = rawData.replaceAll("\n","").split("---");
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
        ObserverPoint point = new ObserverPoint();
        point.setMeasurements(mMeasurements);
        point.setName("Point "+points.size());
        point.setLogFile(fileName);
        points.add(point);
    }

    private void reresh(){
        mMainProgress.startprogress();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.arg1 = MESSAGE_RESULTS_COMPLETE;
                message.obj = getChainResult();
                handler.sendMessage(message);
            }
        };
        new Thread(runnable).start();
    }

    private String getChainResult(){
        if(points.size()>1) {
            int minLenth = points.get(0).getMeasurements().size();
            for (ObserverPoint point : points) {
                if (point.getMeasurements().size()< minLenth){
                    minLenth = point.getMeasurements().size();
                }
            }
            ArrayList<String> reslts = new ArrayList<>();
            StringBuilder currentResult = new StringBuilder();
            for(int i = 0; i < minLenth; i++){
                currentResult.append("Measurment "+i+"\n");
                ArrayList<ArrayList<WiFiPoint>> chainPoints = new ArrayList<>();
                for(int j = 0; j < points.size()-1; j++){
                    ArrayList<WiFiPoint> commonPoints = new ArrayList<>();
                    for(WiFiPoint pontOne: points.get(j).getMeasurements().get(i).getPoints()){
                        addPointToListIfNotExist(points.get(j+1).getMeasurements().get(i).getPoints(), pontOne, commonPoints);
                    }
                    chainPoints.add(commonPoints);
                    currentResult.append(points.get(j).getName() + " and "+points.get(j+1).getName()+" common points: ");
                    for(WiFiPoint commonPoint: commonPoints){
                        currentResult.append(commonPoint.getSsid() +", ");
                    }
                    currentResult.append("\n");
                }

            }
            return currentResult.toString();
        } else {
            return "null";
        }
    }
    private void checkChain(ArrayList<ArrayList<WiFiPoint>> commonPoints){

    }

    private void addPointToListIfNotExist(List<WiFiPoint> points, WiFiPoint searchPoint, ArrayList<WiFiPoint> commonPoints){
//        boolean isExist = false;
        for(WiFiPoint currentPoint: points){
            if(currentPoint.getSsid().equalsIgnoreCase(searchPoint.getSsid())){
                commonPoints.add(currentPoint);
            }
        }
//        if(!isExist){
//            commonPoints.add(searchPoint);
//        }
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
}

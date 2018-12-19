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
import java.util.List;

public class FilterFragment extends Fragment implements FilePickerDialog.OnFilePick {
    public static final String UNIQUE_POINTS = "UNIQUE_POINTS";
    private ITakeStatistic mTakeStatistic;
    private IMainProgress mMainProgress;
    private List<Measurement> mMeasurements;
    private RecyclerView rvPoints;
    private FilterAdapter filterAdapter;
    private ArrayList<WiFiPoint> uniquePoints;
    private TextView tvChooseFile;
    private FilePickerDialog dialog;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String TAG = "FilterFragment";
    private Handler handler;


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
        tvChooseFile = (TextView) view.findViewById(R.id.tv_filter_choose_file);
        tvChooseFile.setOnClickListener(v->chooseFile());
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
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                filterAdapter.setPoints(uniquePoints);
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

    public ArrayList<WiFiPoint> filter(){
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
        return uniquePoints;
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

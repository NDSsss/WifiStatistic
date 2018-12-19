package com.example.wifistatistic.Adding;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wifistatistic.ITakeStatistic;
import com.example.wifistatistic.R;
import com.example.wifistatistic.filepickerdialog.FilePickerDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class AddingFragment extends Fragment implements View.OnClickListener, ITakeStatistic, FilePickerDialog.OnFilePick {

    public static final String TAG = "AddingFragment ";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    int PICK_REQUEST_CODE = 0;
    EditText etInputPath;
    Button btnChoose;
    ITakeStatistic mTakeStatistic;
    ArrayList<String> files;
    private FilePickerDialog dialog;

    public AddingFragment(){

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
        View view = inflater.inflate(R.layout.fragment_adding,container,false);
        etInputPath = (EditText) view.findViewById(R.id.et_adding_path);
        btnChoose = (Button) view.findViewById(R.id.btn_adding_choose);
        btnChoose.setOnClickListener(this);
//        mTakeStatistic.setStat(etInputPath.getText().toString());
        return view;
    }

    @Override
    public void onClick(View v) {
        chooseFile();
    }

    private void chooseFile(){
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("file/*");
//        startActivityForResult(intent,PICK_REQUEST_CODE);
        verifyStoragePermissions(getActivity());
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()+"/wifiAnalyzer");
        if(!file.exists()){
            file.mkdir();
        }
        dialog = new FilePickerDialog(getContext(),file,this::fileClicked);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==PICK_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                verifyStoragePermissions(getActivity());
                Uri uri = data.getData();
                String type = data.getType();
                try {
                    mTakeStatistic.setStat(getStringFromFile(uri.getPath()));
                } catch (IOException e){
                    Log.d(TAG, "onActivityResult: parsing error");
                }catch (Exception e){
                    Log.d(TAG, "onActivityResult: parsingeror");
                }
                Log.i(TAG,"Pick completed: "+ uri + " "+type);
                if (uri != null)
                {
                    String path = uri.toString();
                    if (path.toLowerCase().startsWith("file://"))
                    {
                        // Selected file/directory path is below
                        path = (new File(URI.create(path))).getAbsolutePath();
                    }

                }
            }
            else Log.i(TAG,"Back from pick with cancel status");
        }
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
    public void setStat(String text) {

    }

    @Override
    public String getStat() {
        return null;
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
        }catch (Exception e){
            Log.d(TAG, "onActivityResult: parsingeror");
            Dialog dialog = new Dialog(getContext());
            dialog.setTitle(e.getLocalizedMessage());
            dialog.show();
        }
    }
}

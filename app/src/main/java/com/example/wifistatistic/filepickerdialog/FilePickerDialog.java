package com.example.wifistatistic.filepickerdialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.wifistatistic.R;

import java.io.File;

public class FilePickerDialog extends Dialog {
    private RecyclerView rvFiles;
    private OnFilePick mFilePickListener;

    public FilePickerDialog(@NonNull Context context) {
        super(context);
    }

    public FilePickerDialog(@NonNull Context context, File directory, OnFilePick filePickListener) {
        super(context);
        mFilePickListener = filePickListener;
        init(directory);
    }

    public FilePickerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }



    private void init(File directory){
        setContentView(R.layout.dialog_file_picker);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        FIlePickerAdapter adapter = new FIlePickerAdapter(directory.listFiles(),mFilePickListener);
        rvFiles = (RecyclerView) findViewById(R.id.rv_dialog_picker);
        rvFiles.setLayoutManager(manager);
        rvFiles.setAdapter(adapter);
    }

    public interface OnFilePick{
        void fileClicked(File pickedFile);
    }
}

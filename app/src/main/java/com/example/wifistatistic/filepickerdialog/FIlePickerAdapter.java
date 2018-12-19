package com.example.wifistatistic.filepickerdialog;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wifistatistic.Filter.FilterAdapter;
import com.example.wifistatistic.R;

import java.io.File;
import java.util.ArrayList;

public class FIlePickerAdapter extends RecyclerView.Adapter<FIlePickerAdapter.ViewHolder> {

    private File[] mFiles;
    private FilePickerDialog.OnFilePick mListener;
    public FIlePickerAdapter(File[] files, FilePickerDialog.OnFilePick listener){
        mFiles = files;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_picker_dialog,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvFileName.setText(mFiles[position].getName());
    }

    @Override
    public int getItemCount() {
        return mFiles==null?0:mFiles.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvFileName;
        public ViewHolder(View itemView) {
            super(itemView);
            tvFileName = (TextView) itemView.findViewById(R.id.tv_item_file_picker_dialog);
            tvFileName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        mListener.fileClicked(mFiles[getAdapterPosition()]);
                    }
                }
            });
        }
    }
}

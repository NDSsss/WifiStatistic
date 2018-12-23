package com.example.wifistatistic.Filter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wifistatistic.Classes.WiFiPoint;
import com.example.wifistatistic.R;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private ArrayList<WiFiPoint> points;

    public FilterAdapter(ArrayList<WiFiPoint> points){
        this.points = points;
    }

    public void setPoints(ArrayList<WiFiPoint> points) {
        this.points = points;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_points,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String type = "";
        switch (points.get(position).getType()){
            case WiFiPoint.TYPE_UNDEFINED:
                type = "";
                break;
            case WiFiPoint.TYPE_STATIONARY:
                type = "S ";
                break;
            case WiFiPoint.TYPE_MOBILE:
                type = "M ";
                break;
        }
        holder.tvPoint.setText(type.concat(points.get(position).getSsid().concat(" ").concat(String.valueOf(points.get(position).getTimesUsed()))));
    }

    @Override
    public int getItemCount() {
        return points==null?0:points.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvPoint;
        public ViewHolder(View itemView) {
            super(itemView);
            tvPoint = (TextView) itemView.findViewById(R.id.tv_item_points);
        }
    }
}

package com.example.wifistatistic.chain;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wifistatistic.Classes.ObserverPoint;
import com.example.wifistatistic.R;

import java.util.ArrayList;

public class ChainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private ArrayList<ObserverPoint> points;
    private IChainAdapter iChainAdapter;

    public ChainAdapter(ArrayList<ObserverPoint> points, IChainAdapter iChainAdapter) {
        this.points = points;
        this.iChainAdapter = iChainAdapter;
    }

    public void setPoints(ArrayList<ObserverPoint> points) {
        this.points = points;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            return new HolderChainItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chain_item, parent, false));
        } else {
            return new HolderChainFooter(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chain_footer, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_ITEM) {
            HolderChainItem item = (HolderChainItem) holder;
            item.tvName.setText(points.get(position).getName());
            item.tvFile.setText(points.get(position).getLogFile());
        }
    }

    @Override
    public int getItemCount() {
        return points == null ? 1 : points.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        return position < points.size() ? TYPE_ITEM : TYPE_FOOTER;
    }

    public class HolderChainItem extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvFile;

        public HolderChainItem(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_item_chain_item_name);
            tvFile = itemView.findViewById(R.id.tv_item_chain_item_file);
        }
    }

    public class HolderChainFooter extends RecyclerView.ViewHolder {
        public Button btnAdd;

        public HolderChainFooter(View itemView) {
            super(itemView);
            btnAdd = itemView.findViewById(R.id.btn_item_chain_add);
            btnAdd.setOnClickListener(v-> iChainAdapter.addPoint());
        }
    }

    public interface IChainAdapter {
        void addPoint();
    }
}

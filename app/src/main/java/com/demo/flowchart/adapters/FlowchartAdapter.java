package com.demo.flowchart.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.flowchart.R;
import com.demo.flowchart.database.FlowchartEntity;

import java.util.List;

public class FlowchartAdapter extends RecyclerView.Adapter<FlowchartAdapter.FlowchartViewHolder> {

    FlowchartListener listener;
    List<FlowchartEntity> flowcharts;

    public FlowchartAdapter(FlowchartListener listener) {

        this.listener = listener;
    }

    public void setFlowcharts(List<FlowchartEntity> flowcharts) {
        this.flowcharts = flowcharts;
        updateAdapter();
    }

    @NonNull
    @Override
    public FlowchartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FlowchartViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flowchart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FlowchartViewHolder holder, int position) {

        holder.name.setOnClickListener(v -> listener.onFlowchartClick(flowcharts.get(position).getUid()));

        holder.deleteProject.setOnClickListener(v -> {
            listener.deleteFlowchart(flowcharts.get(position));
            flowcharts.remove(position);
            updateAdapter();
        });

        holder.bind(flowcharts.get(position));
    }

    @Override
    public int getItemCount() {
        return flowcharts.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateAdapter() {
        notifyDataSetChanged();
    }

    protected static class FlowchartViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView cloudUpload;
        ImageView deleteProject;
        FlowchartEntity flowchartEntity;


        public FlowchartViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_item_name);
            cloudUpload = itemView.findViewById(R.id.iv_item_cloud_upload);
            deleteProject = itemView.findViewById(R.id.iv_item_delete);
        }

        protected void bind(FlowchartEntity flowchartEntity) {
            this.flowchartEntity = flowchartEntity;
            name.setText(flowchartEntity.getName());
        }
    }
}

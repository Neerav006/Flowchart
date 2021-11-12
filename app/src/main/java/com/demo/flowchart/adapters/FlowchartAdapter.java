package com.demo.flowchart.adapters;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.flowchart.R;
import com.demo.flowchart.database.FlowchartEntity;
import com.demo.flowchart.drawing.BlockView;
import com.demo.flowchart.drawing.model.DrawingBlock;

import java.util.List;

public class FlowchartAdapter extends RecyclerView.Adapter<FlowchartAdapter.FlowchartViewHolder> {

    FlowchartListener listener;
    List<FlowchartEntity> flowcharts;

    public FlowchartAdapter(FlowchartListener listener) {
        this.listener = listener;
    }

    public void setFlowcharts(List<FlowchartEntity> flowcharts) {
        this.flowcharts = flowcharts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FlowchartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View flowchartView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flowchart, parent, false);
        FlowchartViewHolder viewHolder = new FlowchartViewHolder(flowchartView);
        flowchartView.setOnClickListener(v -> {
            long id = flowcharts.get(viewHolder.getAdapterPosition()).getUid();
            listener.onFlowchartClick(id);
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FlowchartViewHolder holder, int position) {
        holder.bind(flowcharts.get(position));
    }

    @Override
    public int getItemCount() {
        return flowcharts.size();
    }

    protected static class FlowchartViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public FlowchartViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_item_name);
        }

        protected void bind(FlowchartEntity flowchartEntity) {
            name.setText(flowchartEntity.getName());
        }
    }

    public interface FlowchartListener {
        void onFlowchartClick(long flowchartId);
    }
}

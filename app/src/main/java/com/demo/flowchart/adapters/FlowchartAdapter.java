package com.demo.flowchart.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.flowchart.App;
import com.demo.flowchart.R;
import com.demo.flowchart.database.FlowchartEntity;

import java.util.List;

public class FlowchartAdapter extends RecyclerView.Adapter<FlowchartAdapter.FlowchartViewHolder> {

    FlowchartListener listener;
    List<FlowchartEntity> flowcharts;


    public FlowchartAdapter(FlowchartListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
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
//            FlowchartEntity flowchartEntity = flowcharts.get(viewHolder.getAdapterPosition());
            listener.onFlowchartClick(id);
//            listener.onCloudUploadClick(flowchartEntity);
//            listener.onDeleteProjectClick(flowchartEntity);
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
        ImageView cloudUpload;
        ImageView deleteProject;
        FlowchartEntity flowchartEntity;


        public FlowchartViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_item_name);
            cloudUpload = itemView.findViewById(R.id.iv_item_cloud_upload);
            deleteProject = itemView.findViewById(R.id.iv_item_delete);

           // cloudUpload.setOnClickListener(v->firebaseRepo.uploadFlowchartToFirebase());

            deleteProject.setOnClickListener(v -> {
                App.getInstance().getDatabase().flowchartDao().delete(flowchartEntity);
            });
        }

        protected void bind(FlowchartEntity flowchartEntity) {
            this.flowchartEntity = flowchartEntity;
            name.setText(flowchartEntity.getName());
        }
    }

    public interface FlowchartListener {
        void onFlowchartClick(long flowchartId);
//        void onCloudUploadClick(FlowchartEntity flowchartEntity);
//        void onDeleteProjectClick(FlowchartEntity flowchartEntity);
    }
}

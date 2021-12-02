package com.demo.flowchart.adapters;

import com.demo.flowchart.database.FlowchartEntity;

public interface FlowchartListener {
    void onFlowchartClick(long flowchartId);

    void deleteFlowchart(FlowchartEntity flowchartEntity);
}

package com.demo.flowchart.viewmodels;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.demo.flowchart.App;
import com.demo.flowchart.database.FlowchartDao;
import com.demo.flowchart.database.FlowchartEntity;
import com.demo.flowchart.util.JsonService;
import com.demo.flowchart.model.Workspace;

public class EditorViewModel extends ViewModel {

    private final FlowchartDao flowchartDao;
    private final JsonService jsonService;
    private FlowchartEntity flowchartEntity;

    public EditorViewModel() {
        flowchartDao = App.getInstance().getDatabase().flowchartDao();
        jsonService = new JsonService();
    }

    public void saveWorkspace(Workspace workspace) {
        String json = jsonService.flowchartToJson(workspace);
        flowchartEntity.setJson(json);
        Log.d("SUKA", json);
        flowchartDao.update(flowchartEntity);
    }

    public Workspace loadWorkspace(long id) {
        flowchartEntity = flowchartDao.get(id);
        Workspace workspace = jsonService.jsonToFlowchart(flowchartEntity.getJson());
        return workspace;
    }


}

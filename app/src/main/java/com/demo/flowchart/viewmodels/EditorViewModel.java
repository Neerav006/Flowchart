package com.demo.flowchart.viewmodels;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.demo.flowchart.App;
import com.demo.flowchart.auth.AuthRepository;
import com.demo.flowchart.database.FlowchartDao;
import com.demo.flowchart.database.FlowchartEntity;
import com.demo.flowchart.util.JsonService;
import com.demo.flowchart.model.Workspace;

public class EditorViewModel extends ViewModel {

    private final FlowchartDao flowchartDao;
    private final JsonService jsonService;
    private FlowchartEntity flowchartEntity;
    private final AuthRepository authRepo;

    public EditorViewModel() {
        authRepo = new AuthRepository();
        flowchartDao = App.getInstance().getDatabase().flowchartDao();
        jsonService = new JsonService();
    }

    public void saveWorkspace(Workspace workspace) {
        String json = jsonService.flowchartToJson(workspace);
        flowchartEntity.setJson(json);
        flowchartDao.update(flowchartEntity);
        authRepo.uploadToFirebase(flowchartEntity.getName(), json);
    }

    public Workspace loadWorkspace(long id) {
        flowchartEntity = flowchartDao.get(id);
        return jsonService.jsonToFlowchart(flowchartEntity.getJson());
    }
}

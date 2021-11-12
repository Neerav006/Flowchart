package com.demo.flowchart.util;

import com.demo.flowchart.model.Workspace;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonService {

    private final Gson gson;

    public JsonService() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public String flowchartToJson(Workspace workspace) {
        return gson.toJson(workspace);
    }

    public Workspace jsonToFlowchart(String json) {
        return gson.fromJson(json, Workspace.class);
    }
}

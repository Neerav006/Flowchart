package com.demo.flowchart.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class JsonService {

    private final Gson gson;

    public JsonService() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public String flowchartToJson(Block[] blocks) {
        return gson.toJson(blocks);
    }

    public Block[] jsonToFlowchart(String json) {
        return gson.fromJson(json, Block[].class);
    }
}

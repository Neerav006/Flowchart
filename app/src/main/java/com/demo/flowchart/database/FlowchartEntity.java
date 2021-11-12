package com.demo.flowchart.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class FlowchartEntity {

    @PrimaryKey(autoGenerate = true)
    private long uid;
    private String name;
    private String json;

    @Ignore
    public FlowchartEntity(String name, String json) {
        this.name = name;
        this.json = json;
    }

    public FlowchartEntity(long uid, String name, String json) {
        this.uid = uid;
        this.name = name;
        this.json = json;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}

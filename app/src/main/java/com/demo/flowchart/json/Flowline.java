package com.demo.flowchart.json;

public class Flowline {
    private int startBlockId;
    private int endBlockId;

    public Flowline(int startBlockId, int endBlockId) {
        this.startBlockId = startBlockId;
        this.endBlockId = endBlockId;
    }

    public int getStartBlockId() {
        return startBlockId;
    }

    public void setStartBlockId(int startBlockId) {
        this.startBlockId = startBlockId;
    }

    public int getEndBlockId() {
        return endBlockId;
    }

    public void setEndBlockId(int endBlockId) {
        this.endBlockId = endBlockId;
    }
}

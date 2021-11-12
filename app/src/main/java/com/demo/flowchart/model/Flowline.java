package com.demo.flowchart.model;

public class Flowline {

    private long startBlockId;
    private long endBlockId;

    public Flowline(long startBlockId, long endBlockId) {
        this.startBlockId = startBlockId;
        this.endBlockId = endBlockId;
    }

    public long getStartBlockId() {
        return startBlockId;
    }

    public void setStartBlockId(int startBlockId) {
        this.startBlockId = startBlockId;
    }

    public long getEndBlockId() {
        return endBlockId;
    }

    public void setEndBlockId(int endBlockId) {
        this.endBlockId = endBlockId;
    }
}

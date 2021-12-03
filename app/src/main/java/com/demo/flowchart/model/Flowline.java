package com.demo.flowchart.model;

public class Flowline {

    private long startBlockId;
    private long endBlockId;
    private Boolean decision;

    public Flowline(long startBlockId, long endBlockId, Boolean decision) {
        this.startBlockId = startBlockId;
        this.endBlockId = endBlockId;
        this.decision = decision;
    }

    public Flowline(long startBlockId, long endBlockId) {
        this(startBlockId, endBlockId, null);
    }

    public long getStartBlockId() {
        return startBlockId;
    }

    public void setStartBlockId(long startBlockId) {
        this.startBlockId = startBlockId;
    }

    public long getEndBlockId() {
        return endBlockId;
    }

    public void setEndBlockId(long endBlockId) {
        this.endBlockId = endBlockId;
    }

    public Boolean getDecision() {
        return decision;
    }

    public void setDecision(Boolean decision) {
        this.decision = decision;
    }
}

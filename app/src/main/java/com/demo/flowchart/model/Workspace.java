package com.demo.flowchart.model;

import com.demo.flowchart.drawing.WorkspaceView;

import java.util.ArrayList;
import java.util.List;

public class Workspace {

    private List<Block> blocks;
    private Long nextBlockId;
    private float scale;
    private float xOffset;
    private float yOffset;

    public Workspace(List<Block> blocks, Long nextBlockId, float scale, float xOffset, float yOffset) {
        this.blocks = blocks;
        this.nextBlockId = nextBlockId;
        this.scale = scale;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public Workspace() {
        this(new ArrayList<>(), 0L, WorkspaceView.PRE_SCALE, 0, 0);
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public Long getNextBlockId() {
        return nextBlockId;
    }

    public void setNextBlockId(Long nextBlockId) {
        this.nextBlockId = nextBlockId;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getXOffset() {
        return xOffset;
    }

    public void setXOffset(float xOffset) {
        this.xOffset = xOffset;
    }

    public float getYOffset() {
        return yOffset;
    }

    public void setYOffset(float yOffset) {
        this.yOffset = yOffset;
    }
}

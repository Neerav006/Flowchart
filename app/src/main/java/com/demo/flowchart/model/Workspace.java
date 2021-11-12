package com.demo.flowchart.model;

import com.demo.flowchart.drawing.WorkspaceView;

import java.util.ArrayList;
import java.util.List;

public class Workspace {

    private List<Block> blocks;
    private Long nextBlockId;
    private float scale;
    private int xOffset;
    private int yOffset;

    public Workspace(List<Block> blocks, Long nextBlockId, float scale, int xOffset, int yOffset) {
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

    public int getXOffset() {
        return xOffset;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }
}

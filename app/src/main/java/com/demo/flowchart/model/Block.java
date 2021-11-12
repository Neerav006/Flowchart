package com.demo.flowchart.model;


import java.util.List;

public class Block {

    private long id;
    private BlockType type;
    private int startX;
    private int startY;
    private int width;
    private int height;
    private String text;
    private Flowline[] flowlines;

    public Block(long id, BlockType type, int startX, int startY, int width, int height, String text, Flowline[] flowlines) {
        this.id = id;
        this.type = type;
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.text = text;
        this.flowlines = flowlines;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Flowline[] getFlowlines() {
        return flowlines;
    }

    public void setFlowlines(Flowline[] flowlines) {
        this.flowlines = flowlines;
    }
}

package com.demo.flowchart.drawing.model;

public class DecisionBlock extends Block {

    public DecisionBlock(int startX, int startY, int width, int height) {
        super(startX, startY, width, height);
    }

    @Override
    protected void createContour() {
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        contour.moveTo(startX + halfWidth, startY);
        contour.rLineTo(halfWidth, halfHeight);
        contour.rLineTo(-halfWidth, halfHeight);
        contour.rLineTo(-halfWidth, -halfHeight);
        contour.rLineTo(halfWidth, -halfHeight);
    }
}

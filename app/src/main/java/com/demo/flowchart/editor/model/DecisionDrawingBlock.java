package com.demo.flowchart.editor.model;

public class DecisionDrawingBlock extends DrawingBlock {

    public DecisionDrawingBlock(int startX, int startY, int width, int height) {
        super(startX, startY, width, height);
    }

    public DecisionDrawingBlock() {
        super();
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

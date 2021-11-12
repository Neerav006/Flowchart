package com.demo.flowchart.drawing.model;

public class DecisionDrawingBlock extends DrawingBlock {

    public DecisionDrawingBlock(
            long id,
            int startX,
            int startY,
            int width,
            int height,
            String text
    ) {
        super(id, startX, startY, width, height, text);
    }

    public DecisionDrawingBlock(long id, int startX, int startY) {
        super(id, startX, startY);
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

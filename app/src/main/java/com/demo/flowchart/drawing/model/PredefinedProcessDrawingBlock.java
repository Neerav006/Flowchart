package com.demo.flowchart.drawing.model;

public class PredefinedProcessDrawingBlock extends ProcessDrawingBlock {

    public PredefinedProcessDrawingBlock(
            long id,
            int startX,
            int startY,
            int width,
            int height,
            String text
    ) {
        super(id, startX, startY, width, height, text);
    }

    public PredefinedProcessDrawingBlock(long id, int startX, int startY) {
        super(id, startX, startY);
    }

    public PredefinedProcessDrawingBlock() {
        super();
    }

    @Override
    protected void createContour() {
        super.createContour();
        int edgeWidth = 15;
        contour.moveTo(startX + edgeWidth, startY);
        contour.rLineTo(0, height);
        contour.moveTo(startX + width - edgeWidth, startY);
        contour.rLineTo(0, height);
    }
}

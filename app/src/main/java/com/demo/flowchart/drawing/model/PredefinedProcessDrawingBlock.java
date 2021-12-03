package com.demo.flowchart.drawing.model;

public class PredefinedProcessDrawingBlock extends ProcessDrawingBlock {

    private static final int EDGE_WIDTH = 15;

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
        contour.moveTo(startX + EDGE_WIDTH, startY);
        contour.rLineTo(0, height);
        contour.moveTo(startX + width - EDGE_WIDTH, startY);
        contour.rLineTo(0, height);
    }

    @Override
    protected int textHorizontalPadding() {
        return super.textHorizontalPadding() + EDGE_WIDTH;
    }
}

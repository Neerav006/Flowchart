package com.demo.flowchart.editor.model;

public class PredefinedProcessDrawingBlock extends ProcessDrawingBlock {

    public PredefinedProcessDrawingBlock(int startX, int startY, int width, int height) {
        super(startX, startY, width, height);
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

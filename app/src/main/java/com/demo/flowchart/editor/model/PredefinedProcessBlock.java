package com.demo.flowchart.editor.model;

public class PredefinedProcessBlock extends ProcessBlock {

    public PredefinedProcessBlock(int startX, int startY, int width, int height) {
        super(startX, startY, width, height);
    }

    @Override
    protected void createContour() {
        super.createContour();
        int edgeWidth = 10;
        contour.moveTo(startX + edgeWidth, startY);
        contour.rLineTo(0, height);
        contour.moveTo(startX + width - edgeWidth, startY);
        contour.rLineTo(0, height);
    }
}

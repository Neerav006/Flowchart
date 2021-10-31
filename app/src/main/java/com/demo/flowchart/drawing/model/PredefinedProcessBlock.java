package com.demo.flowchart.drawing.model;

public class PredefinedProcessBlock extends ProcessBlock {

    public PredefinedProcessBlock(int startX, int startY, int width, int height) {
        super(startX, startY, width, height);
    }

    @Override
    protected void createFigure() {
        super.createFigure();
        int edgeWidth = 10;
        figure.moveTo(startX + edgeWidth, startY);
        figure.rLineTo(0, height);
        figure.moveTo(startX + width - edgeWidth, startY);
        figure.rLineTo(0, height);

    }
}

package com.demo.flowchart.drawing.model;

public class IOBlock extends Block {

    public IOBlock(int startX, int startY, int width, int height) {
        super(startX, startY, width, height);
    }

    @Override
    protected void createFigure() {
        figure.reset();
        int shift = 20;
        figure.moveTo(startX + shift, startY);
        figure.rLineTo(width - shift, 0);
        figure.rLineTo(-shift, height);
        figure.rLineTo(shift - width, 0);
        figure.rLineTo(shift, -height);
    }
}

package com.demo.flowchart.editor.model;

public class IOBlock extends Block {

    public IOBlock(int startX, int startY, int width, int height) {
        super(startX, startY, width, height);
    }

    public IOBlock() {
        super();
    }

    @Override
    protected void createContour() {
        int shift = 20;
        contour.moveTo(startX + shift, startY);
        contour.rLineTo(width - shift, 0);
        contour.rLineTo(-shift, height);
        contour.rLineTo(shift - width, 0);
        contour.rLineTo(shift, -height);
    }
}

package com.demo.flowchart.drawing.model;

public class IODrawingBlock extends DrawingBlock {

    public IODrawingBlock(
            long id,
            int startX,
            int startY,
            int width,
            int height,
            String text
    ) {
        super(id, startX, startY, width, height, text);
    }

    public IODrawingBlock(long id, int startX, int startY) {
        super(id, startX, startY);
    }

    public IODrawingBlock() {
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

package com.demo.flowchart.drawing.model;

import android.graphics.Path;
import android.graphics.RectF;

public class ProcessDrawingBlock extends DrawingBlock {

    public ProcessDrawingBlock(
            long id,
            int startX,
            int startY,
            int width,
            int height,
            String text
    ) {
        super(id, startX, startY, width, height, text);
    }

    public ProcessDrawingBlock(long id, int startX, int startY) {
        super(id, startX, startY);
    }

    public ProcessDrawingBlock() {
        super();
    }

    @Override
    protected void createContour() {
        RectF rectF = new RectF(startX, startY, startX + width, startY + height);
        contour.addRect(rectF, Path.Direction.CW);
    }
}

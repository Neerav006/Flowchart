package com.demo.flowchart.drawing.model;

import android.graphics.Path;
import android.graphics.RectF;

public class TerminalDrawingBlock extends DrawingBlock {

    public TerminalDrawingBlock(
            long id,
            int startX,
            int startY,
            int width,
            int height,
            String text
    ) {
        super(id, startX, startY, width, height, text);
    }

    public TerminalDrawingBlock(long id, int startX, int startY) {
        super(id, startX, startY);
    }

    public TerminalDrawingBlock() {
        super();
    }

    @Override
    protected void createContour() {
        int radius = height / 2;
        RectF rectF = new RectF(startX, startY, startX + width, startY + height);
        contour.addRoundRect(rectF, radius, radius, Path.Direction.CW);
    }
}
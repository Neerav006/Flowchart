package com.demo.flowchart.editor.model;

import android.graphics.Path;
import android.graphics.RectF;

public class TerminalDrawingBlock extends DrawingBlock {

    public TerminalDrawingBlock(int startX, int startY, int width, int height) {
        super(startX, startY, width, height);
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
package com.demo.flowchart.editor.model;

import android.graphics.Path;
import android.graphics.RectF;

public class ProcessDrawingBlock extends DrawingBlock {

    public ProcessDrawingBlock(int startX, int startY, int width, int height) {
        super(startX, startY, width, height);
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

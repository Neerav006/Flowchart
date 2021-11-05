package com.demo.flowchart.editor.model;

import android.graphics.Path;
import android.graphics.RectF;

public class ProcessBlock extends Block {

    public ProcessBlock(int startX, int startY, int width, int height) {
        super(startX, startY, width, height);
    }

    @Override
    protected void createContour() {
        RectF rectF = new RectF(startX, startY, startX + width, startY + height);
        contour.addRect(rectF, Path.Direction.CW);
    }
}

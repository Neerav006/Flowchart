package com.demo.flowchart.drawing.model;

import android.graphics.Path;
import android.graphics.RectF;

public class TerminalBlock  extends Block {

    public TerminalBlock(int startX, int startY, int width, int height) {
        super(startX, startY, width, height);
    }

    @Override
    protected void createContour() {
        RectF rectF = new RectF(startX, startY, startX + width, startY + height);
        contour.addRoundRect(rectF, 20f, 20f, Path.Direction.CW);
    }
}
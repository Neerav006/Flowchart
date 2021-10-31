package com.demo.flowchart.drawing.model;

import android.graphics.Path;
import android.graphics.RectF;

public class TerminalBlock  extends Block {

    public TerminalBlock(int startX, int startY, int width, int height) {
        super(startX, startY, width, height);
    }

    @Override
    protected void createFigure() {
        RectF rectF = new RectF(startX, startY, startX + width, startY + height);
        figure.reset();
        figure.addRoundRect(rectF, 20f, 20f, Path.Direction.CW);
    }
}
package com.demo.flowchart.drawing.model;

import android.graphics.Path;
import android.graphics.RectF;

import com.demo.flowchart.drawing.model.Block;

public class ProcessBlock extends Block {

    public ProcessBlock(int startX, int startY, int width, int height) {
        super(startX, startY, width, height);
    }

    @Override
    protected void createFigure() {
        RectF rectF = new RectF(startX, startY, startX + width, startY + height);
        figure.reset();
        figure.addRect(rectF, Path.Direction.CW);
    }
}

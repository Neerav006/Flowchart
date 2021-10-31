package com.demo.flowchart;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;

public class ProcessBlock extends Block {

    public ProcessBlock(int startX, int startY, int width, int height) {
        super(startX, startY, width, height);
    }

    @Override
    protected void createFigure() {
        RectF rectF = new RectF(startX, startY, startX + width, startY + height);
        figure.reset();
        figure.addRect(rectF, Path.Direction.CW);
        figure.close();
    }

}

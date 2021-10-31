package com.demo.flowchart.drawing.model;

import android.graphics.Path;
import android.graphics.RectF;

public class DecisionBlock extends Block {

    public DecisionBlock(int startX, int startY, int width, int height) {
        super(startX, startY, width, height);
    }

    @Override
    protected void createFigure() {
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        figure.reset();
        figure.moveTo(startX + halfWidth, startY);
        figure.rLineTo(halfWidth, halfHeight);
        figure.rLineTo(-halfWidth, halfHeight);
        figure.rLineTo(-halfWidth, -halfHeight);
        figure.rLineTo(halfWidth, -halfHeight);
    }
}

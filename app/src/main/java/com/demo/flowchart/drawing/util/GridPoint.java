package com.demo.flowchart.drawing.util;

import android.graphics.Matrix;
import android.view.MotionEvent;

public class GridPoint {

    public int X, Y;

    public GridPoint(MotionEvent event, Matrix matrix) {
        float[] point = new float[]{event.getX(), event.getY()};
        matrix.mapPoints(point);
        this.X = Math.round(point[0]);
        this.Y =  Math.round(point[1]);
    }
}

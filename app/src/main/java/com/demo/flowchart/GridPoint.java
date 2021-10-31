package com.demo.flowchart;

import android.graphics.Matrix;

public class GridPoint {

    public int X, Y;

    public GridPoint(EventPoint eventPoint, Matrix matrix) {
        float[] point = new float[]{eventPoint.X, eventPoint.Y};
        matrix.mapPoints(point);
        this.X = Math.round(point[0]);
        this.Y =  Math.round(point[1]);
    }
}

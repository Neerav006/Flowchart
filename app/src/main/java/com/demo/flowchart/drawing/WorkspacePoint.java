package com.demo.flowchart.drawing;

import android.graphics.Matrix;
import android.view.DragEvent;
import android.view.MotionEvent;

public class WorkspacePoint {

    public int X, Y;

    public WorkspacePoint() {}

    public WorkspacePoint(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    public WorkspacePoint(MotionEvent event, Matrix matrix) {
        float[] point = new float[]{event.getX(), event.getY()};
        matrix.mapPoints(point);
        this.X = Math.round(point[0]);
        this.Y =  Math.round(point[1]);
    }

    public WorkspacePoint(DragEvent event, Matrix matrix) {
        float[] point = new float[]{event.getX(), event.getY()};
        matrix.mapPoints(point);
        this.X = Math.round(point[0]);
        this.Y =  Math.round(point[1]);
    }
}

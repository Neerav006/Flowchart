package com.demo.flowchart.drawing.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.demo.flowchart.drawing.util.GridPoint;

public abstract class Block {

    protected int startX;
    protected int startY;
    protected int width;
    protected int height;

    protected Path contour;
    protected RectF bounds;

    protected Flowline flowline;

    protected Paint fillPaint;
    protected Paint boundsPaint;

    protected Block(int startX, int startY, int width, int height) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;

        adjustSize();
        bindToGrid();

        contour = new Path();
        bounds = new RectF();

        fillPaint = new Paint();
        fillPaint.setColor(Color.WHITE);
        fillPaint.setStyle(Paint.Style.FILL);

        boundsPaint = new Paint();
        boundsPaint.setColor(Color.MAGENTA);
        boundsPaint.setStyle(Paint.Style.FILL);
    }

    public boolean contains(GridPoint gridPoint) {
        return bounds.contains(gridPoint.X, gridPoint.Y);
    }

    public void draw(Canvas canvas, Paint contourPaint) {
        createShape();
//        canvas.drawRect(bounds, boundsPaint);
        canvas.drawPath(contour, fillPaint);
        canvas.drawPath(contour, contourPaint);

        if (flowline != null) {
            flowline.draw(canvas, contourPaint);
        }
    }

    public void move(float dX, float dY) {
        startX -= Math.round(dX);
        startY -= Math.round(dY);
    }

    public void bindToGrid() {
        startX = Math.round(startX / 10f) * 10;
        startY = Math.round(startY / 10f) * 10;
    }

    public void setOrRemoveFlowline(Block endBlock) {
        if (flowline == null || flowline.endBlock != endBlock) {
            flowline = new Flowline(this, endBlock);
        } else {
            flowline = null;
        }
    }

    protected void adjustSize() {
        height = Math.round(height / 10f) * 10;
        width = height * 3 / 2;
    }

    protected void createShape() {
        contour.reset();
        createContour();
        contour.close();
        contour.computeBounds(bounds, false);
    }

    protected abstract void createContour();
}

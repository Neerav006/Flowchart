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

    protected Paint innerPaint;

    protected Path figure;
    protected RectF bounds;

    protected Block(int startX, int startY, int width, int height) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;

//        outerPaint = new Paint();
//        outerPaint.setColor(Color.BLACK);
//        outerPaint.setStrokeWidth(2f);
//        outerPaint.setStyle(Paint.Style.STROKE);

        innerPaint = new Paint();
        innerPaint.setColor(Color.GREEN);
        innerPaint.setStyle(Paint.Style.FILL);

        figure = new Path();
        bounds = new RectF();

        bindToGrid();
    }

    public boolean contains(GridPoint gridPoint) {
        return bounds.contains(gridPoint.X, gridPoint.Y);
    }

    public void draw(Canvas canvas, Paint outerPaint) {
        createFigure();
        canvas.drawRect(bounds, innerPaint);
//        canvas.drawPath(figure, innerPaint);
        canvas.drawPath(figure, outerPaint);
    }

    public void move(float dX, float dY) {
        startX -= Math.round(dX);
        startY -= Math.round(dY);
        rebuildBlock();
    }

    public void bindToGrid() {
        startX = Math.round(startX / 10f) * 10;
        startY = Math.round(startY / 10f) * 10;
        rebuildBlock();
    }

    protected abstract void createFigure();

    protected void rebuildBlock() {
        createFigure();
        figure.computeBounds(bounds, false);
    }
}

package com.demo.flowchart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public abstract class Block {

    protected int startX;
    protected int startY;
    protected int width;
    protected int height;

    protected Paint outerPaint;
    protected Paint innerPaint;

    protected Path figure;
    protected RectF bounds;

    protected Block(int startX, int startY, int width, int height) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;

        outerPaint = new Paint();
        outerPaint.setColor(Color.BLACK);
        outerPaint.setStrokeWidth(2f);
        outerPaint.setStyle(Paint.Style.STROKE);

        innerPaint = new Paint();
        innerPaint.setColor(Color.GREEN);
        innerPaint.setStyle(Paint.Style.FILL);

        figure = new Path();
        bounds = new RectF();

        createFigure();
        figure.computeBounds(bounds, false);
    }

    protected abstract void createFigure();

    public boolean contains(GridPoint gridPoint) {
        figure.computeBounds(bounds, false);
        return bounds.contains(gridPoint.X, gridPoint.Y);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(bounds, innerPaint);
        canvas.drawPath(figure, innerPaint);
        canvas.drawPath(figure, outerPaint);
        // test
    }

    public void move(int dX, int dY) {
        startX += dX;
        startY += dY;
    }
}

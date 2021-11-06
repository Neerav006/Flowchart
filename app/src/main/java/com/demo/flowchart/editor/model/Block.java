package com.demo.flowchart.editor.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.demo.flowchart.editor.view.WorkspaceView;
import com.demo.flowchart.editor.util.WorkspacePoint;

public abstract class Block {

    private static final int DEFAULT_WIDTH = 120;
    private static final int DEFAULT_HEIGHT = 80;

    protected long id;
    protected int startX;
    protected int startY;
    protected int width;
    protected int height;

    protected String text;
    protected Flowline flowline;

    protected final Path contour;
    protected final RectF bounds;

    private final WorkspacePoint leftIn;
    private final WorkspacePoint topIn;
    private final WorkspacePoint rightOut;
    private final WorkspacePoint bottomOut;

    private final  Paint fillPaint;
    private final  Paint boundsPaint;

    {
        contour = new Path();
        bounds = new RectF();

        leftIn = new WorkspacePoint();
        topIn = new WorkspacePoint();
        rightOut = new WorkspacePoint();
        bottomOut = new WorkspacePoint();

        fillPaint = new Paint();
        fillPaint.setColor(Color.WHITE);
        fillPaint.setStyle(Paint.Style.FILL);

        boundsPaint = new Paint();
        boundsPaint.setColor(Color.MAGENTA);
        boundsPaint.setStyle(Paint.Style.FILL);
    }

    public Block(int startX, int startY) {
        this(startX, startY, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Block() {
        this(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    protected Block(int startX, int startY, int width, int height) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;

        adjustSize();
        bindToGrid();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public WorkspacePoint getLeftIn() {
        leftIn.X = startX;
        leftIn.Y = startY + (height / 2);
        return leftIn;
    }

    public WorkspacePoint getTopIn() {
        topIn.X = startX + (width / 2);
        topIn.Y = startY;
        return topIn;
    }

    public WorkspacePoint getRightOut() {
        rightOut.X = startX + width;
        rightOut.Y = startY + (height / 2);
        return rightOut;
    }

    public WorkspacePoint getBottomOut() {
        bottomOut.X = startX + (width / 2);
        bottomOut.Y = startY + height;
        return bottomOut;
    }

    public boolean contains(WorkspacePoint workspacePoint) {
        return bounds.contains(workspacePoint.X, workspacePoint.Y);
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
        if (flowline == null || !flowline.isEndBlockSame(endBlock)) {
            flowline = new Flowline(this, endBlock);
        } else {
            flowline = null;
        }
    }

    public boolean intersects(Block block) {
        int margin = WorkspaceView.GRID_STEP_SMALL;
        return RectF.intersects(
                new RectF(this.startX - margin,
                        this.startY - margin,
                        this.startX + this.width + margin,
                        this.startY + this.height + margin
                ),
                new RectF(block.startX - margin,
                        block.startY - margin,
                        block.startX + block.width + margin,
                        block.startY + block.height + margin
                )
        );
    }

    protected abstract void createContour();

    private void adjustSize() {
        height = Math.round(height / 10f) * 10;
        width = height * 3 / 2;
    }

    private void createShape() {
        contour.reset();
        createContour();
        contour.close();
        contour.computeBounds(bounds, false);
    }
}

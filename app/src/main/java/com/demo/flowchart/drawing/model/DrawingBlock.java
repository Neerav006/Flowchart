package com.demo.flowchart.drawing.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.demo.flowchart.drawing.WorkspaceView;
import com.demo.flowchart.drawing.WorkspacePoint;

import java.util.ArrayList;
import java.util.List;

public abstract class DrawingBlock {

    public static final int DEFAULT_WIDTH = 120;
    public static final int DEFAULT_HEIGHT = 80;

    protected long id;
    protected int startX;
    protected int startY;
    protected int width;
    protected int height;
    protected String text;
    protected List<DrawingFlowline> flowlines;

    protected final Path contour;
    protected final RectF bounds;

    private final WorkspacePoint leftIn;
    private final WorkspacePoint topIn;
    private final WorkspacePoint rightOut;
    private final WorkspacePoint bottomOut;

    private final Paint fillPaint;

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
    }

    protected DrawingBlock(
            long id,
            int startX,
            int startY,
            int width,
            int height,
            String text
    ) {
        this.id = id;
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.text = text;

        // TEST
        flowlines = new ArrayList<>();

        adjustSize();
        bindToGrid();
    }

    public DrawingBlock(long id, int startX, int startY) {
        this(id, startX, startY, DEFAULT_WIDTH, DEFAULT_HEIGHT, "");
    }

    // Sample block constructor
    public DrawingBlock() {
        this(0, 0, 0);
    }

    public long getId() {
        return id;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<DrawingFlowline> getFlowlines() {
        return flowlines;
    }

    public void setFlowlines(List<DrawingFlowline> flowlines) {
        this.flowlines = flowlines;
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

    public boolean contains(@NonNull WorkspacePoint workspacePoint) {
        return bounds.contains(workspacePoint.X, workspacePoint.Y);
    }

    public void draw(@NonNull Canvas canvas, Paint contourPaint) {
        createShape();

        canvas.drawPath(contour, fillPaint);
        canvas.drawPath(contour, contourPaint);
        // TEST
        canvas.drawText(text, startX + 5, startY + 5, contourPaint);

//        for (DrawingFlowline flowline: flowlines) {
//            flowline.draw(canvas, contourPaint);
//        }
    }

    public void move(float dX, float dY) {
        startX -= Math.round(dX);
        startY -= Math.round(dY);
    }

    public void bindToGrid() {
        startX = Math.round(startX / 10f) * 10;
        startY = Math.round(startY / 10f) * 10;
    }

    public void setOrRemoveFlowline(DrawingBlock endDrawingBlock) {
        for (int i = 0; i < flowlines.size(); i++) {
            if (flowlines.get(i) == null || !flowlines.get(i).isEndBlockSame(endDrawingBlock)) {
                flowlines.set(i, new DrawingFlowline(this, endDrawingBlock));
            } else {
                flowlines.set(i, null);
            }
        }
    }

    public boolean intersects(@NonNull DrawingBlock drawingBlock) {
        int margin = WorkspaceView.GRID_STEP_SMALL;
        return RectF.intersects(
                new RectF(this.startX - margin,
                        this.startY - margin,
                        this.startX + this.width + margin,
                        this.startY + this.height + margin
                ),
                new RectF(drawingBlock.startX - margin,
                        drawingBlock.startY - margin,
                        drawingBlock.startX + drawingBlock.width + margin,
                        drawingBlock.startY + drawingBlock.height + margin
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

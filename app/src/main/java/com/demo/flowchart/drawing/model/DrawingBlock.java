package com.demo.flowchart.drawing.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import androidx.annotation.NonNull;

import com.demo.flowchart.drawing.WorkspaceView;
import com.demo.flowchart.drawing.WorkspacePoint;

public abstract class DrawingBlock {

    public static final int DEFAULT_WIDTH = 120;
    public static final int DEFAULT_HEIGHT = 80;

    protected long id;
    protected int startX;
    protected int startY;
    protected int width;
    protected int height;
    protected String text;
    protected DrawingFlowline[] flowlines;

    protected final Path contour;
    protected final RectF bounds;

    private final WorkspacePoint leftIn;
    private final WorkspacePoint topIn;
    private final WorkspacePoint rightOut;
    private final WorkspacePoint bottomOut;

    private final Paint fillPaint;
    protected final TextPaint textPaint;
    protected final Paint hintPaint;

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

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));
        textPaint.setTextSize(18);

        hintPaint = new Paint();
        hintPaint.setColor(Color.BLACK);
        hintPaint.setStyle(Paint.Style.FILL);
        hintPaint.setTextAlign(Paint.Align.LEFT);
        hintPaint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD_ITALIC));
        hintPaint.setTextSize(14);
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

        this.flowlines = new DrawingFlowline[1];

        adjustSize();
        bindToGrid();
    }

    public DrawingBlock(long id, int startX, int startY) {
        this(id, startX, startY, DEFAULT_WIDTH, DEFAULT_HEIGHT, "");
    }

    // Sample block constructor
    public DrawingBlock() {
        this(0, 0, 0);
        this.text = null;
        this.flowlines = null;
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

    public DrawingFlowline[] getFlowlines() {
        return flowlines;
    }

    public void setFlowlines(DrawingFlowline[] flowlines) {
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
        drawBlock(canvas, contourPaint);
        drawText(canvas);
        drawFlowlines(canvas, contourPaint);
    }

    public void resize() {
        int desiredHeight = DEFAULT_HEIGHT;
        int desiredWidth = DEFAULT_WIDTH;

        while (true) {
            StaticLayout textLayout = new StaticLayout(
                    text, textPaint, (desiredWidth - textHorizontalPadding() * 2), Layout.Alignment.ALIGN_NORMAL,
                    1.0f, 0.0f, false
            );
            int textHeight = textLayout.getHeight();
            if (textHeight > desiredHeight - textVerticalPadding() * 2) {
                desiredHeight += 20;
                desiredWidth = desiredHeight * 3 / 2;
            } else {
                break;
            }
        }

        height = desiredHeight;
        adjustSize();
        bindToGrid();
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
        if (flowlines[0] == null || !flowlines[0].isEndBlockSame(endDrawingBlock)) {
            flowlines[0] = new DrawingFlowline(this, endDrawingBlock);
        } else {
            flowlines[0] = null;
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

    private void drawBlock(Canvas canvas, Paint contourPaint) {
        createShape();
        canvas.drawPath(contour, fillPaint);
        canvas.drawPath(contour, contourPaint);
    }

    private void drawText(Canvas canvas) {
        if (text == null) return;
        if (text.isEmpty()) return;

        StaticLayout textLayout =  new StaticLayout(
                text, textPaint, (textHorizontalSize() - textHorizontalPadding() * 2), Layout.Alignment.ALIGN_NORMAL,
                1.0f, 0.0f, false
        );

        int textHeight = textLayout.getHeight();
        float textStartX = bounds.centerX();
        float textStartY = bounds.centerY() - textHeight / 2f;

        canvas.save();
        canvas.translate(textStartX, textStartY);
        textLayout.draw(canvas);
        canvas.restore();
    }

    private void drawFlowlines(Canvas canvas, Paint contourPaint) {
        if (flowlines == null) return;
        for (DrawingFlowline flowline : flowlines) {
            if (flowline != null) {
                flowline.draw(canvas, contourPaint, hintPaint);
            }
        }
    }

    protected void adjustSize() {
        height = Math.round(height / 10f) * 10;
        width = height * 3 / 2;
    }

    private void createShape() {
        contour.reset();
        createContour();
        contour.close();
        contour.computeBounds(bounds, false);
    }

    protected int textHorizontalPadding() {
        return 10;
    }

    protected int textVerticalPadding() {
        return 10;
    }

    protected int textHorizontalSize() {
        return width;
    }
}

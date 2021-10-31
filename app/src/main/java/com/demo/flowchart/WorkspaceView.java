package com.demo.flowchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.ArrayList;
import java.util.List;

public class WorkspaceView extends View {

    private static final int WORKSPACE_WIDTH = 1000;
    private static final int WORKSPACE_HEIGHT = 1000;
    private static final int GRID_STEP_SMALL = 10;
    private static final int GRID_STEP_LARGE = 40;

    private static final int PRE_SCALE = 4;
    private static final int MAX_SCALE = 10;

    private static final long LONG_PRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();

    private int maxXOffset, maxYOffset;
    private int minXOffset, minYOffset;
    private int xOffset, yOffset;

    private float maxScale, minScale;
    private float scale;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    private long downTime, movingStartTime, upTime;
    private Block selectedBlock;
    private boolean isBlockMoving;
    private int lastDownX, lastDownY;

    private Paint blockPaint;
    private Paint gridPaint;
    private Canvas canvas;

    private List<Block> blocks;
    private boolean isLongPressed;
    private boolean isBlockClicked;

    public WorkspaceView(Context context, AttributeSet attrs) {
        super(context);

        ProcessBlock test1 = new ProcessBlock(10, 100, 50, 30);
        ProcessBlock test2 = new ProcessBlock(10, 70, 25, 15);
        ProcessBlock test3 = new ProcessBlock(20, 20, 120, 80);

        scaleGestureDetector = new ScaleGestureDetector(this.getContext(), new WorkspaceScaleListener());
        gestureDetector = new GestureDetector(this.getContext(), new WorkspaceGestureListener());

        blocks = new ArrayList();
//        blocks.add(test1);
//        blocks.add(test2);
        blocks.add(test3);

        setUpPaints();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (right >= bottom) {
            minScale = (float) right / WORKSPACE_WIDTH;
        } else {
            minScale = (float) bottom / WORKSPACE_HEIGHT;
        }
        maxScale = MAX_SCALE;
        scale = PRE_SCALE;

        minXOffset = right - WORKSPACE_WIDTH * PRE_SCALE;
        minYOffset = bottom - WORKSPACE_HEIGHT * PRE_SCALE;
        maxXOffset = 0;
        maxYOffset = 0;
        xOffset = 0;
        yOffset = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        xOffset = Math.max(minXOffset, Math.min(maxXOffset, xOffset));
        yOffset = Math.max(minYOffset, Math.min(maxYOffset, yOffset));

        canvas.translate(xOffset, yOffset);
        canvas.scale(scale, scale);

        drawGrid(canvas);

        for (Block block : blocks) {
            block.draw(canvas);
//            Log.d("WorkspaceView", String.format("BlockXY: %d, %d", block.startX, block.startY));

//            canvas.drawRect(block.getBounds(), paint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                Log.d("WorkspaceView", String.format("Down start: %d", event.getDownTime()));
                return true;
            }

            case MotionEvent.ACTION_MOVE:

            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_CANCEL:
        }
        return true;
    }

    private void drawGrid(Canvas canvas) {
        for (int x = 0; x <= WORKSPACE_WIDTH; x += GRID_STEP_SMALL) {
            for (int y = 0; y <= WORKSPACE_HEIGHT; y += GRID_STEP_SMALL) {
                if (x % GRID_STEP_LARGE == 0 && y % GRID_STEP_LARGE == 0) {
                    gridPaint.setStrokeWidth(8f / scale);
                } else {
                    gridPaint.setStrokeWidth(3f / scale);
                }
                canvas.drawLine(0, y, WORKSPACE_WIDTH, y, gridPaint);
                canvas.drawLine(x, 0, x, WORKSPACE_HEIGHT, gridPaint);
            }
        }
    }

    private void bindToGrid(Block block) {
        block.startX = Math.round(block.startX / 10f) * 10;
        block.startY = Math.round(block.startY / 10f) * 10;
    }

    private void moveBlock(float dX, float dY) {

    }

    private void moveWorkspace(float dX, float dY) {
        xOffset -= Math.round(dX);
        yOffset -= Math.round(dY);
        invalidate();
    }

    private void setUpPaints() {
        gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStyle(Paint.Style.STROKE);

        blockPaint = new Paint();
        blockPaint.setColor(Color.BLACK);
        blockPaint.setStrokeWidth(5f / scale);
        blockPaint.setStyle(Paint.Style.STROKE);
    }

    private void setScale(float scaleFactor) {
        scale *= scaleFactor;
        scale = Math.max(minScale, Math.min(maxScale, scale));
        minXOffset = getWidth() - Math.round(WORKSPACE_WIDTH * scale);
        minYOffset = getHeight() - Math.round(WORKSPACE_HEIGHT * scale);
        invalidate();
    }

    private class WorkspaceScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            scale = Math.max(minScale, Math.min(maxScale, scale));
            minXOffset = getWidth() - Math.round(WORKSPACE_WIDTH * scale);
            minYOffset = getHeight() - Math.round(WORKSPACE_HEIGHT * scale);
            invalidate();
            return true;
        }
    }

    private class WorkspaceGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            scale = (scale != PRE_SCALE) ? PRE_SCALE : PRE_SCALE * 1.5f;
            minXOffset = getWidth() - Math.round(WORKSPACE_WIDTH * scale);
            minYOffset = getHeight() - Math.round(WORKSPACE_HEIGHT * scale);
            invalidate();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent downEvent, MotionEvent scrollEvent, float distanceX, float distanceY) {


            if (isBlockMoving) {
                moveBlock(distanceX, distanceY);
            } else {
                moveWorkspace(distanceX, distanceY);
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent downEvent, MotionEvent flingEvent, float velocityX, float velocityY) {
            return false;
        }
    }

//    private Block getSelectedBlock(MotionEvent event) {
//
//        Block selectedBlock = null;
//        for (Block block : blocks) {
//            if (block.getBounds().contains(gridX, gridY)) {
//                selectedBlock = block;
//            } else {
//                selectedBlock = null;
//            }
//        }
//        return selectedBlock;
//    }

    private Matrix getUpdatedMatrix() {
        Matrix matrix = new Matrix();
        matrix.preScale(scale, scale);
        matrix.postTranslate(xOffset, yOffset);
        matrix.invert(matrix);
        return matrix;
    }
}

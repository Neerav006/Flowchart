package com.demo.flowchart.drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.demo.flowchart.drawing.model.Block;
import com.demo.flowchart.drawing.model.DecisionBlock;
import com.demo.flowchart.drawing.model.IOBlock;
import com.demo.flowchart.drawing.model.PredefinedProcessBlock;
import com.demo.flowchart.drawing.model.ProcessBlock;
import com.demo.flowchart.drawing.model.TerminalBlock;
import com.demo.flowchart.drawing.util.GridPoint;

import java.util.ArrayList;

public class WorkspaceView extends View {

    private static final int WORKSPACE_WIDTH = 1000;
    private static final int WORKSPACE_HEIGHT = 1000;
    private static final int GRID_STEP_SMALL = 10;
    private static final int GRID_STEP_LARGE = 40;

    private static final int MAX_X_OFFSET = 0;
    private static final int MAX_Y_OFFSET = 0;

    private static final int PRE_SCALE = 4;
    private static final int MAX_SCALE = 10;

    private static final long LONG_PRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();

    private final Context context;
    private final Vibrator vibrator;

    private int minXOffset, minYOffset;
    private int xOffset, yOffset;

    private float minScale;
    private float scale;

    private final ScaleGestureDetector scaleGestureDetector;
    private final GestureDetector gestureDetector;

    private Block touchedBlock;
    private Block selectedBlock;
    private boolean isBlockMovable;
    private boolean isBlockClicked;
    private boolean isDoubleTaped;

    private int lastDownX, lastDownY;
    private long lastDownTime;
    private boolean isLongPressed;

    private final Matrix gridMatrix;
    private final Paint blockPaint;
    private final Paint gridPaint;
    // test paints
    private final Paint touchedPaint;
    private final Paint movablePaint;
    private final Paint selectedPaint;

    private ArrayList<Block> blocks;

    private void setTestBlocks() {
        TerminalBlock test1 = new TerminalBlock(20, 240, 120, 60);
        DecisionBlock test2 = new DecisionBlock(40, 120, 180, 80);
        ProcessBlock test3 = new PredefinedProcessBlock(20, 20, 120, 80);
        IOBlock test4 = new IOBlock(40, 340, 120, 80);

//        ProcessBlock test5 = new ProcessBlock(20, 20, 120, 80);
//        ProcessBlock test6 = new ProcessBlock(60, 140, 120, 80);

        blocks = new ArrayList<>();
        blocks.add(test1);
        blocks.add(test2);
        blocks.add(test3);
        blocks.add(test4);
//        blocks.add(test5);
//        blocks.add(test6);
    }

    public WorkspaceView(Context context, AttributeSet attrs) {
        super(context);
        this.context = context;
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        scale = PRE_SCALE;
        xOffset = 0;
        yOffset = 0;

        scaleGestureDetector = new ScaleGestureDetector(this.getContext(), new WorkspaceScaleListener());
        gestureDetector = new GestureDetector(this.getContext(), new WorkspaceGestureListener());
        gestureDetector.setIsLongpressEnabled(false);

        gridMatrix = new Matrix();
        blockPaint = new Paint();
        blockPaint.setColor(Color.BLACK);
        blockPaint.setStyle(Paint.Style.STROKE);
        blockPaint.setStrokeWidth(8f / scale);
        gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStyle(Paint.Style.STROKE);

        touchedPaint = new Paint();
        touchedPaint.setColor(Color.CYAN);
        touchedPaint.setStrokeWidth(8f / scale);
        touchedPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        movablePaint = new Paint();
        movablePaint.setColor(Color.RED);
        movablePaint.setStrokeWidth(8f / scale);
        movablePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        selectedPaint = new Paint();
        selectedPaint.setColor(Color.YELLOW);
        selectedPaint.setStrokeWidth(8f / scale);
        selectedPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        setTestBlocks();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (right >= bottom) {
            minScale = (float) right / WORKSPACE_WIDTH;
        } else {
            minScale = (float) bottom / WORKSPACE_HEIGHT;
        }
        minXOffset = right - WORKSPACE_WIDTH * PRE_SCALE;
        minYOffset = bottom - WORKSPACE_HEIGHT * PRE_SCALE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        xOffset = Math.max(minXOffset, Math.min(MAX_X_OFFSET, xOffset));
        yOffset = Math.max(minYOffset, Math.min(MAX_Y_OFFSET, yOffset));

        canvas.translate(xOffset, yOffset);
        canvas.scale(scale, scale);

        drawGrid(canvas);

        for (Block block : blocks) {
            // test
            if (touchedBlock != null && block == touchedBlock) {
                if (isBlockMovable) {
                    block.draw(canvas, movablePaint);
                } else {
                    block.draw(canvas, touchedPaint);
                }
            } else if (selectedBlock != null && block == selectedBlock) {
                block.draw(canvas, selectedPaint);
            } else {
                block.draw(canvas, blockPaint);
            }
        }
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);

        boolean isHoldDown;
        boolean isLongTime;
        isLongTime = ((event.getEventTime() - lastDownTime) > LONG_PRESS_TIMEOUT);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                lastDownX = Math.round(event.getX());
                lastDownY = Math.round(event.getY());
                lastDownTime = event.getDownTime();
                searchTouchedBlock(event);
                vibrateOnBlockMovable();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                long eventX = Math.round(event.getX());
                long eventY = Math.round(event.getY());
                // Detect a long press until it happens
                if (!isLongPressed) {
                    // A long press consists of a hold and a long touch
                    isHoldDown = ((Math.abs(eventX - lastDownX) < 5) && (Math.abs(eventY - lastDownY) < 5));
//                    isLongTime = ((event.getEventTime() - lastDownTime) > LONG_PRESS_TIMEOUT);
                    isLongPressed = isHoldDown && isLongTime;
                    vibrator.cancel();
                }
                // Set a scroll mode for the gestureDetector's onScroll method
                isBlockMovable = isLongPressed && (touchedBlock != null);
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (!isLongTime && !isLongPressed && !isDoubleTaped) {
                    searchTouchedBlock(event);
                    if (touchedBlock != null) {
                        if (selectedBlock != null) {
                            if (touchedBlock != selectedBlock) {
                                selectedBlock.setOrRemoveFlowline(touchedBlock);
                                invalidate();
                                touchedBlock = null;
                            }
                            selectedBlock = null;
                        } else {
                            selectedBlock = touchedBlock;
                        }
                    } else {
                        selectedBlock = null;
                    }
                }
//                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                if (touchedBlock != null) {
                    touchedBlock.bindToGrid();
                    invalidate();
                }
                isLongPressed = false;
                isBlockMovable = false;
                isDoubleTaped = false;
                touchedBlock = null;
                vibrator.cancel();
                break;
            }
        }
        invalidate();
        return true;
    }

    private class WorkspaceScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            adjustAndSetScale();
            selectedBlock = null;
            touchedBlock = null;
            return true;
        }
    }

    private class WorkspaceGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            searchTouchedBlock(event);
            String message =
                    (touchedBlock != null)
                    ? String.format("Block: %s", touchedBlock.getClass().getSimpleName())
                    : "Workspace";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            searchTouchedBlock(event);
            if (touchedBlock != null) {

            }

            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            isDoubleTaped = true;
            scale = (scale != PRE_SCALE) ? PRE_SCALE : PRE_SCALE * 1.5f;
            adjustAndSetScale();
            selectedBlock = null;
            touchedBlock = null;
            return true;
        }



        @Override
        public boolean onScroll(MotionEvent downEvent, MotionEvent scrollEvent, float distanceX, float distanceY) {
            if (isBlockMovable) {
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

    private void adjustAndSetScale() {
        scale = Math.max(minScale, Math.min(MAX_SCALE, scale));
        minXOffset = getWidth() - Math.round(WORKSPACE_WIDTH * scale);
        minYOffset = getHeight() - Math.round(WORKSPACE_HEIGHT * scale);
        blockPaint.setStrokeWidth(8f / scale);
        invalidate();
    }

    private void moveBlock(float dX, float dY) {
        touchedBlock.move(dX / scale, dY / scale);
        invalidate();
    }

    private void moveWorkspace(float dX, float dY) {
        xOffset -= Math.round(dX);
        yOffset -= Math.round(dY);
        invalidate();
    }

    private void searchTouchedBlock(MotionEvent downEvent) {
        gridMatrix.reset();
        gridMatrix.preScale(scale, scale);
        gridMatrix.postTranslate(xOffset, yOffset);
        gridMatrix.invert(gridMatrix);
        GridPoint gridPoint = new GridPoint(downEvent, gridMatrix);

        touchedBlock = null;
        for (Block block : blocks) {
            if (block.contains(gridPoint)) {
                touchedBlock = block;
                break;
            }
        }
        // Put the touched block to the foreground
        if (touchedBlock != null) {
            blocks.remove(touchedBlock);
            blocks.add(touchedBlock);
        }
    }

    private void vibrateOnBlockMovable() {
        long[] delayPattern = { LONG_PRESS_TIMEOUT, 50L};
        boolean canVibrate = vibrator.hasVibrator();
        if (canVibrate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(delayPattern, -1));
            } else {
                vibrator.vibrate(delayPattern, -1);
            }
        }
    }
}

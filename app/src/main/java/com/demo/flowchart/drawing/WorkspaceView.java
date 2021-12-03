package com.demo.flowchart.drawing;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;

import com.demo.flowchart.drawing.model.DecisionDrawingBlock;
import com.demo.flowchart.drawing.model.DrawingBlock;
import com.demo.flowchart.drawing.model.IODrawingBlock;
import com.demo.flowchart.drawing.model.PredefinedProcessDrawingBlock;
import com.demo.flowchart.drawing.model.ProcessDrawingBlock;
import com.demo.flowchart.drawing.model.TerminalDrawingBlock;
import com.demo.flowchart.util.Converters;
import com.demo.flowchart.model.Workspace;
import com.demo.flowchart.util.JsonService;

import java.util.HashMap;
import java.util.Map;

public class WorkspaceView extends View implements View.OnDragListener {

    public static final int GRID_STEP_SMALL = 10;
    public static final float PRE_SCALE = 3f;

    public static final int WORKSPACE_WIDTH = 1000;
    public static final int WORKSPACE_HEIGHT = 1000;
    public static final float BOLD_LINE_WIDTH = 8f;

    private static final int GRID_STEP_LARGE = 40;

    private static final int MAX_X_OFFSET = 0;
    private static final int MAX_Y_OFFSET = 0;

    private static final float MAX_SCALE = 6f;
    private static final float DOUBLE_TAP_SCALE_FACTOR = 1.5f;
    private static final float THIN_LINE_WIDTH = 3f;

    private static final int HOLD_DOWN_ALLOWABLE_OFFSET = 5;
    private static final long LONG_PRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    private static final String WORKSPACE_STATE_KEY = "workspaceState";
    private static final String SELECTED_BLOCK_ID = "selectedBlockId";

    private long nextBlockId;

    private final Context context;
    private final Vibrator vibrator;

    private float minXOffset, minYOffset;
    private float xOffset, yOffset;

    private float minScale;
    private float scale;

    private final ScaleGestureDetector scaleGestureDetector;
    private final GestureDetector gestureDetector;

    private DrawingBlock touchedDrawingBlock;
    private DrawingBlock selectedDrawingBlock;

    private boolean isBlockMovable;
    private boolean isBlockClicked;
    private boolean isDoubleTaped;

    private int lastDownX, lastDownY;
    private long lastDownTime;
    private boolean isLongPressed;

    private final Matrix workspaceMatrix;
    private final Paint blockPaint;
    private final Paint selectedBlockPaint;
    private final Paint gridPaint;

    private Map<Long, DrawingBlock> drawingBlockMap;

    public WorkspaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        drawingBlockMap = new HashMap<>();

        nextBlockId = 0;

        scale = PRE_SCALE;
        xOffset = 0;
        yOffset = 0;

        scaleGestureDetector = new ScaleGestureDetector(this.getContext(), new WorkspaceScaleListener());
        gestureDetector = new GestureDetector(this.getContext(), new WorkspaceGestureListener());
        gestureDetector.setIsLongpressEnabled(false);

        workspaceMatrix = new Matrix();
        blockPaint = new Paint();
        blockPaint.setColor(Color.BLACK);
        blockPaint.setStyle(Paint.Style.STROKE);
        blockPaint.setStrokeWidth(BOLD_LINE_WIDTH / scale);
        gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        selectedBlockPaint = new Paint();
        selectedBlockPaint.setColor(Color.RED);
        selectedBlockPaint.setStyle(Paint.Style.STROKE);
        selectedBlockPaint.setStrokeWidth(BOLD_LINE_WIDTH / scale);

        setOnDragListener(this);
    }

    public void setWorkspace(Workspace workspace) {
        this.drawingBlockMap = Converters.blocksToDrawingBlocks(workspace.getBlocks());
        this.nextBlockId = workspace.getNextBlockId();
        this.scale = workspace.getScale();
        this.xOffset = workspace.getXOffset();
        this.yOffset = workspace.getYOffset();
    }

    public Workspace getWorkspace() {
        return new Workspace(
                Converters.drawingBlocksToBlocks(drawingBlockMap),
                nextBlockId,
                scale,
                xOffset,
                yOffset
        );
    }

    private SelectedBlockListener selectedBlockListener = null;
    public void setSelectedBlockListener(SelectedBlockListener listener) {
        selectedBlockListener = listener;
    }

    public String getBlockText() {
        return selectedDrawingBlock.getText();
    }

    public void editBlockText(String text) {
        selectedDrawingBlock.setText(text);
        selectedDrawingBlock.resize();
        setSelectedDrawingBlock(null);
        invalidate();
    }

    public void deleteBlock() {
        drawingBlockMap.remove(selectedDrawingBlock.getId());
        setSelectedDrawingBlock(null);
        invalidate();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        String json = new JsonService().flowchartToJson(getWorkspace());
        bundle.putString(WORKSPACE_STATE_KEY, json);
        if (selectedDrawingBlock != null) {
            bundle.putLong(SELECTED_BLOCK_ID, selectedDrawingBlock.getId());
        }
        super.onSaveInstanceState();
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            String json = ((Bundle) state).getString(WORKSPACE_STATE_KEY);
            if (json != null) {
                JsonService jsonService = new JsonService();
                setWorkspace(jsonService.jsonToFlowchart(json));
            }

            long id = ((Bundle) state).getLong(SELECTED_BLOCK_ID, -1);
            if (id != -1) {
                setSelectedDrawingBlock(drawingBlockMap.get(id));
            }
        }
        super.onRestoreInstanceState(super.onSaveInstanceState());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (right >= bottom) {
            minScale = (float) (right - left) / WORKSPACE_WIDTH;
        } else {
            minScale = (float) (bottom - top) / WORKSPACE_HEIGHT;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        adjustWorkspace();

        canvas.translate(xOffset, yOffset);
        canvas.scale(scale, scale);

        drawGrid(canvas);

        for (DrawingBlock drawingBlock : drawingBlockMap.values()) {
            if (drawingBlock == selectedDrawingBlock) {
                drawingBlock.draw(canvas, selectedBlockPaint);
            } else {
                drawingBlock.draw(canvas, blockPaint);
            }
        }

        canvas.drawPoint(WORKSPACE_WIDTH / 2f, WORKSPACE_HEIGHT / 2f, blockPaint);
    }

    // TODO Make it prettier
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
                    isHoldDown = ((Math.abs(eventX - lastDownX) < HOLD_DOWN_ALLOWABLE_OFFSET)
                            && (Math.abs(eventY - lastDownY) < HOLD_DOWN_ALLOWABLE_OFFSET));
                    isLongPressed = isHoldDown && isLongTime;
                    vibrator.cancel();
                }
                // Set a scroll mode for the gestureDetector's onScroll method
                isBlockMovable = isLongPressed && (touchedDrawingBlock != null);
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (!isLongTime && !isLongPressed && !isDoubleTaped) {
                    searchTouchedBlock(event);
                    if (touchedDrawingBlock != null) {
                        if (selectedDrawingBlock != null) {
                            if (touchedDrawingBlock != selectedDrawingBlock) {
                                if (selectedDrawingBlock instanceof DecisionDrawingBlock) {
                                    DecisionDrawingBlock decisionDrawingBlock = ((DecisionDrawingBlock) selectedDrawingBlock);
                                    if (decisionDrawingBlock.hasFlowline(touchedDrawingBlock)) {
                                        decisionDrawingBlock.removeFlowline(touchedDrawingBlock);
                                        invalidate();
                                    } else {
                                        String[] itemsNames = new String[]{"true", "false"};
                                        new AlertDialog.Builder(context)
                                                .setTitle("Decision")
                                                .setSingleChoiceItems(itemsNames, -1, (dialog, which) -> {
                                                    Boolean decision = which == 0;
                                                    decisionDrawingBlock.setFlowline(touchedDrawingBlock, decision);
                                                    invalidate();
                                                    dialog.dismiss();
                                                })
                                                .show();
                                    }
                                } else {
                                    selectedDrawingBlock.setOrRemoveFlowline(touchedDrawingBlock);
                                    invalidate();
                                }
                            }
                            setSelectedDrawingBlock(null);
                        } else {
                            setSelectedDrawingBlock(touchedDrawingBlock);
                        }
                    } else {
                        setSelectedDrawingBlock(null);
                    }
                }
//                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                if (touchedDrawingBlock != null) {
                    touchedDrawingBlock.bindToGrid();
                    invalidate();
                }
                isLongPressed = false;
                isBlockMovable = false;
                isDoubleTaped = false;
                touchedDrawingBlock = null;
                vibrator.cancel();
                break;
            }
        }
        invalidate();
        return true;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        if (scaleGestureDetector.isInProgress()) {
            return false;
        }

        if (dragEvent.getAction() == DragEvent.ACTION_DROP) {
            updateWorkspaceMatrix();
            WorkspacePoint point = new WorkspacePoint(dragEvent, workspaceMatrix);

            ClipData.Item item = dragEvent.getClipData().getItemAt(0);
            String data = item.getText().toString();
            addBlock(data, point);
        }
        invalidate();
        return true;
    }

    private void drawGrid(Canvas canvas) {
        for (int x = 0; x <= WORKSPACE_WIDTH; x += GRID_STEP_SMALL) {
            for (int y = 0; y <= WORKSPACE_HEIGHT; y += GRID_STEP_SMALL) {
                if (x % GRID_STEP_LARGE == 0 && y % GRID_STEP_LARGE == 0) {
                    gridPaint.setStrokeWidth(BOLD_LINE_WIDTH / scale);
                } else {
                    gridPaint.setStrokeWidth(THIN_LINE_WIDTH / scale);
                }
                canvas.drawLine(0, y, WORKSPACE_WIDTH, y, gridPaint);
                canvas.drawLine(x, 0, x, WORKSPACE_HEIGHT, gridPaint);
            }
        }
    }

    private void adjustWorkspace() {
        scale = Math.max(minScale, Math.min(MAX_SCALE, scale));
        blockPaint.setStrokeWidth(BOLD_LINE_WIDTH / scale);
        minXOffset = getWidth() - Math.round(WORKSPACE_WIDTH * scale);
        minYOffset = getHeight() - Math.round(WORKSPACE_HEIGHT * scale);
        xOffset = Math.max(minXOffset, Math.min(MAX_X_OFFSET, xOffset));
        yOffset = Math.max(minYOffset, Math.min(MAX_Y_OFFSET, yOffset));
    }

    private void moveWorkspace(float dX, float dY) {
        xOffset -= Math.round(dX);
        yOffset -= Math.round(dY);
    }

    private void addBlock(String blockClassName, WorkspacePoint startPoint) {
        int startX = startPoint.X - DrawingBlock.DEFAULT_WIDTH / 2;
        int startY = startPoint.Y - DrawingBlock.DEFAULT_HEIGHT / 2;
        DrawingBlock drawingBlock = null;
        if (blockClassName.equals(TerminalDrawingBlock.class.getSimpleName())) {
            drawingBlock = new TerminalDrawingBlock(nextBlockId, startX, startY);
        } else if (blockClassName.equals(ProcessDrawingBlock.class.getSimpleName())) {
            drawingBlock = new ProcessDrawingBlock(nextBlockId, startX, startY);
        } else if (blockClassName.equals(PredefinedProcessDrawingBlock.class.getSimpleName())) {
            drawingBlock = new PredefinedProcessDrawingBlock(nextBlockId, startX, startY);
        } else if (blockClassName.equals(DecisionDrawingBlock.class.getSimpleName())) {
            drawingBlock = new DecisionDrawingBlock(nextBlockId, startX, startY);
        } else if (blockClassName.equals(IODrawingBlock.class.getSimpleName())) {
            drawingBlock = new IODrawingBlock(nextBlockId, startX, startY);
        }
        if (drawingBlock != null) {
            drawingBlockMap.put(nextBlockId, drawingBlock);
            nextBlockId++;
        }
    }

    private void moveBlock(float dX, float dY) {
        touchedDrawingBlock.move(dX / scale, dY / scale);
    }

    private void searchTouchedBlock(MotionEvent downEvent) {
        updateWorkspaceMatrix();
        WorkspacePoint workspacePoint = new WorkspacePoint(downEvent, workspaceMatrix);

        for (DrawingBlock drawingBlock : drawingBlockMap.values()) {
            if (drawingBlock.contains(workspacePoint)) {
                touchedDrawingBlock = drawingBlock;
                break;
            }
        }
    }

    private void updateWorkspaceMatrix() {
        workspaceMatrix.reset();
        workspaceMatrix.preScale(scale, scale);
        workspaceMatrix.postTranslate(xOffset, yOffset);
        workspaceMatrix.invert(workspaceMatrix);
    }

    private void vibrateOnBlockMovable() {
        long[] delayPattern = {LONG_PRESS_TIMEOUT, 50L};
        boolean canVibrate = vibrator.hasVibrator();
        if (canVibrate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(delayPattern, -1));
            } else {
                vibrator.vibrate(delayPattern, -1);
            }
        }
    }

    private void setSelectedDrawingBlock(DrawingBlock selectedDrawingBlock) {
        this.selectedDrawingBlock = selectedDrawingBlock;
        selectedBlockListener.onBlockSelected(selectedDrawingBlock != null);
    }

    public interface SelectedBlockListener {
        void onBlockSelected(Boolean isSelected);
    }

    private class WorkspaceScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float oldScale = scale;
            scale *= detector.getScaleFactor();

            if (scale < MAX_SCALE) {
                float scaleChange = scale - oldScale;
                xOffset += (detector.getFocusX() * scaleChange);
                yOffset += (detector.getFocusY() * scaleChange);
            }

            invalidate();
            setSelectedDrawingBlock(null);
            touchedDrawingBlock = null;
            return true;
        }
    }

    private class WorkspaceGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            searchTouchedBlock(event);
            String message =
                    (touchedDrawingBlock != null)
                            ? String.format("Block: %s", touchedDrawingBlock.getClass().getSimpleName())
                            : "Workspace";

            searchTouchedBlock(event);
            if (touchedDrawingBlock != null) {
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            isDoubleTaped = true;
            float oldScale = scale;
            scale = (scale != PRE_SCALE) ? PRE_SCALE : PRE_SCALE * DOUBLE_TAP_SCALE_FACTOR;

            float scaleChange = scale - oldScale;
            xOffset -= (event.getX() * scaleChange);
            yOffset -= (event.getY() * scaleChange);

            invalidate();
            setSelectedDrawingBlock(null);
            touchedDrawingBlock = null;
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent downEvent, MotionEvent scrollEvent, float distanceX, float distanceY) {
            if (!scaleGestureDetector.isInProgress()) {
                if (isBlockMovable) {
                    moveBlock(distanceX, distanceY);
                } else {
                    moveWorkspace(distanceX, distanceY);
                }
                invalidate();
                return true;
            }
            else return false;
        }

        @Override
        public boolean onFling(MotionEvent downEvent, MotionEvent flingEvent, float velocityX, float velocityY) {
            return false;
        }
    }
}

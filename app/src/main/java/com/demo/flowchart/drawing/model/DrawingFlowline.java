package com.demo.flowchart.drawing.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.demo.flowchart.drawing.WorkspaceView;
import com.demo.flowchart.drawing.WorkspacePoint;

public class DrawingFlowline {

    private static final int MIN_LINE_LENGTH = WorkspaceView.GRID_STEP_SMALL * 2;

    private final DrawingBlock startDrawingBlock;
    private final DrawingBlock endDrawingBlock;
    private final Boolean decision;
    private final Path line;

    public DrawingFlowline(DrawingBlock startDrawingBlock, DrawingBlock endDrawingBlock, Boolean decision) {
        this.startDrawingBlock = startDrawingBlock;
        this.endDrawingBlock = endDrawingBlock;
        this.decision = decision;
        line = new Path();
    }

    public DrawingFlowline(DrawingBlock startDrawingBlock, DrawingBlock endDrawingBlock) {
        this(startDrawingBlock, endDrawingBlock, null);
    }

    public DrawingBlock getStartDrawingBlock() {
        return startDrawingBlock;
    }

    public DrawingBlock getEndDrawingBlock() {
        return endDrawingBlock;
    }

    public Boolean getDecision() {
        return decision;
    }

    public void draw(Canvas canvas, Paint linePaint, Paint textPaint) {
        createLine();
        if (!line.isEmpty()) {
            canvas.drawPath(line, linePaint);
            if (decision != null) {
                String hint = decision ? "True" : "False";
                canvas.drawTextOnPath(hint, line, 5, -5, textPaint);
            }
        }
    }

    protected boolean isEndBlockSame(DrawingBlock endDrawingBlock) {
        return this.endDrawingBlock == endDrawingBlock;
    }

    private void createLine() {
        line.reset();

        if (startDrawingBlock.intersects(endDrawingBlock)) {
            return;
        }

        WorkspacePoint rightOut = startDrawingBlock.getRightOut();
        WorkspacePoint bottomOut = startDrawingBlock.getBottomOut();
        WorkspacePoint leftIn = endDrawingBlock.getLeftIn();
        WorkspacePoint topIn = endDrawingBlock.getTopIn();

        // Common flowlines
        if (decision == null) {
            // If the endBlock is below the startBlock
            if ((bottomOut.Y + MIN_LINE_LENGTH) <= topIn.Y) {
                bottomToTop(bottomOut, topIn);
            }
            // If above
            else {
                // If the endBlock is to the right of the startBlock
                if ((rightOut.X + MIN_LINE_LENGTH) <= leftIn.X) {
                    rightToLeft(rightOut, leftIn);
                }
                // If is to the left
                else  {
                    bottomToTopAndLeft(bottomOut, leftIn);
                }
            }
        }
        // Decision flowlines
        else {
            if (decision) {
                // If the endBlock is below the startBlock
                if ((bottomOut.Y + MIN_LINE_LENGTH) <= topIn.Y) {
                    bottomToTop(bottomOut, topIn);
                }
                // If above
                else {
                    bottomToTopAndLeft(bottomOut, leftIn);
                }
            }
            else {
                // If the endBlock is below the startBlock
                if ((bottomOut.Y + MIN_LINE_LENGTH) <= topIn.Y) {
                    rightToTopDecision(rightOut, topIn);
                }
                // If above
                else {
                    rightToLeftDecision(rightOut, leftIn);
                }
            }
        }
    }

    private void bottomToTop(WorkspacePoint bottomOut, WorkspacePoint topIn) {
        line.moveTo(bottomOut.X, bottomOut.Y);
        int lengthX = topIn.X - bottomOut.X;
        int halfLengthY = (topIn.Y - bottomOut.Y) / 2;
        line.rLineTo(0, halfLengthY);
        line.rLineTo(lengthX, 0);
        line.lineTo(topIn.X, topIn.Y);
    }

    private void rightToLeft(WorkspacePoint rightOut, WorkspacePoint leftIn) {
        line.moveTo(rightOut.X, rightOut.Y);
        int halfLengthX = (leftIn.X - rightOut.X) / 2;
        line.rLineTo(halfLengthX, 0);
        int lengthY = leftIn.Y - rightOut.Y;
        line.rLineTo(0, lengthY);
        line.lineTo(leftIn.X, leftIn.Y);
    }

    private void bottomToTopAndLeft(WorkspacePoint bottomOut, WorkspacePoint leftIn) {
        line.moveTo(bottomOut.X, bottomOut.Y);
        int blocksBotDY = (startDrawingBlock.startY + startDrawingBlock.height) - (endDrawingBlock.startY + endDrawingBlock.height);
        int dY1 = Math.abs(Math.min(0, blocksBotDY)) + MIN_LINE_LENGTH;
        line.rLineTo(0, dY1);
        int blocksLeftDX = startDrawingBlock.startX - endDrawingBlock.startX;
        int maxHalfWidth = Math.max(startDrawingBlock.width, endDrawingBlock.width) / 2;
        int dX = Math.abs(Math.max(0, blocksLeftDX)) + MIN_LINE_LENGTH + maxHalfWidth;
        line.rLineTo(-dX, 0);
        int dY2 = Math.abs(Math.max(0, blocksBotDY)) + (endDrawingBlock.height / 2) + MIN_LINE_LENGTH;
        line.rLineTo(0, -dY2);
        line.lineTo(leftIn.X, leftIn.Y);
    }

    private void rightToTopDecision(WorkspacePoint rightOut, WorkspacePoint topIn) {
        // TODO Simple implementation
        if (rightOut.X + MIN_LINE_LENGTH > topIn.X) return;

        line.moveTo(rightOut.X, rightOut.Y);
        line.lineTo(topIn.X, rightOut.Y);
        line.lineTo(topIn.X, topIn.Y);
    }

    private void rightToLeftDecision(WorkspacePoint rightOut, WorkspacePoint leftIn) {
        // TODO Simple implementation
        if (rightOut.X + MIN_LINE_LENGTH > leftIn.X) return;

        line.moveTo(rightOut.X, rightOut.Y);
        int halfLengthX = (leftIn.X - rightOut.X) / 2;
        line.rLineTo(halfLengthX, 0);
        int lengthY = leftIn.Y - rightOut.Y;
        line.rLineTo(0, lengthY);
        line.lineTo(leftIn.X, leftIn.Y);
    }
}

package com.demo.flowchart.editor.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.demo.flowchart.editor.view.WorkspaceView;
import com.demo.flowchart.editor.util.WorkspacePoint;

public class Flowline {

    private static final int MIN_LINE_LENGTH = WorkspaceView.GRID_STEP_SMALL * 2;

    private final Block startBlock;
    private final Block endBlock;
    private final Path line;

    public Flowline(Block startBlock, Block endBlock) {
        this.startBlock = startBlock;
        this.endBlock = endBlock;
        line = new Path();
    }

    public void draw(Canvas canvas, Paint paint) {
        createLine();
        if (!line.isEmpty()) {
            canvas.drawPath(line, paint);
        }
    }

    protected boolean isEndBlockSame(Block endBlock) {
        return this.endBlock == endBlock;
    }

    private void createLine() {
        line.reset();

        if (startBlock.intersects(endBlock)) {
            return;
        }

        WorkspacePoint rightOut = startBlock.getRightOut();
        WorkspacePoint bottomOut = startBlock.getBottomOut();
        WorkspacePoint leftIn = endBlock.getLeftIn();
        WorkspacePoint topIn = endBlock.getTopIn();

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

    private void bottomToTop(WorkspacePoint bottomOut, WorkspacePoint topIn) {
        line.moveTo(bottomOut.X, bottomOut.Y);
        int lengthX = topIn.X - bottomOut.X;
        int halfLengthY = (topIn.Y - bottomOut.Y) / 2;
        line.rLineTo(0, halfLengthY);
        line.rLineTo(lengthX, 0);
        line.lineTo(topIn.X, topIn.Y);
        line.addCircle(topIn.X, topIn.Y, 2f, Path.Direction.CW);
    }

    private void rightToLeft(WorkspacePoint rightOut, WorkspacePoint leftIn) {
        line.moveTo(rightOut.X, rightOut.Y);
        int halfLengthX = (leftIn.X - rightOut.X) / 2;
        line.rLineTo(halfLengthX, 0);
        int lengthY = leftIn.Y - rightOut.Y;
        line.rLineTo(0, lengthY);
        line.lineTo(leftIn.X, leftIn.Y);
        line.addCircle(leftIn.X, leftIn.Y, 2f, Path.Direction.CW);
    }

    private void bottomToTopAndLeft(WorkspacePoint bottomOut, WorkspacePoint leftIn) {
        line.moveTo(bottomOut.X, bottomOut.Y);
        int blocksBotDY = (startBlock.startY + startBlock.height) - (endBlock.startY + endBlock.height);
        int dY1 = Math.abs(Math.min(0, blocksBotDY)) + MIN_LINE_LENGTH;
        line.rLineTo(0, dY1);
        int blocksLeftDX = startBlock.startX - endBlock.startX;
        int maxHalfWidth = Math.max(startBlock.width, endBlock.width) / 2;
        int dX = Math.abs(Math.max(0, blocksLeftDX)) + MIN_LINE_LENGTH + maxHalfWidth;
        line.rLineTo(-dX, 0);
        int dY2 = Math.abs(Math.max(0, blocksBotDY)) + (endBlock.height / 2) + MIN_LINE_LENGTH;
        line.rLineTo(0, -dY2);
        line.lineTo(leftIn.X, leftIn.Y);
        line.addCircle(leftIn.X, leftIn.Y, 2f, Path.Direction.CW);
    }

    // DON'T KILL US PLEASE :'(

    private void bottomToLeft(WorkspacePoint bottomOut, WorkspacePoint leftIn) {
        line.moveTo(bottomOut.X, bottomOut.Y);
        int lengthY = leftIn.Y - bottomOut.Y;
        line.rLineTo(0, lengthY);
        line.lineTo(leftIn.X, leftIn.Y);
        line.addCircle(leftIn.X, leftIn.Y, 2f, Path.Direction.CW);
    }

    private void rightToTop(WorkspacePoint rightOut, WorkspacePoint topIn) {
        line.moveTo(rightOut.X, rightOut.Y);
        int lengthX = Math.abs(topIn.X - rightOut.X);
        line.rLineTo(lengthX, 0);
        line.lineTo(topIn.X, topIn.Y);
        line.addCircle(topIn.X, topIn.Y, 2f, Path.Direction.CW);
    }

    private void rightToTopAndLeft(WorkspacePoint rightOut, WorkspacePoint leftIn) {
        line.moveTo(rightOut.X, rightOut.Y);
        int halfLengthX = Math.abs(rightOut.X - leftIn.X) / 2;
        int lengthY = Math.abs(rightOut.Y - leftIn.Y);
        line.rLineTo(halfLengthX, 0);
        line.rLineTo(0, -lengthY);
        line.lineTo(leftIn.X, leftIn.Y);
        line.addCircle(leftIn.X, leftIn.Y, 2f, Path.Direction.CW);
    }
}

package com.demo.flowchart.drawing.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Flowline {

    protected final Block endBlock;
    private final Block startBlock;
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

    private void createLine() {
        line.reset();
//        line.moveTo(startBlock.startX, startBlock.startY);
//        line.lineTo(endBlock.startX, endBlock.startY);
//        line.addCircle(endBlock.startX, endBlock.startY, 2f, Path.Direction.CW);
        testLine();


        /*
        If the X or Y coordinates of the centers of the blocks are equal
        - a straight line from one to the another
        The direction of the line determined by subtracting the differing coordinates

        If there are no equal coordinates
        If the endBlock's top edge on the same line or or above the startBlock's bottom edge
        - the flowline starts from the side edge of the startBlock

        Otherwise the flowline starts from the bottom edge
        ...
        */
    }

    private int startX, startY, endX, endY;
    private void testLine() {
        if (endBlock.startY > startBlock.startY + startBlock.height) {
            startX = startBlock.startX + startBlock.width / 2;
            startY = startBlock.startY + startBlock.height;
            endX = endBlock.startX + endBlock.width / 2;
            endY = endBlock.startY;
        } else {
            startX = startBlock.startX + startBlock.width;
            startY = startBlock.startY + startBlock.height / 2;
            endX = endBlock.startX;
            endY = endBlock.startY + endBlock.height / 2;
        }
        line.moveTo(startX, startY);
        line.lineTo(endX, endY);
        line.addCircle(endX, endY, 2f, Path.Direction.CW);

        if (endBlock.startX <= startBlock.startX + startBlock.width
                && endBlock.startY <= startBlock.startY + startBlock.height) {
            line.reset();
        }
    }

}

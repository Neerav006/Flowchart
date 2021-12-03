package com.demo.flowchart.drawing.model;

import android.text.Layout;
import android.text.StaticLayout;

public class DecisionDrawingBlock extends DrawingBlock {

    public DecisionDrawingBlock(
            long id,
            int startX,
            int startY,
            int width,
            int height,
            String text
    ) {
        super(id, startX, startY, width, height, text);
    }

    // Creating new block
    public DecisionDrawingBlock(long id, int startX, int startY) {
        super(id, startX, startY);
        // flowlines[0] - true line
        // flowlines[1] - false line
        this.flowlines = new DrawingFlowline[2];
    }

    public DecisionDrawingBlock() {
        super();
    }

    @Override
    protected void createContour() {
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        contour.moveTo(startX + halfWidth, startY);
        contour.rLineTo(halfWidth, halfHeight);
        contour.rLineTo(-halfWidth, halfHeight);
        contour.rLineTo(-halfWidth, -halfHeight);
        contour.rLineTo(halfWidth, -halfHeight);
    }

    public boolean hasFlowline(DrawingBlock endDrawingBlock) {
        for (DrawingFlowline flowline : flowlines) {
            if (flowline != null && flowline.isEndBlockSame(endDrawingBlock)) {
                return true;
            }
        }
        return false;
    }

    public void removeFlowline(DrawingBlock endDrawingBlock) {
        int id = getFlowLineId(endDrawingBlock);
        flowlines[id] = null;
    }

    public void setFlowline(DrawingBlock endDrawingBlock, Boolean decision) {
        int id = decision ? 0 : 1;
        flowlines[id] = new DrawingFlowline(this, endDrawingBlock, decision);
    }

    @Override
    public void resize() {
        int desiredHeight = DEFAULT_HEIGHT / 2;
        int desiredWidth = DEFAULT_WIDTH / 2;

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

        height = desiredHeight * 2;
        adjustSize();
        bindToGrid();
    }

    @Override
    protected int textHorizontalPadding() {
        return 0;
    }

    @Override
    protected int textVerticalPadding() {
        return 0;
    }

    @Override
    protected int textHorizontalSize() {
        return width / 2;
    }

    private int getFlowLineId(DrawingBlock endDrawingBlock) {
        for (int i = 0; i < flowlines.length; i++) {
            if (flowlines[i] != null && flowlines[i].isEndBlockSame(endDrawingBlock)) {
                return i;
            }
        }
        return -1;
    }
}

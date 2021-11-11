package com.demo.flowchart.editor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.flowchart.editor.model.DrawingBlock;

public class BlockView extends View {

    private static final float MAGIC_NUMBER_ONE = 0.25f;
    private static final float MAGIC_NUMBER_TWO = 6;

    public DrawingBlock drawingBlock;
    Paint paint = new Paint();

    public BlockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void setBlock(DrawingBlock drawingBlock) {
        this.drawingBlock = drawingBlock;
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float scaleX = (float) getWidth() / drawingBlock.getWidth();
        float scaleY = (float) getHeight() / drawingBlock.getHeight();
        canvas.scale(scaleX - MAGIC_NUMBER_ONE, scaleY - MAGIC_NUMBER_ONE);
        canvas.translate(MAGIC_NUMBER_TWO, MAGIC_NUMBER_TWO);
        drawingBlock.draw(canvas, paint);
    }
}

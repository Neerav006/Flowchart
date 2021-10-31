package com.demo.flowchart.drawing.util;

import android.view.MotionEvent;

public class EventPoint {

    public int X, Y;

    public EventPoint(MotionEvent event) {
        this.X = Math.round(event.getX());
        this.Y = Math.round(event.getY());
    }
}

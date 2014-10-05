/*
 * CircleView.java
 *
 * Copyright (c) 2014 Amazon.com, Inc. or its affiliates. All rights reserved.
 *
 * Use is subject to license terms.
 */
package com.zappos.firephone.view;

import android.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import com.amazon.headtracking.HeadTrackingEvent;

/**
 * A custom View that takes HeadTrackingEvents and draws a circle
 * on the screen relative to where the user's head is.
 */
public class CircleView extends View {
    // Can't get the device much closer to the face than this before the face is too large.
    private static final float MIN_HEAD_DISTANCE = 150.0f; // millimeters
    // Rough guess of the max head distance value when a user is holding the device as far away from face as possible.
    private static final float MAX_HEAD_DISTANCE = 700.0f; // millimeters
    // Minimum circle radius
    private static final float CIRCLE_MIN_RADIUS = 10.0f; // pixels

    // Paint for the circle.
    private final Paint mCircleFillPaint = new Paint();
    // Most recent head tracking event.
    private HeadTrackingEvent mEvent;

    /**
     * Default constructor.
     */
    public CircleView(Context context) {
        this(context, null);
    }

    /**
     * Default constructor taking an attribute set.
     */
    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Default constructor taking an attribute set, with a defined style.
     * Initiate the color for drawing Circles.
     */
    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // Configuring paint object to draw Circle
        final int holoBlue = context.getResources().getColor(color.holo_blue_dark);
        mCircleFillPaint.setColor(holoBlue);
        mCircleFillPaint.setAntiAlias(true);
    }

    /**
     * Call onDraw(canvas) to update the View with the latest event.
     */
    public void setHeadTrackingData(HeadTrackingEvent event) {
        mEvent = event;
        // Calls onDraw(canvas).
        // HeadTracking events are consumed on a non-UI thread,
        // thus postInvalidate() is required instead of invalidate().
        postInvalidate();
    }

    /**
     * Draw the Circle on the canvas.
     * Calculates the position of the user's head given a HeadTrackingEvent,
     * and draws a Circle relative to that position. The Circle's size is
     * scaled to the distance of the user's head from the screen.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        synchronized (this) {
            // Update the Circle if the head is tracking.
            PointF circleCenter;
            if (mEvent != null && mEvent.isTracking) {
                // Calculate Euclidean distance.
                float headDistance = (float)Math.sqrt(mEvent.x_mm*mEvent.x_mm +
                                                      mEvent.y_mm*mEvent.y_mm +
                                                      mEvent.z_mm*mEvent.z_mm);
                // Bound the head distance to a given range.
                headDistance = boundToRange(MIN_HEAD_DISTANCE, MAX_HEAD_DISTANCE, headDistance);
                // Scale head distance to circle radius on a exponential scale to more easily discern a change in size.
                float exponent = (MAX_HEAD_DISTANCE - headDistance) / (MAX_HEAD_DISTANCE - MIN_HEAD_DISTANCE);
                float radius = CIRCLE_MIN_RADIUS * (float) Math.pow(10.0, exponent);
                // Scale head position to screen position.
                circleCenter = getScreenCoordinatesFromHead(mEvent.x_mm, mEvent.y_mm, mEvent.z_mm);
                // Draw the Circle.
                canvas.drawCircle(circleCenter.x, circleCenter.y, radius, mCircleFillPaint);
            }
        }
    }

    /**
     * Translate HeadTracking coordinates from head-space to screen-space:
     *
     * Tilting the device to 45 degrees moves the Circle to the edge of the screen.
     *
     * If the user's head is beyond 45 degrees away from the center of the
     * device, the circle will move off screen; at these edges the device has trouble
     * tracking the user's head.
     *
     * Calculates the screen coordinates based on head angle on the X and Y dimensions.
     */
    private PointF getScreenCoordinatesFromHead(float headX, float headY, float headZ) {
        final int halfBoxWidth = getWidth() / 2;
        final int halfBoxHeight = getHeight() / 2;

        // Instantiate a point to hold X and Y coordinates.
        // Invert the Y coordinate, y-axis direction for HeadTracking
        // is the inverse of y-axis for screen coordinates.
        final PointF point = new PointF( halfBoxWidth * headX / headZ,
                                       - halfBoxHeight * headY / headZ );

        // The above x and y were calculated assuming (0,0) is in the center of the device but
        // the canvas' (0,0) is top-left so adjust accordingly.
        point.offset(halfBoxWidth, halfBoxHeight);

        return point;
    }

    /**
     * Bound a given value to a range.
     */
    private float boundToRange(float min, float max, float value) {
        value = Math.max(min, value);
        value = Math.min(max, value);
        return value;
    }
}

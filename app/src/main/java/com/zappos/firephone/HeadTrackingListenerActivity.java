/*
 * HeadTrackingListenerActivity.java
 *
 * Copyright (c) 2014 Amazon.com, Inc. or its affiliates. All rights reserved.
 *
 * Use is subject to license terms.
 */
package com.zappos.firephone;

import android.util.Log;
import android.widget.TextView;
import com.amazon.headtracking.HeadTrackingEvent;
import com.amazon.headtracking.HeadTrackingListener;
import com.amazon.headtracking.HeadTrackingManager;

import android.app.Activity;
import android.os.Bundle;

/**
 * Listens for HeadTrackingEvents.
 * Displays updated X, Y, and Z coordinates every new event.
 */
public class HeadTrackingListenerActivity extends Activity implements HeadTrackingListener {
    // TAG for use in logging.
    private final static String TAG = "HeadTracking Sample HeadTrackingListenerActivity";
    // HeadTrackingManager to register for HeadTrackingEvent.
    private HeadTrackingManager mHeadTrackingManager;
    // X-Coordinate TextView.
    private TextView mXCoor;
    // Y-Coordinate TextView.
    private TextView mYCoor;
    // Z-Coordinate TextView.
    private TextView mZCoor;
    // FaceDetected TextView.
    private TextView mFaceDetected;
    // HeadTracking TextView.
    private TextView mHeadTracking;

    /**
     * Called when the activity is first created.
     * Create an instance of HeadTrackingManager.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the current HeadTrackingManager for this application.
        try {
            mHeadTrackingManager = HeadTrackingManager.createInstance(this);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "No HeadTrackingManager is available for this application", e);
        }
        setContentView(R.layout.tracking);
        ((TextView) this.findViewById(R.id.tracking_label)).setText(TAG);
        mXCoor = (TextView) this.findViewById(R.id.x_coor);
        mYCoor = (TextView) this.findViewById(R.id.y_coor);
        mZCoor = (TextView) this.findViewById(R.id.z_coor);
        mFaceDetected = (TextView) this.findViewById(R.id.face_detected);
        mHeadTracking = (TextView) this.findViewById(R.id.head_tracking);
    }

    /**
     * Called every time the activity is launched.
     * Registers for a HeadTrackingListener.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mHeadTrackingManager != null) {
            /**
             * Register a new Listener.
             *
             * By default, this causes the onHeadTrackingEvent callback to be processed
             * in the main thread. Alternatively, you can register the listener with a
             * HeadTrackingListenerConfig specified, in which you can set a Handler.
             *
             * e.g.
             * HeadTrackingListenerConfiguration mHTConfig = new HeadTrackingListenerConfiguration();
             * mHTConfig.setHandler(mHandler);
             * mHeadTrackingManager.registerListener(this, mHTConfig);
             *
             * In this case, the specified Handler will receive HeadTrackingEvents instead.
             */
            mHeadTrackingManager.registerListener(this);
        }
    }

    /**
     * Called upon application pause or shutdown.
     * Release the HeadTrackingListener.
     */
    @Override
    public void onPause() {
        if (mHeadTrackingManager != null) {
            // Release the Listener.
            mHeadTrackingManager.unregisterListener(this);
        }
        super.onPause();
    }

    /**
     * Listen for HeadTrackingEvents.
     */
    @Override
    public void onHeadTrackingEvent(HeadTrackingEvent event) {
        // Print event details.
        printEvent(event);
    }

    /**
     * Takes a HeadTrackingEvent, and updates the main View with event details.
     */
    private void printEvent(HeadTrackingEvent event) {
        // Event details.
        final float x_coor = event.x_mm;
        final float y_coor = event.y_mm;
        final float z_coor = event.z_mm;

        final boolean face = event.isFaceDetected;
        final boolean tracking = event.isTracking;

        // Updating the view.
        mXCoor.setText(getString(R.string.xcoor, x_coor));
        mYCoor.setText(getString(R.string.ycoor, y_coor));
        mZCoor.setText(getString(R.string.zcoor, z_coor));

        mFaceDetected.setText(getString(R.string.facedetected) + face);
        mHeadTracking.setText(getString(R.string.headtracking) + tracking);
    }
}

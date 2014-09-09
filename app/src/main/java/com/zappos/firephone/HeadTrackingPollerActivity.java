/*
 * HeadTrackingPollingActivity.java
 *
 * Copyright (c) 2014 Amazon.com, Inc. or its affiliates. All rights reserved.
 *
 * Use is subject to license terms.
 */
package com.zappos.firephone;

import android.util.Log;
import android.widget.TextView;
import com.amazon.headtracking.HeadTrackingEvent;
import com.amazon.headtracking.HeadTrackingManager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import com.amazon.headtracking.HeadTrackingPoller;

/**
 * Polls for HeadTrackingEvents.
 * Displays updated X, Y, and Z coordinates every 50 milliseconds.
 */
public class HeadTrackingPollerActivity extends Activity {
    // TAG for use in logging.
    private final static String TAG = "HeadTracking Sample HeadTrackingPollerActivity";
    // HeadTrackingManager to register for HeadTrackingEvent.
    private HeadTrackingManager mHeadTrackingManager;
    // HeadTrackingPoller to poll for HeadTrackingEvents.
    private HeadTrackingPoller mHeadTrackingPoller;
    // HeadTrackingEvent from the event pool.
    private HeadTrackingEvent mHeadTrackingEvent;
    // Time interval for polling, in milliseconds.
    private static final int INTERVAL = 50;
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
    // UI Handler.
    private Handler mUIHandler;
    // Runnable for refreshing UI.
    private RefreshUIRunnable mRefreshUIRunnable;

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
     * Registers for a HeadTrackingPoller, and obtains a HeadTrackingEvent.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mHeadTrackingManager != null) {
            // Register a new poller.
            mHeadTrackingPoller = mHeadTrackingManager.registerPoller();
            // Obtain an event from the event pool.
            mHeadTrackingEvent = HeadTrackingEvent.obtain();

            /**
             * Create a new handler that posts a Runnable which will poll for HeadTrackingEvents and
             * update the UI appropriately. The Runnable will callback to this Handler, posting itself
             * every 50 milliseconds, an arbitrary rate. Must run on the UI thread to update the UI.
             *
             * An application can poll at any desired rate to meet its needs, however, the API is
             * only capable of sampling once every 10 milliseconds. Any sampling above this rate will
             * return duplicate data.
             *
             * The recommended polling rate is one that matches your UI updates, e.g. 30 FPS.
             */
            mUIHandler = new Handler();
            mRefreshUIRunnable = new RefreshUIRunnable();
            mUIHandler.postDelayed(mRefreshUIRunnable, INTERVAL);
        }
    }

    /**
     * Called upon application pause or shutdown.
     * Release the HeadTrackingEvent and Poller.
     */
    @Override
    public void onPause() {
        if (mHeadTrackingManager != null) {
            // Remove any pending posts of a Runnable.
            mUIHandler.removeCallbacks(mRefreshUIRunnable);
            // Release the HeadTrackingEvent.
            mHeadTrackingEvent.recycle();
            // Release the Poller.
            mHeadTrackingManager.unregisterPoller(mHeadTrackingPoller);
        }
        super.onPause();
    }

    /**
     * A private Runnable class that polls for the latest HeadTrackingEvent, and
     * updates the UI accordingly.
     */
    private class RefreshUIRunnable implements Runnable {
        @Override
        public void run() {
            // Poll for the latest HeadTrackingEvent.
            mHeadTrackingPoller.sample(mHeadTrackingEvent);

            // Update the UI.
            mXCoor.setText(getString(R.string.xcoor, mHeadTrackingEvent.x_mm));
            mYCoor.setText(getString(R.string.ycoor, mHeadTrackingEvent.y_mm));
            mZCoor.setText(getString(R.string.zcoor, mHeadTrackingEvent.z_mm));

            mFaceDetected.setText(getString(R.string.facedetected) + mHeadTrackingEvent.isFaceDetected);
            mHeadTracking.setText(getString(R.string.headtracking) + mHeadTrackingEvent.isTracking);

            // Callback to post this Runnable again.
            mUIHandler.postDelayed(this, INTERVAL);
        }
    }
}

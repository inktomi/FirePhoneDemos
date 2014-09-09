/*
 * HeadTrackingMainActivity.java
 *
 * Copyright (c) 2014 Amazon.com, Inc. or its affiliates. All rights reserved.
 *
 * Use is subject to license terms.
 */
package com.zappos.firephone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Main Activity for the HeadTracking Sample Application.
 * Allows user to select an Activity that will launch either a HeadTracking Poller or Listener.
 */
public class HeadTrackingMainActivity extends Activity {
    // TAG for use in logging.
    private final static String TAG = "HeadTracking Sample HeadTrackingMainActivity";
    // Poller Activity.
    private final Class<HeadTrackingPollerActivity> POLLER_ACTIVITY = HeadTrackingPollerActivity.class;
    // Listener Activity.
    private final Class<HeadTrackingListenerActivity> LISTENER_ACTIVITY = HeadTrackingListenerActivity.class;
    // Circle Activity.
    private final Class<HeadTrackingCircleActivity> CIRCLE_ACTIVITY = HeadTrackingCircleActivity.class;

    /**
     * Called when Activity is first created, draw SampleApplication.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
    }

    /**
     * Click handler defined and set in the layout.
     * Launches a HeadTrackingPoller.
     *
     * @param clicked The View that was clicked.
     */
    public void launchPoller(View clicked) {
        Log.i(TAG, "Launch " + POLLER_ACTIVITY);
        startActivity(new Intent(this, POLLER_ACTIVITY));
    }

    /**
     * Click handler defined and set in the layout.
     * Launches a HeadTrackingListener.
     *
     * @param clicked The View that was clicked.
     */
    public void launchListener(View clicked) {
        Log.i(TAG, "Launch " + LISTENER_ACTIVITY);
        startActivity(new Intent(this, LISTENER_ACTIVITY));
    }

    /**
     * Click handler defined and set in the layout.
     * Launches HeadTrackingCircleActivity.
     *
     * @param clicked The View that was clicked.
     */
    public void launchCircle(View clicked) {
        Log.i(TAG, "Launch " + CIRCLE_ACTIVITY);
        startActivity(new Intent(this, CIRCLE_ACTIVITY));

    }
}

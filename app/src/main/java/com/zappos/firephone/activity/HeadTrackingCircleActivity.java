/*
 * HeadTrackingCircleActivity.java
 *
 * Copyright (c) 2014 Amazon.com, Inc. or its affiliates. All rights reserved.
 *
 * Use is subject to license terms.
 */
package com.zappos.firephone.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.amazon.headtracking.HeadTrackingConfiguration;
import com.amazon.headtracking.HeadTrackingEvent;
import com.amazon.headtracking.HeadTrackingListener;
import com.amazon.headtracking.HeadTrackingListenerConfiguration;
import com.amazon.headtracking.HeadTrackingManager;
import com.zappos.firephone.R;
import com.zappos.firephone.view.CircleView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Listens for HeadTrackingEvents, and draws a CircleView.
 * The CircleView draws a circle on the screen based on the user's head,
 * adjusting the size of the circle depending on how far the head is from
 * the device.
 */
public class HeadTrackingCircleActivity extends Activity implements HeadTrackingListener {
    // TAG for use in logging.
    private final static String TAG = "HeadTracking Sample HeadTrackingCircleActivity";
    private CircleView mCircleView;
    // HeadTrackingManager to register for HeadTrackingEvent.
    private HeadTrackingManager mHeadTrackingManager;

    private HeadTrackingListenerConfiguration mHiConfig;
    private HeadTrackingListenerConfiguration mLoConfig;

    @InjectView(R.id.rb)
    RadioGroup mRb;

    @InjectView(R.id.rb_hi)
    RadioButton mRbHi;

    @InjectView(R.id.rb_lo)
    RadioButton mRbLo;

    /**
     * Called when the activity is first created.
     * Create an instance of HeadTrackingManager.
     * Draw a CircleView.
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
        setContentView(R.layout.circle);

        ButterKnife.inject(this);

        // Grab a reference to the CircleView.
        mCircleView = (CircleView) findViewById(R.id.circle_view);

        mHiConfig = new HeadTrackingListenerConfiguration();
        mHiConfig.setFidelity(HeadTrackingConfiguration.Fidelity.HIGH);
        mHiConfig.setRate(HeadTrackingListenerConfiguration.MIN_RATE_HZ);

        mLoConfig = new HeadTrackingListenerConfiguration();
        mLoConfig.setFidelity(HeadTrackingConfiguration.Fidelity.LOW_POWER);
        mHiConfig.setRate(HeadTrackingListenerConfiguration.MAX_RATE_HZ);
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
     * Called every time the activity is launched.
     * Registers for a HeadTrackingListener.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mHeadTrackingManager != null) {
            // Register a new Listener.

            if (mRbHi.isChecked()) {
                Log.d(TAG, "onResume, registering hi config");
                mHeadTrackingManager.registerListener(this, mHiConfig);
            } else {
                Log.d(TAG, "onResume, registering low config");
                mHeadTrackingManager.registerListener(this, mLoConfig);
            }
        }
    }

    /**
     * Listen for HeadTrackingEvents.
     */
    @Override
    public void onHeadTrackingEvent(HeadTrackingEvent event) {
        // Update the CircleView with latest HeadTracking data.
        mCircleView.setHeadTrackingData(event);
    }

    /**
     * Listen for device configuration changes and continue tracking.
     * Defined so that the activity doesn't restart on device rotate.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @OnClick(R.id.rb_hi)
    public void hiClicked() {
        reregisterListener();
    }

    @OnClick(R.id.rb_lo)
    public void loClicked() {
        reregisterListener();
    }

    private void reregisterListener() {
        if (mHeadTrackingManager != null) {
            // Release the Listener.
            mHeadTrackingManager.unregisterListener(this);
        }
        if (mRbHi.isChecked()) {
            Log.d(TAG, "radio clicked, registering hi config");
            mHeadTrackingManager.registerListener(this, mHiConfig);
        } else {
            Log.d(TAG, "radio clicked, registering low config");
            mHeadTrackingManager.registerListener(this, mLoConfig);
        }
    }
}

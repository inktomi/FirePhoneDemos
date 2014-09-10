package com.zappos.firephone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends Activity {

    @InjectView(R.id.btn_head_tracking)
    Button btnHeadTracking;

    @InjectView(R.id.btn_gestures)
    Button btnGestures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.btn_head_tracking)
    public void headTrackingClicked() {
        startActivity(new Intent(this, HeadTrackingCircleActivity.class));
    }

    @OnClick(R.id.btn_gestures)
    public void gesturesClicked() {
        startActivity(new Intent(this, GestureActivity.class));
    }

}

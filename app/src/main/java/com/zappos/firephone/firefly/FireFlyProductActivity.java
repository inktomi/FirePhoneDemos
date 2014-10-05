package com.zappos.firephone.firefly;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zappos.firephone.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FireFlyProductActivity extends Activity {

    @InjectView(R.id.tv_title)
    TextView mTvTitle;

    @InjectView(R.id.tv_upc)
    TextView mTvUpc;

    @InjectView(R.id.rb_ratings)
    RatingBar mRbRating;


    // Get the intent that started this Activity and display the associated product data.
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_fire_fly_product);

        ButterKnife.inject(this);

        // Get the intent that started this activity.
        Intent callerIntent = getIntent();
        final String title = callerIntent.getStringExtra(FireFlyDigitalEntityUI.EXTRA_TITLE);
        final String upc = callerIntent.getStringExtra(FireFlyDigitalEntityUI.EXTRA_UPC);
        final float rating = callerIntent.getFloatExtra(FireFlyDigitalEntityUI.EXTRA_RATING, 0);

        mTvTitle.setText(title);
        mTvUpc.setText("UPC: " + upc);
        mRbRating.setRating(rating);
    }
}

package com.zappos.firephone.firefly;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.zappos.firephone.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jitse on 10/5/14.
 */
public class FireFlyPhoneActivity extends Activity {

    @InjectView(R.id.tv_phone)
    TextView mTvPhone;

    // Get the intent that started this Activity and display the associated product data.
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_fire_fly_phone);

        ButterKnife.inject(this);

        final String phone = getIntent().getStringExtra(FireFlyDigitalEntityUI.EXTRA_PHONE);
        mTvPhone.setText(phone);
    }
}

package com.zappos.firephone.activity;

import android.os.Bundle;

import com.zappos.firephone.R;

import butterknife.ButterKnife;


@SuppressWarnings("unused")
public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
    }


}

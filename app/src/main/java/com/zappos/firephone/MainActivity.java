package com.zappos.firephone;

import android.os.Bundle;

import butterknife.ButterKnife;


@SuppressWarnings("unused")
public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
    }


}

package com.zappos.firephone.activity;

import android.os.Bundle;

import com.zappos.firephone.R;

import butterknife.ButterKnife;


@SuppressWarnings("unused")
/**
 * The home page. Nothing special here.
 */
public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        //locking right panel because we don't have anything to show for on the right
        lockRightPanel();
    }


}

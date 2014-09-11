package com.zappos.firephone;

import android.os.Bundle;
import android.util.Log;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;


@SuppressWarnings("unused")
public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getName();

    // Integer to store current badge number.
    private static int mBadgeNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
    }

    /**
     * Click handler defined and set in the layout.
     * Increments the numeric badge on the app icon.
     */
    @OnClick(R.id.btn_increment_numeric_badge_number)
    public void onIncrementNumericBadge() {
        Log.v(TAG, "onIncreaseNumericBadge, incrementing mBadgeNumber to " + (mBadgeNumber + 1));
        mHomeManager.updateNumericBadge(++mBadgeNumber);
    }

    /**
     * Click handler defined and set in the layout.
     * Decreases the numeric badge on the app icon, does nothing if badge at 0.
     */
    @OnClick(R.id.btn_decrement_numeric_badge_number)
    public void onDecrementNumericBadge() {
        if (mBadgeNumber > 0) {
            Log.v(TAG, "onDecreaseNumericBadge, decrementing mBadgeNumber to " + (mBadgeNumber-1));
            mHomeManager.updateNumericBadge(--mBadgeNumber);
        }
    }
}

package com.zappos.firephone;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * This activity demonstrates the FoneVOIP application's functionality.
 * It will consume the intent launched by the FoneVOIPPlugin and display
 * the phone number identified.
 */
public class FoneVOIPActivity extends Activity {
    // PhoneNumber TextView
    private TextView mPhoneNumber;

    // Get the intent that started this Activity and display the associated phonenumber data.
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.main);

        // Get the intent that started this activity.
        Intent callerIntent = getIntent();
        final String phoneNumber = callerIntent.getStringExtra(Intent.EXTRA_TEXT);

        // Update the phonenumber TextView.
        mPhoneNumber = (TextView) this.findViewById(R.id.phone_textview);

        /* This activity is meant to be launched from the Firefly application and associated
         * with an identified phonenumber.
         *
         * If it is launched any other way, display text describing its intended usage.
         */
        if (phoneNumber != null) {
            mPhoneNumber.setText(getString(R.string.phonenumber, phoneNumber));
        } else {
            mPhoneNumber.setText(getString(R.string.intended_usage));
        }
    }
}

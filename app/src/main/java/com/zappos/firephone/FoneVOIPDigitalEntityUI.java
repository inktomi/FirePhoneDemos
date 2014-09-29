package com.zappos.firephone;

import android.content.Intent;

import com.amazon.mw.entity.DigitalEntity;
import com.amazon.mw.entity.FacetType;
import com.amazon.mw.entity.PhoneNumberFacet;
import com.amazon.mw.plugin.DigitalEntityUI;
import com.amazon.mw.plugin.Label;
import com.amazon.mw.plugin.SimpleLabel;

/**
 * Define the DigitalEntityUI class for the FoneVOIP plugin.
 * This UI is what is displayed once Firefly recognizes a DigitalEntity.
 */
public class FoneVOIPDigitalEntityUI extends DigitalEntityUI {
    // Construct a DigitalEntityUI
    public FoneVOIPDigitalEntityUI(DigitalEntity digitalEntity) {
        super(digitalEntity);
    }

    // Generate a label describing this Plugin's action.
    @Override
    public Label getLabel() {
        // Create a label with the identified PhoneNumberFacet.
        SimpleLabel label = new SimpleLabel();
        label.setExperienceDescriptor(getContext().getString(R.string.plugin_label, getPhoneNumber()));

        return label;
    }

    // Define an onClick() intent to send the identified phone number to FoneVOIPActivity.
    @Override
    public void onClick() {
        // Create an intent to send the phone number to the FoneVOIPActivity.
        Intent sendNumber = new Intent(getContext(), FoneVOIPActivity.class);
        // Ensure that this Activity is marked as new, bringing it to focus.
        sendNumber.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendNumber.putExtra(Intent.EXTRA_TEXT, getPhoneNumber());

        // Start the FoneVOIPActivity.
        getContext().startActivity(sendNumber);
    }

    /**
     * Get the phone number from the identified PhoneNumberFacet.
     */
    private String getPhoneNumber() {
        PhoneNumberFacet phoneFacet = getDigitalEntity().getFacet(FacetType.PHONENUMBER);
        return phoneFacet.getPhoneNumber();
    }
}
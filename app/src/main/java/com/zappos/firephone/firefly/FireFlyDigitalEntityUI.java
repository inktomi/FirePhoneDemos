package com.zappos.firephone.firefly;

import android.content.Intent;

import com.amazon.mw.entity.DigitalEntity;
import com.amazon.mw.entity.FacetType;
import com.amazon.mw.entity.ProductDetailsFacet;
import com.amazon.mw.plugin.DigitalEntityUI;
import com.amazon.mw.plugin.Label;
import com.amazon.mw.plugin.SimpleLabel;
import com.zappos.firephone.R;

/**
 * Define the DigitalEntityUI class for the fire fly plugin.
 * This UI is what is displayed once Firefly recognizes a DigitalEntity.
 */
public class FireflyDigitalEntityUI extends DigitalEntityUI {


    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_UPC = "extra_upc";
    public static final String EXTRA_RATING = "extra_rating";

    // Construct a DigitalEntityUI
    public FireflyDigitalEntityUI(DigitalEntity digitalEntity) {
        super(digitalEntity);
    }

    // Generate a label describing this Plugin's action. This will show in the firefly details section
    @Override
    public Label getLabel() {
        SimpleLabel label = new SimpleLabel();
        label.setExperienceDescriptor(getContext().getString(R.string.plugin_label, "No label found"));

        return label;
    }

    // Define an onClick() intent to send the identified product to FireflyActivity.
    @Override
    public void onClick() {
        // Create an intent to send the product information to the FireflyActivity.
        Intent sendNumber = new Intent(getContext(), FireflyActivity.class);
        // Ensure that this Activity is marked as new, bringing it to focus.
        sendNumber.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        ProductDetailsFacet facet = getDigitalEntity().getFacet(FacetType.PRODUCT);

        sendNumber.putExtra(EXTRA_TITLE, facet.getTitle());
        sendNumber.putExtra(EXTRA_UPC, facet.getUPC());
        sendNumber.putExtra(EXTRA_RATING, facet.getCustomerRating());

        // Start the FireflyActivity.
        getContext().startActivity(sendNumber);
    }
}
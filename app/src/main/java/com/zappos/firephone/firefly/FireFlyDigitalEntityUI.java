package com.zappos.firephone.firefly;

import android.content.Intent;

import com.amazon.mw.entity.DigitalEntity;
import com.amazon.mw.entity.FacetType;
import com.amazon.mw.entity.PhoneNumberFacet;
import com.amazon.mw.entity.ProductDetailsFacet;
import com.amazon.mw.plugin.DigitalEntityUI;
import com.amazon.mw.plugin.Label;
import com.amazon.mw.plugin.SimpleLabel;
import com.zappos.firephone.R;

/**
 * Define the DigitalEntityUI class for the fire fly plugin.
 * This UI is what is displayed once Firefly recognizes a DigitalEntity.
 */
public class FireFlyDigitalEntityUI extends DigitalEntityUI {


    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_PHONE = "extra_phone";
    public static final String EXTRA_UPC = "extra_upc";
    public static final String EXTRA_RATING = "extra_rating";

    // Construct a DigitalEntityUI
    public FireFlyDigitalEntityUI(DigitalEntity digitalEntity) {
        super(digitalEntity);
    }

    // Generate a label describing this Plugin's action. This will show in the firefly details section
    @Override
    public Label getLabel() {
        SimpleLabel label = new SimpleLabel();

        if (getDigitalEntity().getFacet(FacetType.PRODUCT) != null) {
            label.setExperienceDescriptor(getContext().getString(R.string.product_label, "No label found"));
        } else if (getDigitalEntity().getFacet(FacetType.PHONENUMBER) != null) {
            label.setExperienceDescriptor(getContext().getString(R.string.phone_label, "No label found"));
        }
        return label;
    }

    // Define an onClick() intent to send the identified product to FireflyActivity.
    @Override
    public void onClick() {
        ProductDetailsFacet facet = getDigitalEntity().getFacet(FacetType.PRODUCT);
        if (facet != null) {
            handleProduct(facet);
        } else {
            PhoneNumberFacet phoneFacet = getDigitalEntity().getFacet(FacetType.PHONENUMBER);
            if (phoneFacet != null) {
                handlePhoneFacet(phoneFacet);
            }
        }
    }

    private void handlePhoneFacet(PhoneNumberFacet facet) {
        // Create an intent to send the product information to the FireFlyPhoneActivity.
        Intent phoneIntent = new Intent(getContext(), FireFlyPhoneActivity.class);

        // Ensure that this Activity is marked as new, bringing it to focus.
        phoneIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        phoneIntent.putExtra(EXTRA_PHONE, facet.getPhoneNumber());
        getContext().startActivity(phoneIntent);
    }

    private void handleProduct(ProductDetailsFacet facet) {
        // Create an intent to send the product information to the FireFlyProductActivity.
        Intent productIntent = new Intent(getContext(), FireFlyProductActivity.class);
        // Ensure that this Activity is marked as new, bringing it to focus.
        productIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        productIntent.putExtra(EXTRA_TITLE, facet.getTitle());
        productIntent.putExtra(EXTRA_UPC, facet.getUPC());
        productIntent.putExtra(EXTRA_RATING, facet.getCustomerRating());

        // Start the FireflyActivity.
        getContext().startActivity(productIntent);
    }
}
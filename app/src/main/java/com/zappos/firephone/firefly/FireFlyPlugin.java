package com.zappos.firephone.firefly;

import android.util.Log;

import com.amazon.mw.entity.DigitalEntity;
import com.amazon.mw.entity.DigitalEntityFilter;
import com.amazon.mw.entity.FacetType;
import com.amazon.mw.plugin.DigitalEntityUI;
import com.amazon.mw.plugin.PluginError;
import com.amazon.mw.plugin.SimplePlugin;
import com.zappos.firephone.R;

/**
 * For firefly. This is where you declare the type of DigitalEntities your app can handle.
 * This one filters out everything except phone numbers and products.
 */
public class FireFlyPlugin extends SimplePlugin {
    // TAG used for logging
    private static String TAG = FireFlyPlugin.class.getSimpleName();

    /**
     * Configure the plugin to resolve product identifications by
     * adding the PRODUCT facet type to the DigitalEntityFilter object.
     * @return a DigitalEntityFilter
     */
    @Override
    public DigitalEntityFilter getDigitalEntityFilter() {
        DigitalEntityFilter filter = new DigitalEntityFilter();
        filter.addAnyOfFacets(FacetType.PRODUCT, FacetType.PHONENUMBER);
        return filter;
    }

    /**
     * Define a factory method to create a DigitalEntityUI object for this plugin.
     * @param digitalEntity the DigitalEntityUI
     * @return a FireFlyDigitalEntityUI
     */
    @Override
    public DigitalEntityUI createDigitalEntityUI(DigitalEntity digitalEntity) {
        return new FireFlyDigitalEntityUI(digitalEntity);
    }

    /**
     * Define an error callback in case something goes wrong; for example, the service takes
     * too long to respond and Firefly gives up.
     * @param error the PluginError
     */
    @Override
    public void onError(PluginError error) {
        Log.e(TAG, "Error: " + error.getMessage());
    }

    /**
     * Return a one line description of the plugin's function.
     * @return the description
     */
    @Override
    public String getPluginDescription() {
        return getContext().getString(R.string.description);
    }
}


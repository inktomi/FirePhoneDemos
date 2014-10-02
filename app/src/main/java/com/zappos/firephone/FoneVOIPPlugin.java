package com.zappos.firephone;

import android.util.Log;

import com.amazon.mw.entity.DigitalEntity;
import com.amazon.mw.entity.DigitalEntityFilter;
import com.amazon.mw.entity.FacetType;
import com.amazon.mw.plugin.DigitalEntityUI;
import com.amazon.mw.plugin.PluginError;
import com.amazon.mw.plugin.SimplePlugin;

/**
 * Created by jitse on 9/16/14.
 */
public class FoneVOIPPlugin extends SimplePlugin {
    // TAG used for logging
    private static String TAG = FoneVOIPPlugin.class.getSimpleName();

    // Configure the plugin to resolve phone number identifications by
    // adding the PHONENUMBER facet type to the DigitalEntityFilter object.
    @Override
    public DigitalEntityFilter getDigitalEntityFilter() {
        DigitalEntityFilter filter = new DigitalEntityFilter();
        filter.addRequiredFacets(FacetType.PHONENUMBER);
        return filter;
    }

    // Define a factory method to create a DigitalEntityUI object for this plugin.
    @Override
    public DigitalEntityUI createDigitalEntityUI(DigitalEntity digitalEntity) {
        return new FoneVOIPDigitalEntityUI(digitalEntity);
    }

    // Define an error callback in case something goes wrong; for example, the service takes
    // too long to respond and Firefly gives up.
    @Override
    public void onError(PluginError error) {
        Log.e(TAG, "Error: " + error.getMessage());
    }

    // Return a one line description of the plugin's function.
    @Override
    public String getPluginDescription() {
        return getContext().getString(R.string.description);
    }
}


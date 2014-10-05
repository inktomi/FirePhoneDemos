package com.zappos.firephone.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amazon.device.home.GroupedGridHeroWidget;
import com.amazon.device.home.HeroWidgetIntent;
import com.zappos.firephone.R;
import com.zappos.firephone.receiver.WidgetBroadcastReceiver;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Demonstrates how to use the home widget APIs for the Fire Phone
 *
 * Created by mattruno on 9/11/14.
 */
@SuppressWarnings("unused")
public class HomeWidgetActivity extends BaseActivity {

    private static final String TAG = HomeWidgetActivity.class.getSimpleName();

    // Use this Receiver as the intent target for GridEntries in the app widget.
    private static final String TARGET_CLASS_NAME = WidgetBroadcastReceiver.class.getName();

    // references to our images for the widget
    private final static Integer[] THUMBNAIL_IDS = {
            R.drawable.saucony,
            R.drawable.nb,
            R.drawable.converse,
            R.drawable.sketchers,
            R.drawable.keen,
            R.drawable.brooks
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_widget);

        ButterKnife.inject(this);
    }

    /**
     * Click handler defined and set in the layout.
     * Populates app widget with GroupedGridHeroWidget.GridEntries.
     */
    @OnClick(R.id.btn_set_groupedgridwidget)
    public void onSetGroupedGrid() {
        // Indicate that the button has been clicked.
        Toast toast = Toast.makeText(getApplicationContext(), "HeroWidgetGrid Displayed", Toast.LENGTH_SHORT);
        toast.show();

        getAndUpdateBaselineWidget(false);
    }

    /**
     * Click handler defined and set in the layout.
     * Empties app widget of any GridEntries.
     */
    @OnClick(R.id.btn_set_empty_groupedgridwidget)
    public void onSetEmptyGroupedGrid() {
        // Indicate that the button has been clicked.
        Toast toast = Toast.makeText(getApplicationContext(), "HeroWidget Emptied", Toast.LENGTH_SHORT);
        toast.show();

        getAndUpdateBaselineWidget(true);
    }

    /**
     * Creates either an empty app widget, or an app widget with dummy GridEntries.
     * The app widget is then pushed to the HomeManager.
     */
    private void getAndUpdateBaselineWidget(boolean empty) throws IllegalArgumentException {
        try {
            // Create a new app widget and list of groups.
            final GroupedGridHeroWidget widget = new GroupedGridHeroWidget();
            final List<GroupedGridHeroWidget.Group> groups = new ArrayList<GroupedGridHeroWidget.Group>();

            if (empty) {
                // Sets the empty grid property.
                GroupedGridHeroWidget.EmptyGridProperty emptyGridProperty = new GroupedGridHeroWidget.EmptyGridProperty();
                emptyGridProperty.setLabel(getString(R.string.group_empty_grid));

                /* HeroWidgetIntents must be sent to a BroadcastReceiver, where the receiver's onReceive()
                   will process the intent.

                   If you are only trying to launch an Activity, you can instead create a
                   HeroWidgetActivityStarterIntent, which extends HeroWidgetIntent. With this intent,
                   you can start an Activity directly.

                   If widget remains empty, HeroWidgetIntent will be actioned on with an empty label*/
                HeroWidgetIntent heroWidgetIntent = new HeroWidgetIntent(TARGET_CLASS_NAME);
                heroWidgetIntent.setData(getString(R.string.group_empty_label));
                emptyGridProperty.setContentIntent(heroWidgetIntent);

                widget.setEmptyGridProperty(emptyGridProperty);

                // If widget is not empty, populate with Groups.
            } else {
                // Create group with GridEntries.
                GroupedGridHeroWidget.Group group = createSampleGroup(getString(R.string.group_group), THUMBNAIL_IDS.length);
                groups.add(group);

                // Sets the grouped grid.
                widget.setGroups(groups);
            }

            // Display the app widget.
            mHomeManager.updateWidget(widget);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "unable to set the groups for the GroupedGridHeroWidget", e);
        }
    }

    /**
     * Creates and returns a sample Group, given a name and size.
     */
    private GroupedGridHeroWidget.Group createSampleGroup(final String groupName, final int groupSize) {
        if (groupSize < 0) {
            return null;
        }

        // Create new Group and GridEntries.
        GroupedGridHeroWidget.Group group = new GroupedGridHeroWidget.Group();
        List<GroupedGridHeroWidget.GridEntry> gridEntries = new ArrayList<GroupedGridHeroWidget.GridEntry>();

        // Sets a group name.
        group.setGroupName(groupName);

        // Set each GridEntry with the same intent.
        // For intents that start Activities, use HeroWidgetActivityStarterIntent.
        for (int i = 0; i < groupSize; i++) {
            HeroWidgetIntent heroWidgetIntent = new HeroWidgetIntent(TARGET_CLASS_NAME);
            heroWidgetIntent.setData(getString(R.string.group_item_in_group, i, groupName));

            GroupedGridHeroWidget.GridEntry gridEntry = new GroupedGridHeroWidget.GridEntry(getApplicationContext());

            // Sets the entry information and text.
            // You can also show a "play" icon in case this represents media
            gridEntry.setContentIntent(heroWidgetIntent);

            // Set the icon for the grid view
            // You can also setThumbnail("http://...") to load over the network connection
            gridEntry.setThumbnail(THUMBNAIL_IDS[i]);

            // Adds the entry.
            gridEntries.add(gridEntry);
        }

        // Adds the entries
        group.setGridEntries(gridEntries);

        return group;
    }
}

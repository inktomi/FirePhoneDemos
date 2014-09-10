package com.zappos.firephone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.amazon.device.home.GroupedGridHeroWidget;
import com.amazon.device.home.HeroWidgetIntent;
import com.amazon.device.home.HomeManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

@SuppressWarnings("unused")
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    // Used to get data from the intent from our home widget
    public static final String EXTRA_KEY = "data";

    @InjectView(R.id.btn_head_tracking)
    Button btnHeadTracking;

    @InjectView(R.id.btn_gestures)
    Button btnGestures;

    // Home API supports using local application resources as well as remote URLs to assets.
    private final static Uri AMZN_BADGE_URI = Uri.parse("https://images-na.ssl-images-amazon.com/images/G/01/AmazonMobileApps/amazon-apps-store-us-white.png");

    // Use this Receiver as the intent target for GridEntries in the app widget.
    private static final String TARGET_CLASS_NAME = WidgetBroadcastReceiver.class.getName();

    // HomeManager to interface with updating home items.
    private HomeManager mHomeManager;

    // Arbitrary sample group size.
    private static final int SAMPLE_GROUP_SIZE = 8;

    // Integer to store current badge number.
    private static int mBadgeNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        try {
            mHomeManager = HomeManager.getInstance(this);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "No HomeManager instance is available for this application", e);
        }

        // Assuming this activity was launched by an app widget item, we can display
        // data specific to the app widget item that was clicked.
        final Intent intent = getIntent();
        final String data = intent.getStringExtra(EXTRA_KEY);

        if( null != data )
        Log.v(TAG, "Data was set, it is: " + data);
    }

    @OnClick(R.id.btn_head_tracking)
    public void headTrackingClicked() {
        startActivity(new Intent(this, HeadTrackingCircleActivity.class));
    }

    @OnClick(R.id.btn_gestures)
    public void gesturesClicked() {
        startActivity(new Intent(this, GestureActivity.class));
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
     * Click handler defined and set in the layout.
     * Increments the numeric badge on the app icon.
     */
    @OnClick(R.id.btn_increment_numeric_badge_number)
    public void onIncrementNumericBadge() {
        Log.v(TAG, "onIncreaseNumericBadge, incrementing mBadgeNumber to " + (mBadgeNumber+1));
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
                GroupedGridHeroWidget.Group group = createSampleGroup(getString(R.string.group_group), SAMPLE_GROUP_SIZE);
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
            gridEntry.setContentIntent(heroWidgetIntent)
                    .setPlayInfo(true, getString(R.string.grouped_grid_play_duration));

            // Alternate the thumbnail between a URI and a drawable resource.
            if (0 == (i % 2)) {
                gridEntry.setThumbnail(AMZN_BADGE_URI);
            } else {
                gridEntry.setThumbnail(R.drawable.smiley_icon);
            }

            // Adds the entry.
            gridEntries.add(gridEntry);
        }

        // Adds the entries
        group.setGridEntries(gridEntries);

        return group;
    }

}

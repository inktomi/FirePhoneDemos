package com.zappos.firephone;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.amazon.device.home.HomeManager;
import com.amazon.euclid.widget.ZHeaderNavigationBar;

import amazon.widget.NavigationPane;
import amazon.widget.NavigationPaneMenuItem;
import amazon.widget.SidePanelLayout;

/**
 * A BaseActivity is a good way to have a consistent navigation menu throughout your app. We use it here for that purpose.
 *
 * Created by jitse on 9/10/14.
 */
@SuppressWarnings("unused")
public class BaseActivity extends Activity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    protected SidePanelLayout mSidePanelLayout;
    protected NavigationPane mLeftPanel;
    private FrameLayout mFrameLayout;
    protected LinearLayout mRightPanel;

    private ZHeaderNavigationBar mHeaderNavBar;

    // HomeManager to interface with updating home items.
    protected HomeManager mHomeManager;

    // Used to get data from the intent from our home widget
    public static final String EXTRA_KEY = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        try {
            mHomeManager = HomeManager.getInstance(this);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "No HomeManager instance is available for this application", e);
        }

        // We can't inject with butterknife here
        mSidePanelLayout = (SidePanelLayout) findViewById(R.id.side_panel_layout);
        mLeftPanel = (NavigationPane) findViewById(R.id.left_panel);
        mRightPanel = (LinearLayout) findViewById(R.id.right_panel);
        mFrameLayout = (FrameLayout) findViewById(R.id.content_panel);
        mHeaderNavBar = (ZHeaderNavigationBar) findViewById(R.id.zheadernavigationbar);

        // Set the title given the label for the activity in the manifest.
        PackageManager packageManager = getPackageManager();
        try {
            ActivityInfo activityInfo = packageManager.getActivityInfo(getComponentName(), 0);
            setTitle(getString(activityInfo.labelRes));
        } catch (PackageManager.NameNotFoundException e) {
            Log.v(TAG, "Failed to pull out activity label to set the title automatically.", e);
        }

        // Assuming this activity was launched by an app widget item, we can display
        // data specific to the app widget item that was clicked.
        final Intent intent = getIntent();
        final String data = intent.getStringExtra(EXTRA_KEY);

        if( null != data ) {
            Log.v(TAG, "Data was set, it is: " + data);
        }

        mLeftPanel.setOnNavigationMenuItemClickListener(new NavigationPane.OnNavigationMenuItemClickListener() {
            @Override
            public boolean onNavigationMenuItemClick(NavigationPaneMenuItem navigationPaneMenuItem, View view) {
                final int menuItemId = (int) navigationPaneMenuItem.getItemId();

                switch (menuItemId) {
                    case R.id.menu_head_tracking:
                        startActivity(new Intent(BaseActivity.this, HeadTrackingCircleActivity.class));
                        break;
                    case R.id.menu_gesture_sensors:
                        startActivity(new Intent(BaseActivity.this, GestureActivity.class));
                        break;
                    case R.id.menu_home_widget:
                        startActivity(new Intent(BaseActivity.this, HomeWidgetActivity.class));
                        break;
                    case R.id.menu_numeric_badge:
                        startActivity(new Intent(BaseActivity.this, NumericBadgeActivity.class));
                        break;
                    default:
                        return false;
                }

                return true;
            }
        });
    }

    /**
     * Set the content view used for activities which extend this. If we can, we put it inside of our main layout
     * so that we have proper menus throughout the app.
     * @param layoutResID the layout to inflate into our main panel
     */
    @Override
    public void setContentView(int layoutResID) {
        if (mFrameLayout == null) {
            super.setContentView(layoutResID);
        } else {
            getLayoutInflater().inflate(layoutResID, mFrameLayout);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);

        if( null != mHeaderNavBar ){
            mHeaderNavBar.setMainTitle(title);
        }
    }
}

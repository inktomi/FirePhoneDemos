package com.zappos.firephone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import amazon.widget.NavigationPane;
import amazon.widget.NavigationPaneMenuItem;
import amazon.widget.SidePanelLayout;

/**
 * Created by jitse on 9/10/14.
 */
public class BaseActivity extends Activity {

    SidePanelLayout mSidePanelLayout;
    NavigationPane mLeftPanel;
    FrameLayout mFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        mSidePanelLayout = (SidePanelLayout) findViewById(R.id.sidepanellayout);
        mLeftPanel = (NavigationPane) findViewById(R.id.leftPanel);
        mFrameLayout = (FrameLayout) findViewById(R.id.fl_content);

        mLeftPanel.setOnNavigationMenuItemClickListener(new NavigationPane.OnNavigationMenuItemClickListener() {
            @Override
            public boolean onNavigationMenuItemClick(NavigationPaneMenuItem navigationPaneMenuItem, View view) {
                final int menuItemId = (int) navigationPaneMenuItem.getItemId();

                switch (menuItemId) {
                    case R.id.menu_head_tracking:
                        startActivity(new Intent(getApplicationContext(), HeadTrackingCircleActivity.class));
                        break;
                    case R.id.menu_gesture_sensors:
                        startActivity(new Intent(getApplicationContext(), GestureActivity.class));
                        break;
                    default:
                        return false;
                }

                return true;
            }
        });

    }

    @Override
    public void setContentView(int layoutResID) {
        if (mFrameLayout == null) {
            super.setContentView(layoutResID);
        } else {
            getLayoutInflater().inflate(layoutResID, mFrameLayout);
        }
    }
}

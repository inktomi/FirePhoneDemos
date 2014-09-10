package com.zappos.firephone;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import amazon.widget.NavigationPane;
import amazon.widget.SidePanelLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class GestureActivity extends Activity {
    // TAG for use in logging.
    private static final String TAG = GestureActivity.class.getName();

    @InjectView(R.id.sidepanellayout)
    SidePanelLayout mSidePanelLayout;

    @InjectView(R.id.leftPanel)
    NavigationPane mNavigationPane;

    @InjectView(R.id.img_main)
    ImageView mImgMain;

    @InjectView(R.id.gv_recommendations)
    GridView mGridView;

    /**
     * Create a new SidePanelsActivity.
     * Demonstrates NavigationPane (left panel) and SidePanelLayout (right panel).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gesture);
        ButterKnife.inject(this);

        updateMainImage(0);
        setupRightPanel();
    }

    private void setupRightPanel() {
        mGridView.setAdapter(new ImageAdapter(this));
    }

    private void updateMainImage(int position) {
        switch (position) {
            case 0:
                mImgMain.setImageDrawable(getResources().getDrawable(R.drawable.saucony2));
                break;
            case 1:
                mImgMain.setImageDrawable(getResources().getDrawable(R.drawable.nb2));
                break;
            case 2:
                mImgMain.setImageDrawable(getResources().getDrawable(R.drawable.converse2));
                break;
            case 3:
                mImgMain.setImageDrawable(getResources().getDrawable(R.drawable.sketchers2));
                break;
            case 4:
                mImgMain.setImageDrawable(getResources().getDrawable(R.drawable.keen2));
                break;
            case 5:
                mImgMain.setImageDrawable(getResources().getDrawable(R.drawable.brooks2));
                break;
        }

        mSidePanelLayout.getRightPanel().close();

    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setTag(position);
            imageView.setImageResource(mThumbIds[position]);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateMainImage((Integer) v.getTag());
                }
            });
            return imageView;
        }

        // references to our images
        private Integer[] mThumbIds = {
                R.drawable.saucony,
                R.drawable.nb,
                R.drawable.converse,
                R.drawable.sketchers,
                R.drawable.keen,
                R.drawable.brooks
        };
    }

}

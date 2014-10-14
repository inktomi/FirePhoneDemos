package com.zappos.firephone.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amazon.motiongestures.Gesture;
import com.amazon.motiongestures.GestureEvent;
import com.amazon.motiongestures.GestureListener;
import com.amazon.motiongestures.GestureManager;
import com.zappos.firephone.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * An activity that shoes a running shoe. On the right panel, it will have recommendations for other
 * running shoes
 */
public class GestureActivity extends BaseActivity implements GestureListener {

    @InjectView(R.id.img_main)
    ImageView mImgMain;

    @InjectView(R.id.rb_ratings)
    RatingBar mRbRatings;

    @InjectView(R.id.tv_price)
    TextView mTvPrice;

    GestureManager mGestureManager;

    // references to our images
    private final static Integer[] THUMBNAIL_RES_IDS = {
            R.drawable.saucony,
            R.drawable.nb,
            R.drawable.converse,
            R.drawable.sketchers,
            R.drawable.keen,
            R.drawable.brooks
    };


    private final static Integer[] LARGE_RES_IDS = {
            R.drawable.saucony2,
            R.drawable.nb2,
            R.drawable.converse2,
            R.drawable.sketchers2,
            R.drawable.keen2,
            R.drawable.brooks2
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gesture);
        ButterKnife.inject(this);

        mTvPrice.setText("$45.99");
        mRbRatings.setRating(4.5f);

        setupRightPanel();

        // Obtain the GestureManager
        mGestureManager = GestureManager.createInstance(this);
    }

    /**
     * Unlocks the right panel so it will appear on a tilt. Creates the recommendations gridview and
     * add it to the right panel
     */
    private void setupRightPanel() {
        //unlock it in case it was locked by something else
        unLockRightPanel();
        View v = getLayoutInflater().inflate(R.layout.recommendations, mRightPanel);

        GridView mGridView = (GridView) v.findViewById(R.id.gv_recommendations);
        mGridView.setAdapter(new ImageAdapter(this));
    }

    /**
     * Updates the main image view with the new image
     * @param position position for the image to be used
     */
    private void updateMainImage(int position) {
        mImgMain.setImageDrawable(getResources().getDrawable(LARGE_RES_IDS[position]));
        mSidePanelLayout.getRightPanel().close();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register listener only when it is needed. Registers to listen for PEEK gestures from the left and right directions
        if (mGestureManager != null) {
            Gesture peekGesture = Gesture.getGestureFromId(Gesture.PEEK);
            if (peekGesture != null) {
                mGestureManager.registerListener(this, peekGesture, GestureEvent.Direction.LEFT | GestureEvent.Direction.RIGHT);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Make sure to release resources to save battery life
        if (mGestureManager != null) {
            mGestureManager.unregisterListener(this);
        }
    }

    /**
     * We know this is a peek event, because that is what we registered for.
     * @param gestureEvent the GestureEvent
     */
    @Override
    public void onGestureEvent(GestureEvent gestureEvent) {
        if (gestureEvent.action == GestureEvent.Action.ON) {
            //Peek is happening, make things visible
            mTvPrice.setVisibility(View.VISIBLE);
            mRbRatings.setVisibility(View.VISIBLE);
        } else {
            mTvPrice.setVisibility(View.GONE);
            mRbRatings.setVisibility(View.GONE);
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return THUMBNAIL_RES_IDS.length;
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
            imageView.setImageResource(THUMBNAIL_RES_IDS[position]);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateMainImage((Integer) v.getTag());
                }
            });
            return imageView;
        }

    }

}

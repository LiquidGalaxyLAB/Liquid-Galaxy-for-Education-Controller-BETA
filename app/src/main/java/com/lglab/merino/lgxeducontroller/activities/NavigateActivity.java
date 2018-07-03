package com.lglab.merino.lgxeducontroller.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import com.lglab.merino.lgxeducontroller.R;

public class NavigateActivity extends AppCompatActivity {

    public static final String TAG = NavigateActivity.class.getSimpleName();
    private TextView mGestureText;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.navigate);

        mGestureText = (TextView) findViewById(R.id.gestureStatus);

        // Create an object of our Custom Gesture Detector Class
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        // Create a GestureDetector
        mGestureDetector = new GestureDetector(this, customGestureDetector);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    class CustomGestureDetector implements GestureDetector.OnGestureListener {


        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            mGestureText.setText("onFling " + e1.getX() + " - " + e2.getX() + " - " + e1.getY() + " - " + e2.getY());
            int threshold = 100;
            float X = e1.getX() - e2.getX();
            if (Math.abs(X) > threshold) {
                if (X < 0) {
                    Log.d(TAG, "Left to Right swipe performed");
                } else {
                    Log.d(TAG, "Right to Left swipe performed");
                }
            }

            float Y = e1.getY() - e2.getY();
            if (Math.abs(Y) > threshold) {
                if (Y < 0) {
                    Log.d(TAG, "Up to Down swipe performed");
                } else {
                    Log.d(TAG, "Down to Up swipe performed");
                }
            }

            return true;
        }
    }
}

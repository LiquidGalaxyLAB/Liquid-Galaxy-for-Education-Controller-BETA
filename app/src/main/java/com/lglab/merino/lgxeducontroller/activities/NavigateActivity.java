package com.lglab.merino.lgxeducontroller.activities;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.legacy.utils.LGUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NavigateActivity extends AppCompatActivity {

    public final String DEBUG_TAG = "HEEEYY IVAAAN";
    HashMap<Integer, PointerDetector> movements = new HashMap<>();
    private double distanceBefore = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_navigate);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();
        int index;
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {

            index = event.getActionIndex();
            PointerDetector pointer = new PointerDetector();
            pointer.update(event.getX(index), event.getY(index));

            movements.put(event.getPointerId(index), pointer);

            if (movements.size() == 2) {
                float startX1 = event.getX(0);
                float startY1 = event.getY(0);
                float startX2 = event.getX(1);
                float startY2 = event.getY(1);
                distanceBefore = Math.sqrt(Math.pow(startX1 - startX2, 2) + Math.pow(startY1 - startY2, 2));

            }
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {

            index = event.getActionIndex();

            movements.remove(event.getPointerId(index));
        } else if (action == MotionEvent.ACTION_MOVE) {

            int pointerCount = event.getPointerCount();

            for (int i = 0; i < pointerCount; i++) {
                int pointerId = event.getPointerId(i);

                if (movements.containsKey(pointerId)) {
                    movements.get(pointerId).update(event.getX(i), event.getY(i));
                }
            }
        }
        int pointerCount = movements.size();

        if (pointerCount <= 0 || pointerCount > 2)
            return true;

        ArrayList<Integer> pointerMovingIds = getPointerMovingIds();

        if (pointerCount == 1 && pointerMovingIds.size() == 1) {
            PointerDetector pointer1 = movements.get(pointerMovingIds.get(0));
            Log.d(DEBUG_TAG, "We're moving the earth with an angle of: " + String.valueOf(pointer1.getAngle()));
        }
        if (pointerCount == 2 && pointerMovingIds.size() == 2) {
            PointerDetector pointer1 = movements.get(pointerMovingIds.get(0));
            PointerDetector pointer2 = movements.get(pointerMovingIds.get(1));

            double diffAngle = getAngleDiff(pointer1.getAngle(), pointer2.getAngle());

            if (diffAngle >= 135 || diffAngle >= 45 && diffAngle <= 75) {
                float x1 = event.getX(0);
                float y1 = event.getY(0);
                float x2 = event.getX(1);
                float y2 = event.getY(1);
                double distanceBetween = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
                if (distanceBetween >= distanceBefore) {
                    Log.d(DEBUG_TAG, "We're zooming IN");
                } else if (distanceBetween < distanceBefore) {
                    Log.d(DEBUG_TAG, "We are zooming OUT");
                    try {
                        //"cat << EOF > /home/lg/test.txt\n TESTING TEST ONE AGAIN \n EOF"
                        // sudo service ssh start   THIS COMMAND NEEDS TO BE IN THE RUNNING SCRIPT OF LG
                        // use xdotool POLAR
                        LGUtils.setConnectionWithLiquidGalaxy(null, "DISPLAY=:3.0 xdotool mousemove 0 0", this);
                    } catch (Exception e) {
                    }
                }
                distanceBefore = distanceBetween;

            } else if (diffAngle < 45) {
                Log.d(DEBUG_TAG, "We're moving the camera with an angle of: \n" + String.valueOf(pointer1.getAngle())
                        + "\n" + String.valueOf(pointer2.getAngle())
                        + "\n" + String.valueOf(getAverageAngle(pointer1.getAngle(), pointer2.getAngle(), diffAngle)));
            }
        }
        return true;
    }

    private ArrayList<Integer> getPointerMovingIds() {
        ArrayList<Integer> pointerMovingIds = new ArrayList<>();

        for (Map.Entry<Integer, PointerDetector> entry : movements.entrySet()) {
            if (entry.getValue().hasMoved())
                pointerMovingIds.add(entry.getKey());
        }
        return pointerMovingIds;
    }

    private double getAngleDiff(double alpha, double beta) {
        double phi = Math.abs(beta - alpha) % 360; // This is either the distance or 360 - distance
        return phi > 180 ? 360 - phi : phi;
    }

    private double getAverageAngle(double alpha, double beta, double diff) {
        return alpha > beta ? alpha - (diff / 2) : beta - (diff / 2);
    }
}
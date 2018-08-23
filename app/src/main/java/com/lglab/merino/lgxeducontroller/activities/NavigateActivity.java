package com.lglab.merino.lgxeducontroller.activities;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.connection.LGConnectionManager;
import com.lglab.merino.lgxeducontroller.legacy.utils.LGUtils;
import com.lglab.merino.lgxeducontroller.utils.PointerDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NavigateActivity extends AppCompatActivity {

    public final String DEBUG_TAG = "HEEEYY IVAAAN";

    HashMap<Integer, PointerDetector> pointers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_navigate);

        LGConnectionManager.getInstance().setData("lg", "lqgalaxy", "10.160.67.159", 22);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();
        int index;
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            if(pointers.size() < 2) {
                index = event.getActionIndex();
                pointers.put(event.getPointerId(index), new PointerDetector(event.getX(index), event.getY(index)));

            }
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
            pointers.remove(event.getPointerId(event.getActionIndex()));
        } else if (action == MotionEvent.ACTION_MOVE) {
            int pointerCount = event.getPointerCount();
            for (int i = 0; i < pointerCount; i++) {
                int pointerId = event.getPointerId(i);
                if (pointers.containsKey(pointerId)) {
                    pointers.get(pointerId).update(event.getX(i), event.getY(i));
                }
            }
        }

        if (pointers.size() == 0)
            return true;

        if(pointers.size() == 1) {
            PointerDetector pointer = pointers.entrySet().iterator().next().getValue();
            if(pointer.isMoving()) {
                //sudo service ssh start
                //"DISPLAY=3.0 xdotool mousemove 0 0"
                LGConnectionManager.getInstance().addCommandToLG("export DISPLAY=:0; " +
                        "xdotool mouseup 1; " +
                        "xdotool mousemove --polar 0 0; " +
                        "xdotool mousedown 1; " +
                        "xdotool mousemove --polar " + (int)pointer.getTraveledAngle() + " " + (int)Math.min(pointer.getTraveledDistance(), 250) + ";"
                );
            }
        }
        else if(pointers.size() == 2) {
            Iterator<Map.Entry<Integer, PointerDetector>> iterator = pointers.entrySet().iterator();
            PointerDetector pointer1 = iterator.next().getValue();
            PointerDetector pointer2 =iterator.next().getValue();
            if(pointer1.isMoving() && pointer2.isMoving()){
                short interaction = pointer1.getInteraction(pointer2);
                if(interaction != 0) {
                    LGConnectionManager.getInstance().addCommandToLG("export DISPLAY=:0; " +
                            "xdotool mouseup 1; " +
                            "xdotool mousemove --polar 0 0; " +
                            "xdotool mouseclick " + (interaction == PointerDetector.JOINING ? 4 : 5) + ";"
                    );
                }
            }


        }


        return true;
    }

    private double getAngleDiff(double alpha, double beta) {
        double phi = Math.abs(beta - alpha) % 360; // This is either the distance or 360 - distance
        return phi > 180 ? 360 - phi : phi;
    }

    private double getAverageAngle(double alpha, double beta, double diff) {
        return alpha > beta ? alpha - (diff / 2) : beta - (diff / 2);
    }
}
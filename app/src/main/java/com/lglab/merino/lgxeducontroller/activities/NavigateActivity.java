package com.lglab.merino.lgxeducontroller.activities;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.connection.LGConnectionManager;
import com.lglab.merino.lgxeducontroller.legacy.utils.LGUtils;
import com.lglab.merino.lgxeducontroller.utils.LGCommand;
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

        LGConnectionManager.getInstance().setData("lg", "lqgalaxy", "10.160.67.54", 22);
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

        if(pointers.size() != 2) {
            if(PointerDetector.isZoomingIn) {
                PointerDetector.isZoomingIn = false;
                updateKeyToLG(PointerDetector.isZoomingIn, PointerDetector.KEY_ZOOM_IN);
            }
            if(PointerDetector.isZoomingOut) {
                PointerDetector.isZoomingOut = false;
                updateKeyToLG(PointerDetector.isZoomingOut, PointerDetector.KEY_ZOOM_OUT);
            }
        }

        if (pointers.size() == 0)
            return true;

        if(pointers.size() == 1) {
            PointerDetector pointer = pointers.entrySet().iterator().next().getValue();
            if(pointer.isMoving()) {
                //sudo service ssh start
                //"DISPLAY=3.0 xdotool mousemove 0 0"
                LGConnectionManager.getInstance().addCommandToLG(new LGCommand("export DISPLAY=:0; " +
                        "xdotool mouseup 1 " +
                        "mousemove --polar 0 0 " +
                        "mousedown 1 " +
                        "mousemove --polar " + (int)pointer.getTraveledAngle() + " " + (int)Math.min(pointer.getTraveledDistance(), 250) + " " +
                        "mouseup 1;", LGCommand.NON_CRITICAL_MESSAGE)
                );
            }
        }
        else if(pointers.size() == 2) {
            Iterator<Map.Entry<Integer, PointerDetector>> iterator = pointers.entrySet().iterator();
            PointerDetector pointer1 = iterator.next().getValue();
            PointerDetector pointer2 =iterator.next().getValue();

            short zoomInteractionType = pointer1.getZoomInteractionType(pointer2);
            if(zoomInteractionType == PointerDetector.ZOOM_IN && !PointerDetector.isZoomingIn){
                if(PointerDetector.isZoomingOut) {
                    PointerDetector.isZoomingOut = false;
                    updateKeyToLG(PointerDetector.isZoomingOut, PointerDetector.KEY_ZOOM_OUT);
                }
                PointerDetector.isZoomingIn = true;
                updateKeyToLG(PointerDetector.isZoomingIn, PointerDetector.KEY_ZOOM_IN);
            }
            else if(zoomInteractionType == PointerDetector.ZOOM_OUT && !PointerDetector.isZoomingOut){
                if(PointerDetector.isZoomingIn) {
                    PointerDetector.isZoomingIn = false;
                    updateKeyToLG(PointerDetector.isZoomingIn, PointerDetector.KEY_ZOOM_IN);
                }
                PointerDetector.isZoomingOut = true;
                updateKeyToLG(PointerDetector.isZoomingOut, PointerDetector.KEY_ZOOM_OUT);
            }

            if(pointer1.isMoving() && pointer2.isMoving() && pointer1.getZoomInteractionType(pointer2) == PointerDetector.ZOOM_NONE){
                LGConnectionManager.getInstance().addCommandToLG(new LGCommand("export DISPLAY=:0; " +
                        "xdotool mouseup 1 " +
                        "mousemove --polar 0 0 " +
                        "mousedown 3 " +
                        "mousemove --polar " + getAverageAngle(pointer1.getTraveledAngle(), pointer2.getTraveledAngle(), getAngleDiff(pointer1.getTraveledAngle(), pointer2.getTraveledAngle())) + " " + (int)Math.min(pointer1.getTraveledDistance(), 250) + " " +
                        "mouseup 3;", LGCommand.NON_CRITICAL_MESSAGE)
                );
            }

        }


        return true;
    }

    private void updateKeyToLG(boolean isActive, String key) {
        LGConnectionManager.getInstance().addCommandToLG(new LGCommand("export DISPLAY=:0; " +
                "xdotool key" + (isActive ? "down" : "up") + " " + key + ";", LGCommand.CRITICAL_MESSAGE)
        );
    }

    private double getAngleDiff(double alpha, double beta) {
        double phi = Math.abs(beta - alpha) % 360; // This is either the distance or 360 - distance
        return phi > 180 ? 360 - phi : phi;
    }

    private double getAverageAngle(double alpha, double beta, double diff) {
        return alpha > beta ? alpha - (diff / 2) : beta - (diff / 2);
    }
}
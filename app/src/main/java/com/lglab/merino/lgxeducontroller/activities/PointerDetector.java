package com.lglab.merino.lgxeducontroller.activities;

public class PointerDetector {

    private long timeUpdated = 0;

    private float xFirst = -1;
    private float yFirst = -1;

    private float xLast = -1;
    private float yLast = -1;


    public double getDistance(){

        return Math.sqrt( Math.pow(xFirst - xLast, 2) + Math.pow(yFirst - yLast, 2));
    }

    public double getAngle(){
        double angle = Math.toDegrees(Math.atan2(yLast - yFirst, xLast - xFirst));
        if(angle < 0) {
            angle += 360;
        }

        return angle;
    }

    public void update(float x, float y){

        long timeNow = System.currentTimeMillis();

        if(timeNow - timeUpdated >= 100) {
            xFirst = xLast;
            yFirst = yLast;

            xLast = x;
            yLast = y;

            timeUpdated = timeNow;
        }
    }

    private boolean isMoving() {
        return (xFirst == -1 || yFirst == -1) ? false : getDistance() < 50 ? false : true;
    }

    public boolean hasMoved() {
        return isMoving() && System.currentTimeMillis() - timeUpdated < 150;
    }
}


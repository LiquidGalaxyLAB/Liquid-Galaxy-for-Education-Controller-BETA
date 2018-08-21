package com.lglab.merino.lgxeducontroller.utils;

public class PointerDetector {

    private float xAfter = -1;
    private float yAfter = -1;

    private float xBefore = -1;
    private float yBefore = -1;

    public PointerDetector(float x, float y){
        update(x, y);
    }


    public double getTraveledDistance(){
        return Math.sqrt( Math.pow(xAfter - xBefore, 2) + Math.pow(yAfter - yBefore, 2));
    }

    public double getTraveledAngle(){
        double angle = Math.toDegrees(Math.atan2(yAfter - yBefore, xAfter - xBefore));
        if(angle < 0) {
            angle += 360;
        }

        return angle;
    }

    public void update(float x, float y) {
        xBefore = xAfter;
        yBefore = yAfter;

        xAfter = x;
        yAfter = y;
    }

    public boolean isMoving() {
        return (xBefore != xAfter || yBefore != yAfter) && xBefore != -1;
    }

    public double getDistanceFromPointerAfter(PointerDetector pointer) {
        return Math.sqrt( Math.pow(xAfter - pointer.xAfter, 2) + Math.pow(yAfter - pointer.yAfter, 2));
    }

    public double getDistanceFromPointerBefore(PointerDetector pointer) {
        return Math.sqrt( Math.pow(xBefore - pointer.xBefore, 2) + Math.pow(yBefore - pointer.yBefore, 2));
    }
}




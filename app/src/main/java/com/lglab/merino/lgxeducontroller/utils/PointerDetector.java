package com.lglab.merino.lgxeducontroller.utils;

public class PointerDetector {

    public static final short JOINING = 1;
    public static final short SEPARATING = 2;

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
        angle -= 270;
        angle %= 360;
        if(angle < 0) {
            angle += 360;
        }


        return angle;
    }

    public void update(float x, float y) {
        if(Math.sqrt( Math.pow(xAfter - x, 2) + Math.pow(yAfter - y, 2)) >= 10) {
            xBefore = xAfter;
            yBefore = yAfter;

            xAfter = x;
            yAfter = y;
        }
    }

    public boolean isMoving() {
        return (xBefore != xAfter || yBefore != yAfter) && xBefore != -1;
    }

    private double getDistanceFromPointerAfter(PointerDetector pointer) {
        return Math.sqrt( Math.pow(xAfter - pointer.xAfter, 2) + Math.pow(yAfter - pointer.yAfter, 2));
    }

    private double getDistanceFromPointerBefore(PointerDetector pointer) {
        return Math.sqrt( Math.pow(xBefore - pointer.xBefore, 2) + Math.pow(yBefore - pointer.yBefore, 2));
    }

    public short getInteraction(PointerDetector pointer) {
        double distance = getDistanceFromPointerAfter(pointer) - getDistanceFromPointerBefore(pointer);
        return distance >= 10 ? SEPARATING : distance <= -10 ? JOINING : 0;
    }
}




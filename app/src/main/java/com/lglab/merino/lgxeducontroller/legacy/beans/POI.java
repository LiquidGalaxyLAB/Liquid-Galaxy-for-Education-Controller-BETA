package com.lglab.merino.lgxeducontroller.legacy.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.plus.model.people.Person;
import com.lglab.merino.lgxeducontroller.interfaces.IJsonPacker;

import org.json.JSONException;
import org.json.JSONObject;

public class POI implements IJsonPacker, Parcelable {

    private long id;
    private String name;
    private String visited_place;
    private double longitude;
    private double latitude;
    private double altitude;
    private double heading;
    private double tilt;
    private double range;
    private String altitudeMode;
    private boolean hidden;
    private int categoryId;

    public POI() { }

    public POI(long id, String name, String visited_place, double longitude, double latitude, double altitude, double heading, double tilt, double range, String altitudeMode, boolean hidden, int categoryId) {
        this.id = id;
        this.name = name;
        this.visited_place = visited_place;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.heading = heading;
        this.tilt = tilt;
        this.range = range;
        this.altitudeMode = altitudeMode;
        this.hidden = hidden;
        this.categoryId = categoryId;
    }

    public POI(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.visited_place = in.readString();
        this.longitude = in.readLong();
        this.latitude = in.readLong();
        this.altitude = in.readLong();
        this.heading = in.readLong();
        this.tilt = in.readLong();
        this.range = in.readLong();
        this.altitudeMode = in.readString();
        this.hidden = in.readInt() != 0;
        this.categoryId = in.readInt();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVisited_place() {
        return visited_place;
    }

    public void setVisited_place(String visited_place) {
        this.visited_place = visited_place;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getTilt() {
        return tilt;
    }

    public void setTilt(double tilt) {
        this.tilt = tilt;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public String getAltitudeMode() {
        return altitudeMode;
    }

    public void setAltitudeMode(String altitudeMode) {
        this.altitudeMode = altitudeMode;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("id", id);
        obj.put("name", name);
        obj.put("visited_place", visited_place);
        obj.put("longitude", longitude);
        obj.put("latitude", latitude);
        obj.put("altitude", altitude);
        obj.put("heading", heading);
        obj.put("tilt", tilt);
        obj.put("range", range);
        obj.put("altitudeMode", altitudeMode);
        obj.put("hidden", hidden);
        obj.put("categoryId", categoryId);

        return obj;
    }

    @Override
    public POI unpack(JSONObject obj) throws JSONException {
        id = obj.getLong("id");
        name = obj.getString("name");
        visited_place = obj.getString("visited_place");
        longitude = obj.getDouble("longitude");
        latitude = obj.getDouble("latitude");
        altitude = obj.getDouble("altitude");
        heading = obj.getDouble("heading");
        tilt = obj.getDouble("tilt");
        range = obj.getDouble("range");
        altitudeMode = obj.getString("altitudeMode");
        hidden = obj.getBoolean("hidden");
        categoryId = obj.getInt("categoryId");

        return this;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(visited_place);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
        parcel.writeDouble(altitude);
        parcel.writeDouble(heading);
        parcel.writeDouble(tilt);
        parcel.writeDouble(range);
        parcel.writeString(altitudeMode);
        parcel.writeInt(hidden ? 1 : 0);
        parcel.writeInt(categoryId);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public POI createFromParcel(Parcel in) {
            return new POI(in);
        }
        public POI[] newArray(int size) {
            return new POI[size];
        }
    };
}

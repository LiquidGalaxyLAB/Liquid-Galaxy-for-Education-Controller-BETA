package com.lglab.merino.lgxeducontroller.games.quiz;

import com.lglab.merino.lgxeducontroller.interfaces.IJsonPacker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Quiz implements IJsonPacker {
    private ArrayList<Question> questions;
    private String name;

    public Quiz() {
        questions = new ArrayList<>();
    }

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("name", name);

        JSONArray array = new JSONArray();
        for(int i = 0; i < questions.size(); i++) {
            array.put(questions.get(i).pack());
        }
        obj.put("questions", array);

        return obj;
    }

    @Override
    public Quiz unpack(JSONObject obj) throws JSONException {
        name = obj.getString("name");

        JSONArray array = obj.getJSONArray("questions");
        for(int i = 0; i < array.length(); i++) {
            questions.add(new Question().unpack(array.getJSONObject(i)));
        }
        return this;
    }

    public String getNameForExporting() {
        return name.replaceAll("[:\\/*\"?|<> ]", "_") + ".json";
    }
}

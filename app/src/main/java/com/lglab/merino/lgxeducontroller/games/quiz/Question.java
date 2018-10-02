package com.lglab.merino.lgxeducontroller.games.quiz;

import android.util.Log;

import com.lglab.merino.lgxeducontroller.interfaces.IJsonPacker;
import com.lglab.merino.lgxeducontroller.legacy.beans.POI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Question implements IJsonPacker {
    public static final String TAG = Question.class.getSimpleName();
    public static final int MAX_ANSWERS = 4;
    public String question;
    public int correctAnswer;
    public String[] answers;
    public String information;
    public POI[] pois;
    public POI initialPOI;
    //Additional for game-use only
    public int selectedAnswer = 0;
    private int id;

    public Question() {
        answers = new String[MAX_ANSWERS];
        pois = new POI[MAX_ANSWERS];
    }

    public Question(int id, String question, int correctAnswer, String[] answers, String information, POI[] answer_pois, POI initialPOI) {
        this();
        this.id = id;
        this.question = question;
        this.correctAnswer = correctAnswer;
        Log.i(TAG, "Question: " + Arrays.toString(answers) + " " + Arrays.toString(this.answers));
        System.arraycopy(answers, 0, this.answers, 0, answers.length);
        this.information = information;
        System.arraycopy(answer_pois, 0, this.pois, 0, answer_pois.length);
        this.initialPOI = initialPOI;
    }

    @Override
    public JSONObject pack() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("ID_question", id);
        obj.put("Question", question);
        obj.put("Correct_answer", correctAnswer);
        obj.put("Text_bubble", information);

        obj.put("Poi_0", initialPOI.pack());

        for (int i = 0; i < MAX_ANSWERS; i++) {
            obj.put("Answer_" + (i + 1), answers[i]);
        }

        for (int i = 0; i < MAX_ANSWERS; i++) {
            obj.put("Poi_" + (i + 1), pois[i].pack());
        }

        return obj;
    }

    @Override
    public Question unpack(JSONObject obj) throws JSONException {
        id = obj.getInt("ID_question");
        question = obj.getString("Question");
        correctAnswer = obj.getInt("Correct_answer");
        information = obj.getString("Text_bubble");
        initialPOI = new POI().unpack(obj.getJSONObject("Poi_0"));

        for (int i = 0; i < MAX_ANSWERS; i++) {
            answers[i] = obj.getString("Answer_" + (i + 1));
        }

        for (int i = 0; i < MAX_ANSWERS; i++) {
            pois[i] = new POI().unpack(obj.getJSONObject("Poi_" + (i + 1)));
        }

        return this;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", correctAnswer=" + correctAnswer +
                ", answers=" + Arrays.toString(answers) +
                ", information='" + information + '\'' +
                ", pois=" + Arrays.toString(pois) +
                ", initialPOI=" + initialPOI +
                ", selectedAnswer=" + selectedAnswer +
                ", id=" + id +
                '}';
    }
}

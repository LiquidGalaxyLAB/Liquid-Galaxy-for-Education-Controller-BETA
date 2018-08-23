package com.lglab.merino.lgxeducontroller.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.lglab.merino.lgxeducontroller.BuildConfig;
import com.lglab.merino.lgxeducontroller.connection.LGConnectionManager;
import com.lglab.merino.lgxeducontroller.fragments.AnswerQuizFragment;
import com.lglab.merino.lgxeducontroller.games.quiz.Question;
import com.lglab.merino.lgxeducontroller.games.quiz.QuizManager;
import com.lglab.merino.lgxeducontroller.legacy.POISFragment;
import com.lglab.merino.lgxeducontroller.legacy.beans.POI;
import com.lglab.merino.lgxeducontroller.legacy.utils.LGUtils;
import com.lglab.merino.lgxeducontroller.legacy.utils.PoisGridViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.lglab.merino.lgxeducontroller.legacy.data.POIsContract.POIEntry;
import static com.lglab.merino.lgxeducontroller.legacy.data.POIsContract.TourPOIsEntry;

public class LiquidGalaxyAnswerTourView extends AsyncTask<String, Void, String> {

    private AnswerQuizFragment fragment;
    private int questionNumber;

    public LiquidGalaxyAnswerTourView(AnswerQuizFragment fragment, int questionNumber) {
        this.fragment = fragment;
        this.questionNumber = questionNumber;
    }

    protected String doInBackground(String... params) {

        List<String> pois = new ArrayList();
        List<Integer> poisDuration = new ArrayList();

        Question question = QuizManager.getInstance().getQuiz().questions.get(questionNumber);

        if(question.selectedAnswer != question.correctAnswer)
        {
            poisDuration.add(10);
            pois.add(buildCommand(question.pois[question.selectedAnswer - 1]));
        }

        poisDuration.add(10);
        pois.add(buildCommand(question.pois[question.correctAnswer - 1]));

        try {
            sendTourPOIs(pois, poisDuration);
            return BuildConfig.FLAVOR;
        } catch (IndexOutOfBoundsException e) {
            return "Error. There's probably no POI inside the Tour.";
        }

    }

    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        cancel(true);
    }

    private String buildCommand(POI poi) {
        return "echo 'flytoview=<LookAt><longitude>" + poi.getLongitude() + "</longitude><latitude>" + poi.getLatitude() + "</latitude><altitude>" + poi.getAltitude() + "</altitude><heading>" + poi.getHeading() + "</heading><tilt>" + poi.getTilt() + "</tilt><range>" + poi.getRange() + "</range><gx:altitudeMode>" + poi.getAltitudeMode() + "</gx:altitudeMode></LookAt>' > /tmp/query.txt";
    }

    private void sendTourPOIs(List<String> pois, List<Integer> poisDuration) {
        sendPOI(0, pois.get(0));
        sendOtherTourPOIs(pois, poisDuration);
    }

    private void sendOtherTourPOIs(List<String> pois, List<Integer> poisDuration) {
        int i = 1;
        while (!isCancelled()) {
            sendPOI(Integer.valueOf(poisDuration.get(i).intValue()), pois.get(i));
            i++;
            if (i == pois.size()) {
                i = 0;
            }
        }
    }

    private void sendPOI(Integer duration, String command) {
        try {

            if(duration != 0)
                Thread.sleep((long) (duration * 1000));

            LGConnectionManager.getInstance().addCommandToLG(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onCancelled() {
        super.onCancelled();
    }
}

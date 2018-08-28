package com.lglab.merino.lgxeducontroller.utils;


import android.os.AsyncTask;
import android.util.Log;


import com.lglab.merino.lgxeducontroller.connection.LGConnectionManager;
import com.lglab.merino.lgxeducontroller.fragments.QuestionFragment;
import com.lglab.merino.lgxeducontroller.games.quiz.Question;
import com.lglab.merino.lgxeducontroller.games.quiz.QuizManager;

import com.lglab.merino.lgxeducontroller.legacy.beans.POI;

public class LiquidGalaxyAnswerTourView extends AsyncTask<Integer, Integer, Boolean> {

    private QuestionFragment fragment;

    public LiquidGalaxyAnswerTourView(QuestionFragment fragment) {
        this.fragment = fragment;
    }

    private String buildCommand(POI poi) {
        return "echo 'flytoview=<LookAt><longitude>" + poi.getLongitude() + "</longitude><latitude>" + poi.getLatitude() + "</latitude><altitude>" + poi.getAltitude() + "</altitude><heading>" + poi.getHeading() + "</heading><tilt>" + poi.getTilt() + "</tilt><range>" + poi.getRange() + "</range><gx:altitudeMode>" + poi.getAltitudeMode() + "</gx:altitudeMode></LookAt>' > /tmp/query.txt";
    }

    private void sendPOI(String command) {
        LGConnectionManager.getInstance().addCommandToLG(new LGCommand(command, LGCommand.CRITICAL_MESSAGE));
        Log.d("TOUR", "Sent a POI to LG");
    }

    @Override
    public Boolean doInBackground(Integer... questionNumber) {
        Log.d("TOUR", "doInBackground_start");
        Question question = QuizManager.getInstance().getQuiz().questions.get(questionNumber[0]);

        if(question.selectedAnswer != question.correctAnswer) {
            sendPOI(buildCommand(question.pois[question.selectedAnswer - 1]));
            fragment.changeAlertDialogTitle("Oops! You've chosen a wrong answer!");
            try { Thread.sleep(10000); } catch(Exception e) { return false; }
            sendPOI(buildCommand(question.pois[question.correctAnswer - 1]));
            try { Thread.sleep(10000); } catch(Exception e) { return false; }
        }
        else {
            sendPOI(buildCommand(question.pois[question.correctAnswer - 1]));
            fragment.changeAlertDialogTitle("Great! You're totally right!");
            try { Thread.sleep(10000); } catch(Exception e) { return false; }
        }



        Log.d("TOUR", "doInBackground_finish");
        return true;
    }



    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        Log.d("TOUR", "onPostExecute");
    }

    @Override
    protected void onCancelled (Boolean result) {
        super.onCancelled(result);
        Log.d("TOUR", "onCancelled");
    }
}

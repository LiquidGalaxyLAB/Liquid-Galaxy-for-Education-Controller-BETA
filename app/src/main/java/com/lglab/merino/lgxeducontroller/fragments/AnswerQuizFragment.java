package com.lglab.merino.lgxeducontroller.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.legacy.LiquidGalaxyTourView;
import com.lglab.merino.lgxeducontroller.utils.LiquidGalaxyAnswerTourView;

public class AnswerQuizFragment extends DialogFragment {

    private LiquidGalaxyAnswerTourView tour;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_answer_quiz, null))
                .setPositiveButton("SKIP", (dialog, id) -> {
                    this.getDialog().cancel();
                });
                /*.setNegativeButton("SKIP", (dialog, id) -> {
                    this.getDialog().cancel();
                });*/

        return builder.create();
    }



    public void setQuestionNumber(int question) {
        tour = new LiquidGalaxyAnswerTourView(this, question);
        tour.execute();
    }

}


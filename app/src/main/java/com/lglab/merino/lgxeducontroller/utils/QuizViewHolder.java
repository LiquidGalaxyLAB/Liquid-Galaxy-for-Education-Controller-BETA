package com.lglab.merino.lgxeducontroller.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.activities.GoogleDriveActivity;
import com.lglab.merino.lgxeducontroller.activities.QuizActivity;
import com.lglab.merino.lgxeducontroller.games.quiz.Quiz;
import com.lglab.merino.lgxeducontroller.games.quiz.QuizManager;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class QuizViewHolder extends ChildViewHolder {

    private TextView quizName;
    public Quiz quiz;

    public QuizViewHolder(View itemView) {
        super(itemView);
        quizName = itemView.findViewById(R.id.list_item_quiz_name);
    }

    public void onBind(Quiz quiz) {
        quizName.setText(quiz.toString());
        this.quiz = quiz;
        this.itemView.setOnClickListener(arg0 -> {
            GoogleDriveActivity activity = (GoogleDriveActivity) itemView.getContext();

            QuizManager.getInstance().startQuiz(quiz);

            Intent intent = new Intent(activity, QuizActivity.class);
            activity.startActivity(intent);

        });
    }
}

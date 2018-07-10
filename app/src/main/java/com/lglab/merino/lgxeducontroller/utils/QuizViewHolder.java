package com.lglab.merino.lgxeducontroller.utils;

import android.view.View;
import android.widget.TextView;

import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.games.quiz.Quiz;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class QuizViewHolder extends ChildViewHolder {

    private TextView quizName;

    public QuizViewHolder(View itemView) {
        super(itemView);
        quizName = itemView.findViewById(R.id.list_item_quiz_name);
    }

    public void onBind(Quiz quiz) {
        quizName.setText(quiz.toString());
    }
}

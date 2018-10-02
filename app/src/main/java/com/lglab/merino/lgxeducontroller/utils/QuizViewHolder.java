package com.lglab.merino.lgxeducontroller.utils;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.activities.GoogleDriveActivity;
import com.lglab.merino.lgxeducontroller.activities.QuizActivity;
import com.lglab.merino.lgxeducontroller.games.quiz.Quiz;
import com.lglab.merino.lgxeducontroller.games.quiz.QuizManager;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class QuizViewHolder extends ChildViewHolder {

    public Quiz quiz;
    private TextView quizName;
    private ImageView playButton;
    private ImageView shareButton;

    public QuizViewHolder(View itemView) {
        super(itemView);
        quizName = itemView.findViewById(R.id.list_item_quiz_name);
        shareButton = itemView.findViewById(R.id.list_item_category_share);
        playButton = itemView.findViewById(R.id.list_item_category_arrow);
    }

    public void onBind(Quiz quiz) {
        quizName.setText(quiz.name);
        this.quiz = quiz;
        this.itemView.setOnClickListener(arg0 -> startQuiz((GoogleDriveActivity) itemView.getContext()));
        this.playButton.setOnClickListener(arg0 -> startQuiz((GoogleDriveActivity) itemView.getContext()));
        this.shareButton.setOnClickListener(arg0 -> shareQuiz((GoogleDriveActivity) itemView.getContext()));

    }

    private void startQuiz(GoogleDriveActivity activity) {
        QuizManager.getInstance().startQuiz(quiz);

        Intent intent = new Intent(activity, QuizActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); //Adds the FLAG_ACTIVITY_NO_HISTORY flag
        activity.startActivity(intent);
    }

    private void shareQuiz(GoogleDriveActivity activity) {
        activity.exportQuiz(quiz);
    }
}

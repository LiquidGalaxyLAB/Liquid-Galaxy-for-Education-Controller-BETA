package com.lglab.merino.lgxeducontroller.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.games.quiz.Question;
import com.lglab.merino.lgxeducontroller.games.quiz.QuizManager;

public class QuestionFragment extends Fragment {
    private View view;
    private int questionNumber;
    private Question question;
    private TextView textView;
    private TextView[] answerViews;

    private boolean hasClicked = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questionNumber = getArguments().getInt("data");
        question = QuizManager.getInstance().getQuiz().questions.get(questionNumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_question, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hasClicked = false;

        textView = view.findViewById(R.id.question_title);
        textView.setText(question.question);

        answerViews = new TextView[4];
        answerViews[0] = getView().findViewById(R.id.answerText1);
        answerViews[1] = getView().findViewById(R.id.answerText2);
        answerViews[2] = getView().findViewById(R.id.answerText3);
        answerViews[3] = getView().findViewById(R.id.answerText4);
        for(int i = 0; i < question.answers.length; i++) {
            answerViews[i].setText(question.answers[i]);
        }

        for(int i = 0; i < answerViews.length; i++) {
            setClickListener(i);
        }
    }

    public void setClickListener(final int i) {
        view.findViewById(R.id.answerCard1 + i).setOnClickListener(v -> {
            if(!hasClicked) {
                hasClicked = true;
                question.selectedAnswer = i + 1;
                view.findViewById(R.id.answerCard1 + question.correctAnswer - 1).setBackgroundColor(Color.parseColor("#5cd65c"));
                answerViews[question.correctAnswer - 1].setTextColor(Color.parseColor("#000000"));

                if (i != question.correctAnswer - 1) {
                    v.setBackgroundColor(Color.parseColor("#ff3333"));
                    answerViews[i].setTextColor(Color.parseColor("#000000"));
                }
            }
        });

        if(question.selectedAnswer == i + 1) {
            view.findViewById(R.id.answerCard1 + i).performClick();
        }
    }
}

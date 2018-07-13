package com.lglab.merino.lgxeducontroller.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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

    }
}

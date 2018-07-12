package com.lglab.merino.lgxeducontroller.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.games.quiz.Question;
import com.lglab.merino.lgxeducontroller.legacy.CreateItemActivity;
import com.lglab.merino.lgxeducontroller.utils.Exceptions.MissingInformationException;
import com.lglab.merino.lgxeducontroller.legacy.beans.POI;

public class CreateQuestionActivity extends AppCompatActivity {

    private Context context;
    private EditText questionEditText;
    private RadioGroup correctAnswerRadioButton;
    private EditText questionPOI;

    private EditText textAnswer1;
    private EditText textAnswer2;
    private EditText textAnswer3;
    private EditText textAnswer4;

    private EditText textPOI1;
    private EditText textPOI2;
    private EditText textPOI3;
    private EditText textPOI4;

    private EditText additionalInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.create_question);
        context = CreateQuestionActivity.this;

        questionPOIButton();
        answer1POIButton();
        answer2POIButton();
        answer3POIButton();
        answer4POIButton();

        acceptButton();

    }

    private void questionPOIButton() {
        findViewById(R.id.addQuestionPOIButton).setOnClickListener(view -> {

                Intent createPoiIntent = new Intent(context, CreateItemActivity.class);
                createPoiIntent.putExtra("CREATION_TYPE", "POI");
                startActivity(createPoiIntent);

        });
    }

    private void answer1POIButton() {
        findViewById(R.id.addAnswer1POIButton).setOnClickListener(view -> {
            EditText textPOI1 = (EditText) findViewById(R.id.answer1POITextEdit);
            textPOI1.setText("kalsdh");
        });
    }

    private void answer2POIButton() {
        findViewById(R.id.addAnswer2POIButton).setOnClickListener(view -> {
            EditText textPOI2 = (EditText) findViewById(R.id.answer2POITextEdit);
            textPOI2.setText("kalsdh");
        });
    }

    private void answer3POIButton() {
        findViewById(R.id.addAnswer3POIButton).setOnClickListener(view -> {
            EditText textPOI3 = (EditText) findViewById(R.id.answer3POITextEdit);
            textPOI3.setText("kalsdh");
        });
    }

    private void answer4POIButton() {
        findViewById(R.id.addAnswer4POIButton).setOnClickListener(view -> {
            EditText textPOI4 = (EditText) findViewById(R.id.answer4POITextEdit);
            textPOI4.setText("kalsdh");
        });
    }

    private Question acceptButton() {
        questionEditText = (EditText) findViewById(R.id.questionTextEdit);
        correctAnswerRadioButton = (RadioGroup) findViewById(R.id.radio_group_correct_answer);
        questionPOI = (EditText) findViewById(R.id.questionPOITextEdit);

        textAnswer1 = (EditText) findViewById(R.id.answer1TextEdit);
        textAnswer2 = (EditText) findViewById(R.id.answer2TextEdit);
        textAnswer3 = (EditText) findViewById(R.id.answer3TextEdit);
        textAnswer4 = (EditText) findViewById(R.id.answer4TextEdit);

        textPOI1 = (EditText) findViewById(R.id.answer1POITextEdit);
        textPOI2 = (EditText) findViewById(R.id.answer2POITextEdit);
        textPOI3 = (EditText) findViewById(R.id.answer3POITextEdit);
        textPOI4 = (EditText) findViewById(R.id.answer4POITextEdit);

        additionalInformation = (EditText) findViewById(R.id.informationTextEdit);

        final Question[] q = {};

        findViewById(R.id.accept_button).setOnClickListener(view -> {
            try {

                //Id question (need Intend from Game Manager)
                int id = 5;

                //Question stuff
                String question = getTextFromEditText(questionEditText, getString(R.string.question_text_edit));

                //Correct Answer
                int idSelectedRadioButton = correctAnswerRadioButton.getCheckedRadioButtonId();
                if (idSelectedRadioButton == -1) {
                    throw new MissingInformationException(getString(R.string.correct_answer));
                }
                RadioButton pressedRadioButton = (RadioButton) findViewById(correctAnswerRadioButton.getCheckedRadioButtonId());
                int correctAnswer = Integer.parseInt(pressedRadioButton.getText().toString());

                //Answers
                String[] answers = {getTextFromEditText(textAnswer1, getString(R.string.answer_1)),
                                    getTextFromEditText(textAnswer2, getString(R.string.answer_2)),
                                    getTextFromEditText(textAnswer3, getString(R.string.answer_3)),
                                    getTextFromEditText(textAnswer4, getString(R.string.answer_4))};
                String information = getTextFromEditText(additionalInformation, getString(R.string.more_information));

                //POIs stuff

                POI[] pois = {new POI(),
                                new POI(),
                                new POI(),
                                new POI()};
                POI initial = new POI();

                q[0] = new Question(id, question, correctAnswer, answers, information, pois, initial);


            } catch (MissingInformationException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return q[0];
    }

    private String getTextFromEditText(EditText editText, String whichTextEdit) throws MissingInformationException {
        String toReturn = editText.getText().toString();
        if (toReturn.equals(""))
            throw new MissingInformationException(whichTextEdit);
        return toReturn;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}

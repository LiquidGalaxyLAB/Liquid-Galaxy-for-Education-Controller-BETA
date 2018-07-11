package com.lglab.merino.lgxeducontroller.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.games.quiz.Question;
import com.lglab.merino.lgxeducontroller.legacy.CreateItemActivity;
import com.lglab.merino.lgxeducontroller.legacy.CreateItemFragment;

public class CreateQuestionActivity extends AppCompatActivity {

    private Context context;
    EditText questionEditText;
    RadioGroup correctAnswerRadioButton;
    EditText questionPOI;
    EditText textPOI1;
    EditText textPOI2;
    EditText textPOI3;
    EditText textPOI4;

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

    }

    private void questionPOIButton() {
        findViewById(R.id.addQuestionPOIButton).setOnClickListener(view -> {
            try {
                Intent createPoiIntent = new Intent(this, CreateItemActivity.class);
                createPoiIntent.putExtra("CREATION_TYPE", "POI");
                startActivity(createPoiIntent);
            }
            catch(Exception e) {
                Log.d("HEYHEY", e.toString());
            }
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
        questionPOI = (EditText) findViewById(R.id.answer4POITextEdit);
        textPOI4 = (EditText) findViewById(R.id.answer4POITextEdit);

        /*EditText textPOI4 = (EditText) findViewById(R.id.answer4POITextEdit);
        EditText textPOI4 = (EditText) findViewById(R.id.answer4POITextEdit);
        */

        findViewById(R.id.accept_button).setOnClickListener(view -> {
            int selectedId = correctAnswerRadioButton.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton prova = (RadioButton) findViewById(selectedId);

        });
        return null;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}

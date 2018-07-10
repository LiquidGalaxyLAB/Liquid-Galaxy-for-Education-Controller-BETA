package com.lglab.merino.lgxeducontroller.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.lglab.merino.lgxeducontroller.R;

public class CreateQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.create_question);

        questionPOIButton();
        answer1POIButton();
        answer2POIButton();
        answer3POIButton();
        answer4POIButton();
        acceptButton();


    }

    private void questionPOIButton() {
        findViewById(R.id.addQuestionPOIButton).setOnClickListener(view -> {
            EditText textPOI1 = (EditText) findViewById(R.id.questionPOITextEdit);
            textPOI1.setText("kalsdh");
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

    private void acceptButton() {
        findViewById(R.id.accepr_button).setOnClickListener(view -> {
            EditText textPOI4 = (EditText) findViewById(R.id.answer4POITextEdit);
            textPOI4.setText("nepeee");
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}

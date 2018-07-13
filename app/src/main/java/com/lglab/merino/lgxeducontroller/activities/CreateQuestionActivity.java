package com.lglab.merino.lgxeducontroller.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.internal.Hide;
import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.games.quiz.Question;
import com.lglab.merino.lgxeducontroller.games.quiz.Quiz;
import com.lglab.merino.lgxeducontroller.legacy.CreateItemActivity;
import com.lglab.merino.lgxeducontroller.legacy.data.POIsProvider;
import com.lglab.merino.lgxeducontroller.utils.Category;
import com.lglab.merino.lgxeducontroller.utils.Exceptions.MissingInformationException;
import com.lglab.merino.lgxeducontroller.legacy.beans.POI;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CreateQuestionActivity extends AppCompatActivity {

    private Context context;
    private Quiz quiz;
    private HashMap<Long, POI> poiList;
    private ArrayAdapter poiStringList;

    private EditText questionEditText;
    private RadioGroup correctAnswerRadioButton;
    private EditText questionPOI;

    private EditText textAnswer1;
    private EditText textAnswer2;
    private EditText textAnswer3;
    private EditText textAnswer4;

    private AutoCompleteTextView textPOI1;
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

        poiStringList = new ArrayAdapter(context, android.R.layout.select_dialog_item);

        getPOIStringsFromDatabase();
        textPOI1 = (AutoCompleteTextView) findViewById(R.id.answer1POITextEdit);
        textPOI1.setAdapter(poiStringList);


        questionPOIButton();
        answer1POIButton();
        answer2POIButton();
        answer3POIButton();
        answer4POIButton();

        acceptButton();

    }

    private void getPOIStringsFromDatabase() {
        poiList = new HashMap<>();
        Cursor poiCursor = POIsProvider.getAllPOIs();
        while (poiCursor.moveToNext()) {
            long poiID = poiCursor.getLong(poiCursor.getColumnIndexOrThrow("_id"));
            String name = poiCursor.getString(poiCursor.getColumnIndexOrThrow("Name"));
            String visitedPlace = poiCursor.getString(poiCursor.getColumnIndexOrThrow("Visited_Place"));
            long longitude = poiCursor.getLong(poiCursor.getColumnIndexOrThrow("Longitude"));
            long altitude = poiCursor.getLong(poiCursor.getColumnIndexOrThrow("Altitude"));
            long latitude = poiCursor.getLong(poiCursor.getColumnIndexOrThrow("Heading"));
            long tilt = poiCursor.getLong(poiCursor.getColumnIndexOrThrow("Tilt"));
            long range = poiCursor.getLong(poiCursor.getColumnIndexOrThrow("Range"));
            String altitudeMode = poiCursor.getString(poiCursor.getColumnIndexOrThrow("Altitude_Mode"));
            boolean hidden = poiCursor.getInt(poiCursor.getColumnIndexOrThrow("Hide")) == 1;
            int categoryID = poiCursor.getInt(poiCursor.getColumnIndexOrThrow("Category"));

            try {
                POI newPOI = new POI(poiID, name, visitedPlace, longitude, latitude, altitude, tilt, range, altitudeMode, hidden, categoryID);
                poiList.put(poiID, newPOI);
            }
            catch(Exception e) {
                Log.e("BRUH", e.toString());
            }

            Iterator it = poiList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                POI temp = (POI) pair.getValue();
                poiStringList.add(temp.getName());
            }
        }
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

    private void acceptButton() {
        questionEditText = (EditText) findViewById(R.id.questionTextEdit);
        correctAnswerRadioButton = (RadioGroup) findViewById(R.id.radio_group_correct_answer);
        questionPOI = (EditText) findViewById(R.id.questionPOITextEdit);

        textAnswer1 = (EditText) findViewById(R.id.answer1TextEdit);
        textAnswer2 = (EditText) findViewById(R.id.answer2TextEdit);
        textAnswer3 = (EditText) findViewById(R.id.answer3TextEdit);
        textAnswer4 = (EditText) findViewById(R.id.answer4TextEdit);

        textPOI1 = (AutoCompleteTextView) findViewById(R.id.answer1POITextEdit);
        textPOI2 = (EditText) findViewById(R.id.answer2POITextEdit);
        textPOI3 = (EditText) findViewById(R.id.answer3POITextEdit);
        textPOI4 = (EditText) findViewById(R.id.answer4POITextEdit);

        additionalInformation = (EditText) findViewById(R.id.informationTextEdit);

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
                POI[] pois = {new POI(), new POI(), new POI(), new POI()};
                POI initial = new POI();

                quiz.addQuestion(new Question(id, question, correctAnswer, answers, information, pois, initial));

            } catch (MissingInformationException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
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

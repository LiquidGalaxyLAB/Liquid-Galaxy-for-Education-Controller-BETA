package com.lglab.merino.lgxeducontroller.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.internal.Hide;
import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.fragments.ExitFromQuizFragment;
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
//LINES
    private Context context;
    private Quiz quiz;
    private HashMap<Long, POI> poiList;
    private ArrayAdapter<POI> poiStringList;

    private EditText questionEditText;
    private RadioGroup correctAnswerRadioButton;
    private EditText questionPOI;

    private EditText textAnswer1;
    private EditText textAnswer2;
    private EditText textAnswer3;
    private EditText textAnswer4;

    Dialog dialog;

    private EditText additionalInformation;

    private POI[] pois;
    private POI initial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.create_question);
        context = CreateQuestionActivity.this;
        pois = new POI[4];

        //Waiting for alberts part
        this.quiz = new Quiz();

        poiStringList = new ArrayAdapter<>(context, android.R.layout.select_dialog_item);

        getPOIStringsFromDatabase();

        //Autocomplete text field listeners
        answerPOIText(0, R.id.answer1POITextEdit);
        answerPOIText(1, R.id.answer2POITextEdit);
        answerPOIText(2, R.id.answer3POITextEdit);
        answerPOIText(3, R.id.answer4POITextEdit);
        questionPOIText();

        //POIs Buttons listeners
        POIButton(R.id.addQuestionPOIButton, 0);
        POIButton(R.id.addAnswer1POIButton, 1);
        POIButton(R.id.addAnswer2POIButton, 2);
        POIButton(R.id.addAnswer3POIButton, 3);
        POIButton(R.id.addAnswer4POIButton, 4);

        //finish buttons listeners
        acceptButton();
        cancelButton();
        dialog = new Dialog(this);

    }

    private void getPOIStringsFromDatabase() {
        poiList = new HashMap<>();
        Cursor poiCursor = POIsProvider.getAllPOIs();

        while (poiCursor.moveToNext()) {
            long poiID = poiCursor.getLong(poiCursor.getColumnIndexOrThrow("_id"));
            String name = poiCursor.getString(poiCursor.getColumnIndexOrThrow("Name"));
            String visitedPlace = poiCursor.getString(poiCursor.getColumnIndexOrThrow("Visited_Place"));
            double longitude = poiCursor.getDouble(poiCursor.getColumnIndexOrThrow("Longitude"));
            double altitude = poiCursor.getDouble(poiCursor.getColumnIndexOrThrow("Altitude"));
            double latitude = poiCursor.getDouble(poiCursor.getColumnIndexOrThrow("Latitude"));
            double heading = poiCursor.getDouble(poiCursor.getColumnIndexOrThrow("Longitude"));
            double tilt = poiCursor.getDouble(poiCursor.getColumnIndexOrThrow("Tilt"));
            double range = poiCursor.getDouble(poiCursor.getColumnIndexOrThrow("Range"));
            String altitudeMode = poiCursor.getString(poiCursor.getColumnIndexOrThrow("Altitude_Mode"));
            boolean hidden = poiCursor.getInt(poiCursor.getColumnIndexOrThrow("Hide")) == 1;
            int categoryID = poiCursor.getInt(poiCursor.getColumnIndexOrThrow("Category"));

            try {
                POI newPOI = new POI(poiID, name, visitedPlace, longitude, latitude, altitude, heading, tilt, range, altitudeMode, hidden, categoryID);
                poiList.put(poiID, newPOI);
            }
            catch(Exception e) {
                Log.e("BRUH", e.toString());
            }

        }
        Iterator it = poiList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            POI temp = (POI) pair.getValue();
            poiStringList.add(temp);
            //Log.d("BRUH", temp.getName());
        }
    }

    private void POIButton(int id, int button) {
        findViewById(id).setOnClickListener(view -> {
                Intent createPoiIntent = new Intent(context, CreateItemActivity_Copy.class);
                createPoiIntent.putExtra("CREATION_TYPE", "POI");
                createPoiIntent.putExtra("Button", button);
                startActivity(createPoiIntent);
        });
    }

    private void answerPOIText(int pos, int layoutID) {
        AutoCompleteTextView textPOI = (AutoCompleteTextView) findViewById(layoutID);
        textPOI.setAdapter(poiStringList);

        textPOI.setOnItemClickListener((parent, view, position, id) -> {
            POI poi = poiStringList.getItem(position);
            pois[pos] = poi;
        });

        AutoCompleteTextView finalTextPOI = textPOI;
        textPOI.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pois[pos] == null || !pois[pos].toString().equals(finalTextPOI.getText()))
                    pois[pos] = null;
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
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

                for (int i = 0; i < pois.length; i++) {
                    if (pois[i] == null)
                        throw new MissingInformationException("Answer " + i+1 + " POI");
                }

                String information = additionalInformation.getText().toString();

                quiz.addQuestion(new Question(id, question, correctAnswer, answers, information, this.pois, initial));

            } catch (MissingInformationException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelButton() {
        findViewById(R.id.cancel_button).setOnClickListener(view -> {
            DialogFragment dialog = new ExitFromQuizFragment();
            dialog.show(this.getSupportFragmentManager(), "dialog");
        });

    }

    private String getTextFromEditText(EditText editText, String whichTextEdit) throws MissingInformationException {
        String toReturn = editText.getText().toString();
        if (toReturn.equals(""))
            throw new MissingInformationException(whichTextEdit);
        return toReturn;
    }

    private void questionPOIText() {
        AutoCompleteTextView textPOI = (AutoCompleteTextView) findViewById(R.id.questionPOITextEdit);
        textPOI.setAdapter(poiStringList);
        final POI[] toReturn = new POI[1];

        textPOI.setOnItemClickListener((parent, view, position, id) -> {
            POI poi = poiStringList.getItem(position);
            toReturn[0] = poi;
        });

        AutoCompleteTextView finalTextPOI = textPOI;
        textPOI.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(toReturn[0] == null || !toReturn[0].toString().equals(finalTextPOI.getText()))
                    toReturn[0] = null;
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        this.initial = toReturn[0] == null ? new POI() : toReturn[0];
    }

    @Override
    public boolean onSupportNavigateUp() {
        DialogFragment dialog = new ExitFromQuizFragment();
        dialog.show(this.getSupportFragmentManager(), "dialog");
        return true;
    }


}

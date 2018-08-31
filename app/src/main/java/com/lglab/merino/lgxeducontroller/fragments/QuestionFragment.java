package com.lglab.merino.lgxeducontroller.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.activities.QuizActivity;
import com.lglab.merino.lgxeducontroller.connection.LGConnectionManager;
import com.lglab.merino.lgxeducontroller.games.quiz.Question;
import com.lglab.merino.lgxeducontroller.games.quiz.QuizManager;
import com.lglab.merino.lgxeducontroller.legacy.beans.POI;
import com.lglab.merino.lgxeducontroller.utils.LGCommand;

public class QuestionFragment extends Fragment {
    private View view;
    private int questionNumber;
    private Question question;
    private TextView textView;
    private TextView[] answerViews;

    private boolean sendInitialPOI = false;

    //LiquidGalaxyAnswerTourView activeTour;
    AlertDialog activeAlertDialog;

    private boolean hasClicked = false;

    private static final POI EARTH_POI = new POI()
                        .setLongitude(10.52668d)
                        .setLatitude(40.085941d)
                        .setAltitude(0.0d)
                        .setHeading(0.0d)
                        .setTilt(0.0d)
                        .setRange(10000000.0d)
                        .setAltitudeMode("relativeToSeaFloor");

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

        if(sendInitialPOI == true) {
            sendInitialPOI = false;
            if(question.initialPOI.getId() != -1) {
                sendPOI(buildCommand(question.initialPOI));
            }
            else {
                sendPOI(buildCommand(EARTH_POI));
            }
        }
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            if(question == null) {
                sendInitialPOI = true;
            }
            else if(question.initialPOI.getId() != -1) {
                sendPOI(buildCommand(question.initialPOI));
            }
            else {
                sendPOI(buildCommand(EARTH_POI));
            }
        }
    }

    public void setClickListener(final int i) {
        view.findViewById(R.id.answerCard1 + i).setOnClickListener(v -> {
            if(!hasClicked) {
                hasClicked = true;
                boolean hadAlreadyClicked = question.selectedAnswer != 0;
                question.selectedAnswer = i + 1;
                view.findViewById(R.id.answerCard1 + question.correctAnswer - 1).setBackgroundColor(Color.parseColor("#5cd65c"));
                answerViews[question.correctAnswer - 1].setTextColor(Color.parseColor("#000000"));

                if (i != question.correctAnswer - 1) {
                    v.setBackgroundColor(Color.parseColor("#ff3333"));
                    answerViews[i].setTextColor(Color.parseColor("#000000"));
                }

                if(!hadAlreadyClicked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    if(question.selectedAnswer != question.correctAnswer) {
                        builder.setTitle("Oops! You've chosen a wrong answer!");
                        builder.setMessage("Going to " + question.pois[question.selectedAnswer - 1].getName());
                        builder.setPositiveButton("SHOW CORRECT ANSWER", (dialog, id) -> {
                            //We override this later in order to prevent alertdialog from closing after clicking this button
                        });

                        sendPOI(buildCommand(question.pois[question.selectedAnswer - 1]));
                    } else {
                        builder.setTitle("Great! You're totally right!");
                        builder.setMessage("Going to " + question.pois[question.correctAnswer - 1].getName());
                        sendPOI(buildCommand(question.pois[question.correctAnswer - 1]));
                    }

                    builder.setNegativeButton("SKIP", (dialog, id) -> dialog.cancel());
                    builder.setOnCancelListener(dialog -> checkQuizProgress());

                    activeAlertDialog = builder.create();
                    activeAlertDialog.show();

                    if(question.selectedAnswer != question.correctAnswer) {
                        activeAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                            sendPOI(buildCommand(question.pois[question.correctAnswer - 1]));
                            activeAlertDialog.setMessage("And now going to " + question.pois[question.correctAnswer - 1].getName());
                            v1.setEnabled(false);
                        });
                    }
                }
            }
        });

        if(question.selectedAnswer == i + 1) {
            view.findViewById(R.id.answerCard1 + i).performClick();
        }
    }

    private void checkQuizProgress() {
        if(QuizManager.getInstance().hasAnsweredAllQuestions()){
            ((QuizActivity)getActivity()).showFloatingExitButton();
        }
    }

    private String buildCommand(POI poi) {
        return "echo 'flytoview=<LookAt><longitude>" + poi.getLongitude() + "</longitude><latitude>" + poi.getLatitude() + "</latitude><altitude>" + poi.getAltitude() + "</altitude><heading>" + poi.getHeading() + "</heading><tilt>" + poi.getTilt() + "</tilt><range>" + poi.getRange() + "</range><gx:altitudeMode>" + poi.getAltitudeMode() + "</gx:altitudeMode></LookAt>' > /tmp/query.txt";
    }

    private void sendPOI(String command) {
        LGConnectionManager.getInstance().addCommandToLG(new LGCommand(command, LGCommand.CRITICAL_MESSAGE));
    }
}

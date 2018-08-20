package com.lglab.merino.lgxeducontroller.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.fragments.ExitFromQuizFragment;
import com.lglab.merino.lgxeducontroller.fragments.QuestionFragment;
import com.lglab.merino.lgxeducontroller.games.quiz.Quiz;
import com.lglab.merino.lgxeducontroller.games.quiz.QuizManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class QuizActivity extends AppCompatActivity {

    ScrollerViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.view_pager);
        SpringIndicator springIndicator = findViewById(R.id.indicator);

        PagerModelManager manager = new PagerModelManager();
        manager.addCommonFragment(QuestionFragment.class, getQuestionsIds(), getTitles());

        ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(), manager);
        viewPager.setAdapter(adapter);
        viewPager.fixScrollSpeed();

        // just set viewPager
        springIndicator.setViewPager(viewPager);
    }

    private List<String> getTitles(){
        int size = QuizManager.getInstance().getQuiz().questions.size();

        ArrayList<String> list = new ArrayList<>(size);

        for(int i = 0; i < size; i++) {
            list.add(String.valueOf(i + 1));
        }

        return list;
    }

    private List<Integer> getQuestionsIds(){
        int size = QuizManager.getInstance().getQuiz().questions.size();

        ArrayList<Integer> list = new ArrayList<>(size);

        for(int i = 0; i < size; i++) {
            list.add(i);
        }

        return list;
    }

    @Override
    public boolean onSupportNavigateUp() {
        DialogFragment dialog = new ExitFromQuizFragment();
        dialog.show(this.getSupportFragmentManager(), "dialog");
        return true;
    }
}




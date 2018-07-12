package com.lglab.merino.lgxeducontroller.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.games.quiz.ArrayAlternateAdapter;
import com.lglab.merino.lgxeducontroller.games.quiz.Quiz;
import com.lglab.merino.lgxeducontroller.legacy.data.POIsProvider;

import org.json.JSONObject;

import java.util.ArrayList;

public class PlayActivityOld extends GoogleDriveActivity {

    ArrayAdapter<Quiz> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

       setContentView(R.layout.activity_play);
       ActionBar actionBar = getSupportActionBar();
       actionBar.setDisplayHomeAsUpEnabled(true);
       actionBar.setTitle(R.string.play);

        ListView lv = findViewById(R.id.listViewQuiz);

        reloadListView();

        lv.setClickable(true);
        lv.setOnItemClickListener((arg0, arg1, position, arg3) -> {

            Quiz quiz = (Quiz)lv.getItemAtPosition(position);
            showMessage(quiz.getNameForExporting());
            startActivity(new Intent(PlayActivityOld.this, QuizActivity.class));
        });

        findViewById(R.id.import_from_drive).setOnClickListener(view -> importQuiz());

   }

   public void reloadListView() {
       ListView lv = findViewById(R.id.listViewQuiz);
       ArrayList<Quiz> arrayQuiz = new ArrayList<>();

       Cursor cursor = POIsProvider.getAllQuizes();
       while (cursor.moveToNext()) {
           long quizId = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
           String questData = cursor.getString(cursor.getColumnIndexOrThrow("Data"));
           try {
               Quiz newQuiz = new Quiz().unpack(new JSONObject(questData));
               newQuiz.id = quizId;
               arrayQuiz.add(newQuiz);
           }
           catch(Exception e) {

           }
       }

       adapter = new ArrayAlternateAdapter<>(
               PlayActivityOld.this,
               android.R.layout.simple_list_item_1,
               arrayQuiz);



       lv.setAdapter(adapter);
   }

    @Override
    public void handleStringFromDrive(String input) {
        try {
            new Quiz().unpack(new JSONObject(input)); //Checking if the json is fine ;)

            POIsProvider.insertQuiz(input);
            reloadListView();
            showMessage("Quiz imported successfully");
        }
        catch(Exception e) {
            showMessage("Couldn't import the file");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }
   
    @Override
    public boolean onSupportNavigateUp() {
       onBackPressed();
       return true;
   }

}

package com.lglab.merino.lgxeducontroller.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.lglab.merino.lgxeducontroller.R;


public class MainActivity extends GoogleDriveActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        button_navigate();
        button_play();
        button_prova_ALEKS();
    }

    private void button_prova_ALEKS() {
        Button aleks = (Button)findViewById(R.id.aleks_button);
        aleks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CreateQuestionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void button_navigate() {
        findViewById(R.id.navigate).setOnClickListener(view -> {
            Toast.makeText(context, "Navigate", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(context, NavigateActivity.class));
        });
    }

    private void button_play() {
        findViewById(R.id.play).setOnClickListener(view -> {
            Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(context, PlayActivity.class));
        });
    }

    @Override
    public void handleStringFromDrive(String input) {
        //Nothing here...
    }
}

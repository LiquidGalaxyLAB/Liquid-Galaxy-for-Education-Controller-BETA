package com.lglab.merino.lgxeducontroller.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.legacy.LGPC;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        button_navigate();
        button_play();
        populate_partners_icons();
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
            startActivity(new Intent(context, LGPC.class));
        });
    }

    private void populate_partners_icons() {
        LinearLayout partnersView = findViewById(R.id.partners);
        TypedArray img = getResources().obtainTypedArray(R.array.partners_icons);
        for (int i = 0; i < img.length(); i++) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageResource(img.getResourceId(i, -1));
            partnersView.addView(imageView);
        }
    }
}

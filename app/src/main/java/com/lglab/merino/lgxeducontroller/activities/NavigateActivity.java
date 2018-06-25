package com.lglab.merino.lgxeducontroller.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.lglab.merino.lgxeducontroller.R;

import java.util.Locale;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class NavigateActivity extends AppCompatActivity {

    public static final String TAG = NavigateActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.navigate);
        joystick_view();
    }

    private void joystick_view() {
        TextView infoTV = findViewById(R.id.info);
        JoystickView joystick = findViewById(R.id.joystickView);
        joystick.setOnMoveListener((angle, strength) -> {
            Log.i(TAG, String.format("Angle: %dº Strength: %d%%", angle, strength));
            infoTV.setText(String.format(Locale.getDefault(), "Angle: %dº\nStrength: %d%%", angle, strength));
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

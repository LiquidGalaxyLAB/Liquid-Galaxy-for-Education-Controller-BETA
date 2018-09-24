package com.lglab.merino.lgxeducontroller.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.legacy.Help;
import com.lglab.merino.lgxeducontroller.legacy.SettingsActivity;


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
        //button_prova();
    }

   /* private void button_prova() {
        Button button = findViewById(R.id.button_button);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(context, CreateQuestionActivity.class);
            startActivity(intent);
        });
    }*/

    private void button_navigate() {
        findViewById(R.id.navigate).setOnClickListener(view -> {
            //Toast.makeText(context, "Navigate", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(context, NavigateActivity.class));
        });
    }

    private void button_play() {
        findViewById(R.id.play).setOnClickListener(view -> {
            //Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(context, PlayActivity.class));
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_lgxedu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings_2) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_information_help) {
            startActivity(new Intent(this, Help.class));
            return true;
        } else if (id == R.id.action_about_2) {
            showAboutDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showAboutDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.about_dialog);
        dialog.setTitle(getResources().getString(R.string.about_Controller_message));

        Button dialogButton = dialog.findViewById(R.id.aboutDialogButtonOK);
        dialogButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void handleStringFromDrive(String input) {
        //Nothing here...
    }
}

package com.lglab.merino.lgxeducontroller.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.drive.GoogleDriveManager;
import com.lglab.merino.lgxeducontroller.games.quiz.Quiz;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static com.google.android.gms.drive.Drive.getDriveResourceClient;
import static com.lglab.merino.lgxeducontroller.drive.GoogleDriveManager.RC_SIGN_IN;

public abstract class GoogleDriveActivity extends AppCompatActivity {

    private TaskCompletionSource<DriveId> mOpenItemTaskSource;

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data));
                    return;
                }

                Log.e(GoogleDriveManager.TAG, "Sign-in failed with requestCode = " + String.valueOf(requestCode));
                finish();
                break;
            case GoogleDriveManager.RC_OPEN_ITEM:
                if (resultCode == RESULT_OK) {
                    mOpenItemTaskSource.setResult(data.getParcelableExtra(
                            OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID));
                } else {
                    mOpenItemTaskSource.setException(new RuntimeException("Unable to open file"));
                }
                break;
        }
    }

    public void signIn() {
        if(GoogleDriveManager.GoogleSignInClient != null && GoogleDriveManager.DriveClient != null && GoogleDriveManager.DriveResourceClient != null)
            return;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Drive.SCOPE_FILE)
                .build();

        GoogleDriveManager.GoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account != null)
            GoogleDriveManager.GoogleSignInClient.signOut();

        Intent signInIntent = GoogleDriveManager.GoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void importQuiz() {
        if(GoogleDriveManager.DriveClient != null && GoogleDriveManager.DriveResourceClient != null) {
            pickTextFile()
                    .addOnSuccessListener(this,
                            driveId -> handleReadItem(driveId.asDriveFile()))
                    .addOnFailureListener(this, e -> {
                        Log.e(GoogleDriveManager.TAG, "No file selected", e);
                        finish();
                    });
        }
    }

    public void exportQuiz(Quiz quiz) {
        if(GoogleDriveManager.DriveClient != null && GoogleDriveManager.DriveResourceClient != null) {
            pickFolder()
                    .addOnSuccessListener(this,
                            driveId -> handleSaveItem(driveId.asDriveFolder(), quiz))
                    .addOnFailureListener(this, e -> {
                        Log.e(GoogleDriveManager.TAG, "No folder selected", e);
                        finish();
                    });
        }
    }

    private Task<DriveId> pickTextFile() {
        OpenFileActivityOptions openOptions =
                new OpenFileActivityOptions.Builder()
                        .setSelectionFilter(Filters.eq(SearchableField.MIME_TYPE, "text/plain"))
                        .setActivityTitle("Select a file")
                        .build();
        return pickItem(openOptions);
    }

    private Task<DriveId> pickFolder() {
        OpenFileActivityOptions openOptions =
                new OpenFileActivityOptions.Builder()
                        .setSelectionFilter(
                                Filters.eq(SearchableField.MIME_TYPE, DriveFolder.MIME_TYPE))
                        .setActivityTitle("Select a folder")
                        .build();
        return pickItem(openOptions);
    }

    private Task<DriveId> pickItem(OpenFileActivityOptions openOptions) {
        mOpenItemTaskSource = new TaskCompletionSource<>();
        GoogleDriveManager.DriveClient
                .newOpenFileActivityIntentSender(openOptions)
                .continueWith((Continuation<IntentSender, Void>) task -> {
                    startIntentSenderForResult(
                            task.getResult(), GoogleDriveManager.RC_OPEN_ITEM, null, 0, 0, 0);
                    return null;
                });
        return mOpenItemTaskSource.getTask();
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleDriveManager.DriveClient = Drive.getDriveClient(this, account);
            GoogleDriveManager.DriveResourceClient = getDriveResourceClient(this, account);
            showMessage("Logged into " + account.getEmail());

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(GoogleDriveManager.TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void handleReadItem(final DriveFile file) {
        try {
            GoogleDriveManager.DriveResourceClient.openFile(file, DriveFile.MODE_READ_ONLY)
                    .continueWithTask(task -> {
                        DriveContents contents = task.getResult();
                        try (BufferedReader reader = new BufferedReader(
                                new InputStreamReader(contents.getInputStream()))) {

                            StringBuilder builder = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                builder.append(line).append("\n");
                            }

                            handleStringFromDrive(builder.toString());
                            //quiz = new Quiz().unpack(new JSONObject(builder.toString()));
                        }

                        Task<Void> discardTask = GoogleDriveManager.DriveResourceClient.discardContents(contents);
                        return discardTask;
                    })
                    .addOnSuccessListener(this,
                            driveFile -> showMessage("File imported successfully"))
                    .addOnFailureListener(e -> {
                        Log.e(GoogleDriveManager.TAG, e.toString());
                    });
        } catch (Exception e) {
            Log.e(GoogleDriveManager.TAG, e.toString());
        }
    }

    private void handleSaveItem(final DriveFolder parent, Quiz quiz) {
        GoogleDriveManager.DriveResourceClient
                .createContents()
                .continueWithTask(task -> {
                    DriveContents contents = task.getResult();
                    OutputStream outputStream = contents.getOutputStream();
                    try (Writer writer = new OutputStreamWriter(outputStream)) {
                        writer.write(quiz.pack().toString());
                    }

                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle(quiz.getNameForExporting())
                            .setMimeType("text/plain")
                            .setStarred(true)
                            .build();

                    return GoogleDriveManager.DriveResourceClient.createFile(parent, changeSet, contents);
                })
                .addOnSuccessListener(this,
                        driveFile -> showMessage("File saved successfully with name " + quiz.getNameForExporting()))
                .addOnFailureListener(this, e -> {
                    Log.e(GoogleDriveManager.TAG, "Unable to create file", e);
                    showMessage("Unable to create file");
                });
    }


    private void showMessage(String message){
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.snackbarPosition), message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    public abstract void handleStringFromDrive(String input);
}

package com.lglab.merino.lgxeducontroller.activities;

import android.app.Activity;
import android.content.Intent;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static com.google.android.gms.drive.Drive.getDriveResourceClient;

public class GoogleDriveManager {

    private static final String TAG = "drive_log_in";

    private static final int RC_SIGN_IN = 0;
    private static final int RC_OPEN_ITEM = 1;

    private static GoogleSignInClient mGoogleSignInClient;
    private static DriveClient mDriveClient;
    private static DriveResourceClient mDriveResourceClient;

    private static TaskCompletionSource<DriveId> mOpenItemTaskSource;

    public static void initialize(Activity activity) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Drive.SCOPE_FILE)
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account != null)
            mGoogleSignInClient.signOut();

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private static void handleSignInResult(Task<GoogleSignInAccount> completedTask, Activity activity) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            mDriveClient = Drive.getDriveClient(this, account);
            mDriveResourceClient = getDriveResourceClient(this, account);
            showMessage("Logged into " + account.getEmail(), activity);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private static void handleReadItem(final DriveFile file, Activity activity) {
        try {
            mDriveResourceClient.openFile(file, DriveFile.MODE_READ_ONLY)
                    .continueWithTask(task -> {
                        DriveContents contents = task.getResult();
                        try (BufferedReader reader = new BufferedReader(
                                new InputStreamReader(contents.getInputStream()))) {

                            StringBuilder builder = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                builder.append(line).append("\n");
                            }

                            //quiz = new Quiz().unpack(new JSONObject(builder.toString()));
                        }

                        Task<Void> discardTask = mDriveResourceClient.discardContents(contents);
                        return discardTask;
                    })
                    .addOnSuccessListener(this,
                            driveFile -> showMessage("File imported successfully", activity))
                    .addOnFailureListener(e -> {
                        Log.e(TAG, e.toString());
                    });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private static void handleSaveItem(final DriveFolder parent, Activity activity) {
        mDriveResourceClient
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

                    return mDriveResourceClient.createFile(parent, changeSet, contents);
                })
                .addOnSuccessListener(this,
                        driveFile -> showMessage("File saved successfully with name " + quiz.getNameForExporting()))
                .addOnFailureListener(this, e -> {
                    Log.e(TAG, "Unable to create file", e);
                    showMessage("Unable to create file");
                });
    }

}

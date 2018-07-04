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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public abstract class GoogleDriveActivity extends AppCompatActivity {

    public TaskCompletionSource<DriveId> mOpenItemTaskSource;

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GoogleDriveManager.RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    GoogleDriveManager.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data), this);
                    return;
                }

                Log.e(GoogleDriveManager.TAG, "Sign-in failed.");
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

    public Task<DriveId> pickTextFile(DriveClient mDriveClient) {
        OpenFileActivityOptions openOptions =
                new OpenFileActivityOptions.Builder()
                        .setSelectionFilter(Filters.eq(SearchableField.MIME_TYPE, "text/plain"))
                        .setActivityTitle("Select a file")
                        .build();
        return pickItem(mDriveClient, openOptions);
    }

    public Task<DriveId> pickFolder(DriveClient mDriveClient) {
        OpenFileActivityOptions openOptions =
                new OpenFileActivityOptions.Builder()
                        .setSelectionFilter(
                                Filters.eq(SearchableField.MIME_TYPE, DriveFolder.MIME_TYPE))
                        .setActivityTitle("Select a folder")
                        .build();
        return pickItem(mDriveClient, openOptions);
    }

    private Task<DriveId> pickItem(DriveClient mDriveClient, OpenFileActivityOptions openOptions) {
        mOpenItemTaskSource = new TaskCompletionSource<>();
        mDriveClient
                .newOpenFileActivityIntentSender(openOptions)
                .continueWith((Continuation<IntentSender, Void>) task -> {
                    startIntentSenderForResult(
                            task.getResult(), GoogleDriveManager.RC_OPEN_ITEM, null, 0, 0, 0);
                    return null;
                });
        return mOpenItemTaskSource.getTask();
    }


    public void showMessage(String message){
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.snackbarPosition), message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

}

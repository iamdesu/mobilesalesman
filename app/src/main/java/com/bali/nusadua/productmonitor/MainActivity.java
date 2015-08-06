package com.bali.nusadua.productmonitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bali.nusadua.productmonitor.dropbox.DownloadDataFromDropbox;
import com.bali.nusadua.productmonitor.dropbox.DropboxHelper;
import com.bali.nusadua.productmonitor.dropbox.UploadFileToDropbox;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private final static String APP_KEY = "shhgratsyvnsh4p";
    private final static String APP_SECRET = "hfvq8mizpla3992";

    private final static String FILE_DIR_EXPORT = "/MobileSalesman/Export/";
    private final static String FILE_DIR_IMPORT = "/MobileSalesman/Import/";

    private final static String DROPBOX_PREFS_NAME = "dropbox_prefs";
    private static final String ACCESS_KEY_NAME = "ACCESS_KEY";
    private static final String ACCESS_SECRET_NAME = "ACCESS_SECRET";

    private static final boolean USE_OAUTH1 = false;

    private DropboxAPI<AndroidAuthSession> dropboxApi;
    private boolean mLoggedIn;

    //Android widget
    private Button btnSendData, btnRetrieveData, btnTransaction, btnExit;
    private TextView tvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We create a new AuthSession so that we can use the Dropbox API.
        AndroidAuthSession session = DropboxHelper.buildSession(MainActivity.this);
        dropboxApi = new DropboxAPI<AndroidAuthSession>(session);

        // Basic Android widgets
        setContentView(R.layout.activity_main);

        checkAppKeySetup();

        btnRetrieveData = (Button) findViewById(R.id.btn_retrieve_data);
        btnSendData = (Button) findViewById(R.id.btn_sent_data);
        btnTransaction = (Button) findViewById(R.id.btn_transaction);
        btnExit = (Button) findViewById(R.id.btn_exit);

        btnRetrieveData.setOnClickListener(this);
        btnSendData.setOnClickListener(this);
        btnTransaction.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        tvUsername = (TextView) findViewById(R.id.staff_name);

        // Display the proper UI state if logged in or not
        setLoggedIn(dropboxApi.getSession().isLinked());

        // Display Greeting
        displayUsername();

        connectDropbox();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you medirecords_adminspecify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reconnect) {
            logOut();
            connectDropbox();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.btn_retrieve_data)) {
            onButtonRetrieveDataClick(view);
        } else if (view == findViewById(R.id.btn_transaction)) {
            onButtonTransactionClick(view);
        } else if (view == findViewById(R.id.btn_sent_data)) {
            onButtonSentDataClick(view);
        } else if (view == findViewById(R.id.btn_exit)) {
            onButtonExitClick(view);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidAuthSession session = dropboxApi.getSession();

        // The next part must be inserted in the onResume() method of the
        // activity from which session.startAuthentication() was called, so
        // that Dropbox authentication completes properly.
        if (session.authenticationSuccessful()) {
            try {
                // Mandatory call to complete the auth
                session.finishAuthentication();

                // Store it locally in our app for later use
                storeAuth(session);

                if (mLoggedIn != true) {
                    downloadFileFromDropBox();
                }

                setLoggedIn(true);
            } catch (IllegalStateException e) {
                showToast("Couldn't authenticate with Dropbox:" + e.getLocalizedMessage());
                Log.i(TAG, "Error authenticating", e);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*displayUsername();*/
    }

    public void onButtonRetrieveDataClick(View view) {
        if (mLoggedIn == true) {
            Intent intent = new Intent(MainActivity.this, AmbilDataActivity.class);
            startActivityForResult(intent, 0);
        } else {
            showToast("Please login to DropBox");
            connectDropbox();
        }
    }

    public void onButtonTransactionClick(View view) {
        Intent intent = new Intent(MainActivity.this, ViewOutletActivity.class);
        startActivity(intent);
    }

    public void onButtonSentDataClick(View view) {
        if (mLoggedIn == true) {
            Log.i("Button Kirim data", " Klik");
            SharedPreferences prefs = getSharedPreferences(MSConstantsIntf.MOBILESALES_PREFS_NAME, 0);
            String team = prefs.getString(MSConstantsIntf.TEAM, null);

            if (team != null && !team.isEmpty()) {
                uploadFileToDropBox();
            } else {
                showToast("Mohon login dan ambil data terbaru");
            }
        } else {
            showToast("Please login to DropBox");
            connectDropbox();
        }
    }

    public void onButtonExitClick(View view) {
        finish();
        System.exit(0);
    }

    private AndroidAuthSession buildSession() {
        AppKeyPair pair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(pair);
        loadAuth(session);

        return session;
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     */
    private void loadAuth(AndroidAuthSession session) {
        SharedPreferences prefs = getSharedPreferences(DROPBOX_PREFS_NAME, 0);
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
        if (key == null || secret == null || key.length() == 0 || secret.length() == 0) return;

        if (key.equals("oauth2:")) {
            // If the key is set to "oauth2:", then we can assume the token is for OAuth 2.
            session.setOAuth2AccessToken(secret);
        } else {
            // Still support using old OAuth 1 tokens.
            session.setAccessTokenPair(new AccessTokenPair(key, secret));
        }
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     */
    private void storeAuth(AndroidAuthSession session) {
        // Store the OAuth 2 access token, if there is one.
        String oauth2AccessToken = session.getOAuth2AccessToken();
        if (oauth2AccessToken != null) {
            SharedPreferences prefs = getSharedPreferences(DROPBOX_PREFS_NAME, 0);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(ACCESS_KEY_NAME, "oauth2:");
            edit.putString(ACCESS_SECRET_NAME, oauth2AccessToken);
            edit.commit();
            return;
        }
        // Store the OAuth 1 access token, if there is one.  This is only necessary if
        // you're still using OAuth 1.
        AccessTokenPair oauth1AccessToken = session.getAccessTokenPair();
        if (oauth1AccessToken != null) {
            SharedPreferences prefs = getSharedPreferences(DROPBOX_PREFS_NAME, 0);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(ACCESS_KEY_NAME, oauth1AccessToken.key);
            edit.putString(ACCESS_SECRET_NAME, oauth1AccessToken.secret);
            edit.commit();
            return;
        }
    }

    private void logOut() {
        // Remove credentials from the session
        dropboxApi.getSession().unlink();

        // Clear our stored keys
        clearKeys();
        // Change UI state to display logged out version
        setLoggedIn(false);
    }

    private void clearKeys() {
        SharedPreferences prefs = getSharedPreferences(DROPBOX_PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }

    /**
     * Convenience function to change UI state based on being logged in
     */
    private void setLoggedIn(boolean loggedIn) {
        mLoggedIn = loggedIn;
        /*if (loggedIn) {
            mSubmit.setText("Unlink from Dropbox");
            mDisplay.setVisibility(View.VISIBLE);
        } else {
            mSubmit.setText("Link with Dropbox");
            mDisplay.setVisibility(View.GONE);
            mImage.setImageDrawable(null);
        }*/
    }

    private void checkAppKeySetup() {
        // Check to make sure that we have a valid app key
        if (APP_KEY.startsWith("CHANGE") ||
                APP_SECRET.startsWith("CHANGE")) {
            showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");
            finish();
            return;
        }

        // Check if the app has set up its manifest properly.
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        String scheme = "db-" + APP_KEY;
        String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
        testIntent.setData(Uri.parse(uri));
        PackageManager pm = getPackageManager();
        if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
            showToast("URL scheme in your app's " +
                    "manifest is not set up correctly. You should have a " +
                    "com.dropbox.client2.android.AuthActivity with the " +
                    "scheme: " + scheme);
            finish();
        }
    }

    private void displayUsername() {
        SharedPreferences prefs = getSharedPreferences(MSConstantsIntf.MOBILESALES_PREFS_NAME, 0);
        String staffName = prefs.getString(MSConstantsIntf.STAFF_NAME, null);
        if (staffName == null || staffName.length() == 0) return;

        tvUsername.setText("User Login: " + staffName);
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }

    private void connectDropbox() {
        DropboxHelper.connectDropbox(MainActivity.this, dropboxApi, mLoggedIn);
    }

    private void uploadFileToDropBox() {
        ProgressDialog progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getResources().getString(R.string.file_uploading));
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        UploadFileToDropbox upload = new UploadFileToDropbox(this, dropboxApi, FILE_DIR_EXPORT, progressBar);
        upload.execute();
    }

    private void downloadFileFromDropBox() {
        ProgressDialog progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getResources().getString(R.string.file_setup));
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        DownloadDataFromDropbox download = new DownloadDataFromDropbox(this, dropboxApi, DropboxHelper.FILE_DIR_IMPORT, false, "", progressBar, false);
        download.execute();
    }
}
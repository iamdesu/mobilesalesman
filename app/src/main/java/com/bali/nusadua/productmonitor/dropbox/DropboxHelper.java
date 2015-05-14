package com.bali.nusadua.productmonitor.dropbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.bali.nusadua.productmonitor.AmbilDataActivity;
import com.bali.nusadua.productmonitor.MainActivity;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;

public class DropboxHelper {
    private final static String APP_KEY = "shhgratsyvnsh4p";
    private final static String APP_SECRET = "hfvq8mizpla3992";

    private final static String DROPBOX_PREFS_NAME = "dropbox_prefs";
    private static final String ACCESS_KEY_NAME = "ACCESS_KEY";
    private static final String ACCESS_SECRET_NAME = "ACCESS_SECRET";

    private static final boolean USE_OAUTH1 = false;

    private final static String FILE_DIR_EXPORT = "/MobileSalesman/Export/";
    private final static String FILE_DIR_IMPORT = "/MobileSalesman/Import/";

    public static AndroidAuthSession buildSession(Context context) {
        AppKeyPair pair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(pair);
        loadAuth(session, context);

        return session;
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     */
    public static void loadAuth(AndroidAuthSession session, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DROPBOX_PREFS_NAME, 0);
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
    public static void storeAuth(Context context, AndroidAuthSession session) {
        // Store the OAuth 2 access token, if there is one.
        String oauth2AccessToken = session.getOAuth2AccessToken();
        if (oauth2AccessToken != null) {
            SharedPreferences prefs = context.getSharedPreferences(DROPBOX_PREFS_NAME, 0);
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
            SharedPreferences prefs = context.getSharedPreferences(DROPBOX_PREFS_NAME, 0);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(ACCESS_KEY_NAME, oauth1AccessToken.key);
            edit.putString(ACCESS_SECRET_NAME, oauth1AccessToken.secret);
            edit.commit();
            return;
        }
    }

    public static void checkAppKeySetup(Activity ativity) {
        // Check to make sure that we have a valid app key
        if (APP_KEY.startsWith("CHANGE") ||
                APP_SECRET.startsWith("CHANGE")) {
            showToast(ativity.getApplicationContext(), "You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");
            ativity.finish();
            return;
        }

        // Check if the app has set up its manifest properly.
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        String scheme = "db-" + APP_KEY;
        String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
        testIntent.setData(Uri.parse(uri));
        PackageManager pm = ativity.getApplicationContext().getPackageManager();
        if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
            showToast(ativity.getApplicationContext(), "URL scheme in your app's " +
                    "manifest is not set up correctly. You should have a " +
                    "com.dropbox.client2.android.AuthActivity with the " +
                    "scheme: " + scheme);
            ativity.finish();
        }
    }

    public static void logOut(Context context, DropboxAPI<AndroidAuthSession> dropboxApi, Boolean mLoggedIn) {
        // Remove credentials from the session
        dropboxApi.getSession().unlink();

        // Clear our stored keys
        clearKeys(context);

        // Change UI state to display logged out version
        mLoggedIn = false;
    }

    private static void showToast(Context context, String msg) {
        Toast error = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        error.show();
    }

    private static void clearKeys(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DROPBOX_PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }

    public static void connectDropbox(Context context, DropboxAPI<AndroidAuthSession> dropboxApi, Boolean mLoggedIn){
        // This logs you out if you're logged in, or vice versa
        if (mLoggedIn) {
            //logOut();
        } else {
            // Start the remote authentication
            if (USE_OAUTH1) {
                dropboxApi.getSession().startAuthentication(context);
            } else {
                dropboxApi.getSession().startOAuth2Authentication(context);
            }
        }
    }
}

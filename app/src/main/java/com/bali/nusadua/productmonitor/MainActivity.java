package com.bali.nusadua.productmonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bali.nusadua.productmonitor.dropbox.UploadFileToDropbox;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.dropbox.client2.session.TokenPair;


public class MainActivity extends ActionBarActivity {

    private DropboxAPI<AndroidAuthSession> dropbox;
    private final static String FILE_DIR = "/mobilesalesman/";
    private final static String DROPBOX_NAME = "dropbox_prefs";
    private final static String ACCESS_KEY = "xro5idej2ikdb4f";
    private final static String ACCESS_SECRET = "k8egpv6pt3an92l";
    private boolean isLoggedIn;
    private Button mBtnKirimData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnKirimData = (Button) findViewById(R.id.btn_kirim_data);

        //dipake buat autentikasi dropbox
        loggedIn(false);
        AndroidAuthSession session;
        AppKeyPair pair = new AppKeyPair(ACCESS_KEY, ACCESS_SECRET);

        SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, 0);
        String key = prefs.getString(ACCESS_KEY, null);
        String secret = prefs.getString(ACCESS_SECRET, null);

        if (key != null && secret != null) {
            AccessTokenPair token = new AccessTokenPair(key, secret);
            session = new AndroidAuthSession(pair, Session.AccessType.APP_FOLDER, token);
        } else {
            session = new AndroidAuthSession(pair, Session.AccessType.APP_FOLDER);
        }
        dropbox = new DropboxAPI<AndroidAuthSession>(session);

        loginDropBox();
    }

    private void loginDropBox(){
            //buat log out drop box
            dropbox.getSession().unlink();

            //buat login dropbox
            dropbox.getSession().startAuthentication(MainActivity.this);

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonCloseClick(View view) {
        finish();
        System.exit(0);
    }

    public void onButtonAmbilDataClick(View view) {
        Intent intent = new Intent(MainActivity.this, AmbilDataActivity.class);
        startActivity(intent);
    }

    public void onTransaksiClick(View view) {
        Intent intent = new Intent(MainActivity.this, TransaksiActivity.class);
        startActivity(intent);
    }

    public void onButtonKirimDataClick(View view){
        Log.i("Button ambil data", " Klik");
        uploadFileToDropBox();
    }

    private void uploadFileToDropBox(){
        loginDropBox();
        UploadFileToDropbox upload = new UploadFileToDropbox(this, dropbox,
                FILE_DIR);
        upload.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        AndroidAuthSession session = dropbox.getSession();
        if (session.authenticationSuccessful()) {
            try {
                session.finishAuthentication();
                TokenPair tokens = session.getAccessTokenPair();
                SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(ACCESS_KEY, tokens.key);
                editor.putString(ACCESS_SECRET, tokens.secret);
                editor.commit();
                loggedIn(true);
            } catch (IllegalStateException e) {
                Toast.makeText(this, "Error during Dropbox authentication",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loggedIn(boolean isLogged) {
        isLoggedIn = isLogged;
        mBtnKirimData.setEnabled(isLogged);
    }
}

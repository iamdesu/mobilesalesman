package com.bali.nusadua.productmonitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bali.nusadua.productmonitor.dropbox.DownloadDataFromDropbox;
import com.bali.nusadua.productmonitor.dropbox.DropboxHelper;
import com.bali.nusadua.productmonitor.model.StaffBilling;
import com.bali.nusadua.productmonitor.repo.StaffBillingRepo;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

public class AmbilDataActivity extends ActionBarActivity {

    private ProgressDialog progressBar;
    private EditText etUserId;
    private EditText etPassword;

    private DropboxAPI<AndroidAuthSession> dropboxApi;
    private boolean mLoggedIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We create a new AuthSession so that we can use the Dropbox API.
        AndroidAuthSession session = DropboxHelper.buildSession(AmbilDataActivity.this);
        dropboxApi = new DropboxAPI<AndroidAuthSession>(session);

        setContentView(R.layout.activity_retrieve_data);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        etUserId = (EditText) findViewById(R.id.user_id);
        etPassword = (EditText) findViewById(R.id.password);

        // Display the proper UI state if logged in or not
        setLoggedIn(dropboxApi.getSession().isLinked());

        DropboxHelper.connectDropbox(AmbilDataActivity.this, dropboxApi, mLoggedIn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_retrieve_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you medirecords_adminspecify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();

                break;

            case R.id.action_refresh_user:
                progressBar = new ProgressDialog(AmbilDataActivity.this);
                progressBar.setCancelable(false);
                progressBar.setMessage(getResources().getString(R.string.file_setup));
                progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBar.show();

                DownloadDataFromDropbox download = new DownloadDataFromDropbox(this, dropboxApi, DropboxHelper.FILE_DIR_IMPORT, false, "", progressBar, false);
                download.execute();

                break;
        }

        return true;
    }

    public void onBtnProsesClick(View view) {
        StaffBillingRepo staffBillingRepo = new StaffBillingRepo(AmbilDataActivity.this);
        StaffBilling staffBilling = staffBillingRepo.getStaffBilling(etUserId.getText().toString(), etPassword.getText().toString());

        if(staffBilling != null) {
            progressBar = new ProgressDialog(AmbilDataActivity.this);
            progressBar.setCancelable(false);
            progressBar.setMessage(getResources().getString(R.string.file_downloading));
            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();

            storeLogin(staffBilling);

            DownloadDataFromDropbox download = new DownloadDataFromDropbox(this, dropboxApi, DropboxHelper.FILE_DIR_IMPORT, true, "", progressBar, true);
            download.execute();
        } else {
            showToast(getResources().getString(R.string.failed_login));
        }
    }

    public void onBtnKeluarClick(View view) {
        //kalo mau pindah ke halaman sebelumnya,
        //cukup panggil method finish() buat matiin activity
        //nanti otomatis kembali ke halaman sebelumnya
        //asal activity sebelumnya masih nyala
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setLoggedIn(boolean loggedIn) {
        mLoggedIn = loggedIn;
    }

    private void storeLogin(StaffBilling staffBilling) {
        SharedPreferences prefs = getSharedPreferences(MSConstantsIntf.MOBILESALES_PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(MSConstantsIntf.USER_ID, staffBilling.getUserID());
        edit.putString(MSConstantsIntf.STAFF_NAME, staffBilling.getStaffName());
        edit.putString(MSConstantsIntf.TEAM, staffBilling.getTeam());
        edit.commit();
    }

    private void showToast(String msg) {
        Toast info = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        info.show();
    }
}

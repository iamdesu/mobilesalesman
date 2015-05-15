package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bali.nusadua.productmonitor.dropbox.DownloadDataFromDropbox;
import com.bali.nusadua.productmonitor.dropbox.DropboxHelper;
import com.bali.nusadua.productmonitor.model.StaffBilling;
import com.bali.nusadua.productmonitor.repo.StaffBillingRepo;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

public class AmbilDataActivity extends Activity {

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

        setContentView(R.layout.activity_ambil_data);
        etUserId = (EditText) findViewById(R.id.user_id);
        etPassword = (EditText) findViewById(R.id.password);

        // Display the proper UI state if logged in or not
        setLoggedIn(dropboxApi.getSession().isLinked());

        DropboxHelper.connectDropbox(AmbilDataActivity.this, dropboxApi, mLoggedIn);
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

            DownloadDataFromDropbox download = new DownloadDataFromDropbox(this, dropboxApi, DropboxHelper.FILE_DIR_IMPORT, true, "", progressBar);
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
        finish();
    }

    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent == findViewById(R.id.spinnerTeam)){
            Team selectedTeam = (Team) parent.getItemAtPosition(position);
            mTvSelectedTeam.setText(selectedTeam.getName());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mTvSelectedTeam.setText("Not selected");
    }*/



    private void setLoggedIn(boolean loggedIn) {
        mLoggedIn = loggedIn;
    }

    private void showToast(String msg) {
        Toast info = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        info.show();
    }
}

package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bali.nusadua.productmonitor.adapter.SpinnerTeamAdapter;
import com.bali.nusadua.productmonitor.dropbox.DownloadDataFromDropbox;
import com.bali.nusadua.productmonitor.dropbox.DropboxHelper;
import com.bali.nusadua.productmonitor.model.Team;
import com.bali.nusadua.productmonitor.repo.TeamRepo;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;

import java.util.List;

/**
 * Created by desu sudarsana on 4/4/2015.
 */
public class AmbilDataActivity extends Activity implements AdapterView.OnItemSelectedListener{

    Spinner mSpinnerTeam;
    TextView mTvSelectedTeam;
    private ProgressDialog progressBar;

    private DropboxAPI<AndroidAuthSession> dropboxApi;
    private boolean mLoggedIn;
    private final static String APP_KEY = "shhgratsyvnsh4p";
    private final static String APP_SECRET = "hfvq8mizpla3992";
    private final static String DROPBOX_PREFS_NAME = "dropbox_prefs";
    private static final String ACCESS_KEY_NAME = "ACCESS_KEY";
    private static final String ACCESS_SECRET_NAME = "ACCESS_SECRET";
    private static final boolean USE_OAUTH1 = false;
    private final static String FILE_DIR_IMPORT = "/MobileSalesman/Import/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We create a new AuthSession so that we can use the Dropbox API.
        AndroidAuthSession session = DropboxHelper.buildSession(AmbilDataActivity.this);
        dropboxApi = new DropboxAPI<AndroidAuthSession>(session);

        setContentView(R.layout.activity_ambil_data);
        mTvSelectedTeam = (TextView) findViewById(R.id.tv_selected_team);
        mSpinnerTeam = (Spinner) findViewById(R.id.spinnerTeam);
        mSpinnerTeam.setOnItemSelectedListener(this);

        //insertDummyData();
        loadTeam();

        // Display the proper UI state if logged in or not
        setLoggedIn(dropboxApi.getSession().isLinked());

        DropboxHelper.connectDropbox(AmbilDataActivity.this, dropboxApi, mLoggedIn);
    }

    public void onBtnProsesClick(View view) {
        progressBar = new ProgressDialog(AmbilDataActivity.this);
        progressBar.setCancelable(false);
        progressBar.setMessage("File downloading ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        DownloadDataFromDropbox download = new DownloadDataFromDropbox(this, dropboxApi, FILE_DIR_IMPORT, progressBar);
        download.execute();
    }

    public void onBtnKeluarClick(View view) {
        //kalo mau pindah ke halaman sebelumnya,
        //cukup panggil method finish() buat matiin activity
        //nanti otomatis kembali ke halaman sebelumnya
        //asal activity sebelumnya masih nyala
        finish();
//        Intent intent = new Intent(AmbilDataActivity.this, MainActivity.class);
//        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent == findViewById(R.id.spinnerTeam)){
            Team selectedTeam = (Team) parent.getItemAtPosition(position);
            mTvSelectedTeam.setText(selectedTeam.getName());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mTvSelectedTeam.setText("Not selected");
    }

    private void insertDummyData(){
        TeamRepo repo = new TeamRepo(this);
        //repo.deleteAll();
        repo.delete();

        Log.i("Starting insert data", "data");
        for(int i = 0; i < 5; i++){
            Team team = new Team();
            team.setId(i);
            team.setGuid("");
            team.setName("Team " + i);
            long guid = repo.insert(team);
        }
    }

    private void loadTeam(){
        SpinnerTeamAdapter adapter = null;
        TeamRepo repo = new TeamRepo(getApplicationContext());
        List<Team> teams = repo.getAll();
        int size = teams.size();
        Log.i("List size : ", Integer.toString(size));
        for(int i = 0; i < size; i++){
            Log.i("Teams", teams.get(i).getName());
        }
        adapter = new SpinnerTeamAdapter(AmbilDataActivity.this,
                android.R.layout.simple_spinner_item, teams);
        mSpinnerTeam.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
    }

    private void setLoggedIn(boolean loggedIn) {
        mLoggedIn = loggedIn;
    }
}

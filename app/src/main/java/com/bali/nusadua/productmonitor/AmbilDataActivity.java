package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bali.nusadua.productmonitor.adapter.SpinnerTeamAdapter;
import com.bali.nusadua.productmonitor.model.Team;
import com.bali.nusadua.productmonitor.repo.TeamRepo;

import java.util.List;

/**
 * Created by desu sudarsana on 4/4/2015.
 */
public class AmbilDataActivity extends Activity implements AdapterView.OnItemSelectedListener{

    Spinner mSpinnerTeam;
    TextView mTvSelectedTeam;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambil_data);
        mTvSelectedTeam = (TextView) findViewById(R.id.tv_selected_team);
        mSpinnerTeam = (Spinner) findViewById(R.id.spinnerTeam);
        mSpinnerTeam.setOnItemSelectedListener(this);

        insertDummyData();
        loadTeam();
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
}

package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bali.nusadua.productmonitor.adapter.SpinnerTeamAdapter;
import com.bali.nusadua.productmonitor.model.Team;
import com.bali.nusadua.productmonitor.repo.TeamRepo;

import java.util.List;

/**
 * Created by desu sudarsana on 4/19/2015.
 */
public class TransaksiActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Button btnSummary, btnProses;
    private LinearLayout blockOrderPenjualan, blockRetur, blockPelunasan;
    private Spinner spinnerOutlet;
    private Team team = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        //Declare component UI
        btnProses = (Button) findViewById(R.id.btn_proses);
        btnSummary = (Button) findViewById(R.id.btn_summary);
        blockOrderPenjualan = (LinearLayout) findViewById(R.id.block_order);
        spinnerOutlet = (Spinner) findViewById(R.id.spinnerOutlet);

        btnProses.setOnClickListener(this);
        btnSummary.setOnClickListener(this);
        blockOrderPenjualan.setOnClickListener(this);
        spinnerOutlet.setOnItemSelectedListener(this);

        loadTeam();
    }

    @Override
    public void onClick(View view) {
        if(view == findViewById(R.id.block_order) && team != null) {
            Intent intent = new Intent(TransaksiActivity.this, OrderPenjualanActivity.class);
            intent.putExtra("team_guid", team.getGuid());
            startActivity(intent);
        }
    }

    private void loadTeam() {
        SpinnerTeamAdapter adapter = null;
        TeamRepo repo = new TeamRepo(getApplicationContext());
        List<Team> teams = repo.getAll();
        int size = teams.size();
        Log.i("List size : ", Integer.toString(size));
        for(int i = 0; i < size; i++){
            Log.i("Teams", teams.get(i).getName());
        }
        adapter = new SpinnerTeamAdapter(TransaksiActivity.this,
                android.R.layout.simple_spinner_item, teams);
        spinnerOutlet.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent == findViewById(R.id.spinnerOutlet)){
            Team selectedTeam = (Team) parent.getItemAtPosition(position);
            team = selectedTeam;
            Log.i("Team GUID : ", team.getGuid());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        team = null;
    }
}

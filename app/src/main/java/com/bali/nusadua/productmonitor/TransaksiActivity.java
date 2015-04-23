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

import com.bali.nusadua.productmonitor.adapter.SpinnerOutletAdapter;
import com.bali.nusadua.productmonitor.model.Outlet;
import com.bali.nusadua.productmonitor.repo.OutletRepo;

import java.util.List;

/**
 * Created by desu sudarsana on 4/19/2015.
 */
public class TransaksiActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Button btnSummary, btnProses;
    private LinearLayout blockOrderPenjualan, blockRetur, blockPelunasan;
    private Spinner spinnerOutlet;
    private Outlet outlet = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        //Declare component UI
        btnProses = (Button) findViewById(R.id.btn_proses);
        btnSummary = (Button) findViewById(R.id.btn_summary);
        blockOrderPenjualan = (LinearLayout) findViewById(R.id.block_order);
        blockRetur = (LinearLayout) findViewById(R.id.block_retur);
        spinnerOutlet = (Spinner) findViewById(R.id.spinnerOutlet);

        btnProses.setOnClickListener(this);
        btnSummary.setOnClickListener(this);
        blockOrderPenjualan.setOnClickListener(this);
        blockRetur.setOnClickListener(this);
        spinnerOutlet.setOnItemSelectedListener(this);

        loadOutlet();
    }

    @Override
    public void onClick(View view) {
        if(view == findViewById(R.id.block_order) && outlet != null) {
            Intent intent = new Intent(TransaksiActivity.this, OrderPenjualanActivity.class);
            intent.putExtra("kode_outlet", outlet.getKode());
            startActivity(intent);
        } else if(view == findViewById(R.id.block_retur) && outlet != null) {
            Intent intent = new Intent(TransaksiActivity.this, ReturPenjualanActivity.class);
            intent.putExtra("kode_outlet", outlet.getKode());
            startActivity(intent);
        }
    }

    private void loadOutlet() {
        SpinnerOutletAdapter adapter = null;
        OutletRepo repo = new OutletRepo(getApplicationContext());
        List<Outlet> outlets = repo.getAll();
        int size = outlets.size();
        Log.i("List size : ", Integer.toString(size));
        /*for(int i = 0; i < size; i++){
            Log.i("Outlets", outlets.get(i).getName());
        }*/
        adapter = new SpinnerOutletAdapter(TransaksiActivity.this,
                android.R.layout.simple_spinner_item, outlets);
        spinnerOutlet.setAdapter(adapter);

        outlet = outlets.get(0);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent == findViewById(R.id.spinnerOutlet)){
            Outlet selectedOutlet = (Outlet) parent.getItemAtPosition(position);
            outlet = selectedOutlet;
            Log.i("Outlet KODE : ", outlet.getKode());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        outlet = null;
    }
}

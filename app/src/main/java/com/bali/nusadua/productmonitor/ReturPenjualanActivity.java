package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bali.nusadua.productmonitor.model.Retur;
import com.bali.nusadua.productmonitor.repo.ReturRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by desu sudarsana on 4/23/2015.
 */
public class ReturPenjualanActivity extends Activity implements android.view.View.OnClickListener {

    private Button btnAdd, btnProses, btnBatal;
    private TableLayout theGrid;
    private EditText tvCode, tvName, tvPrice, tvQty, tvUnit;
    private String kodeOutlet;

    private List<Retur> returs = new ArrayList<Retur>();
    private ReturRepo returRepo = new ReturRepo(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retur_penjualan);

        //Declare UI component
        btnAdd = (Button) findViewById(R.id.button_add);
        btnProses = (Button) findViewById(R.id.button_proses);
        btnBatal = (Button) findViewById(R.id.button_batal);
        theGrid = (TableLayout) findViewById(R.id.tableLayoutData);
        tvCode = (EditText) findViewById(R.id.order_code);
        tvName = (EditText) findViewById(R.id.order_name);
        tvPrice = (EditText) findViewById(R.id.order_price);
        tvQty = (EditText) findViewById(R.id.order_qty);
        tvUnit = (EditText) findViewById(R.id.order_unit);

        btnAdd.setOnClickListener(this);
        btnProses.setOnClickListener(this);
        btnBatal.setOnClickListener(this);

        Intent intent = getIntent();
        kodeOutlet = intent.getStringExtra("kode_outlet");
        Log.i("Outlet GUID : ", kodeOutlet);

        returs = returRepo.getAll();
        Log.i("Jumlah retur penjualan di database : ", Integer.toString(returs.size()));
    }

    @Override
    public void onClick(View view) {
        Retur retur = new Retur();
        if (view == findViewById(R.id.button_add)) {
            int count = theGrid.getChildCount();
            TableRow tableRow = new TableRow(this);
            tableRow.setId(count + 1);

            TextView labelCode = new TextView(this);
            labelCode.setId(200 + count + 1);
            labelCode.setText(tvCode.getText() + " " + tvName.getText());
            tableRow.addView(labelCode);
            retur.setKode(tvCode.getText().toString());
            retur.setNamaBarang(tvName.getText().toString());

            TextView labelPrice = new TextView(this);
            labelPrice.setId(200 + count + 1);
            labelPrice.setText(tvPrice.getText());
            tableRow.addView(labelPrice);
            retur.setHarga(Integer.valueOf(tvPrice.getText().toString()));

            TextView labelQty = new TextView(this);
            labelQty.setId(200 + count + 1);
            labelQty.setText(tvQty.getText() + "/" + tvUnit.getText());
            tableRow.addView(labelQty);
            retur.setQty(Integer.valueOf(tvQty.getText().toString()));
            retur.setUnit(tvUnit.getText().toString());
            retur.setKodeOutlet(kodeOutlet);

            TextView labelSummary = new TextView(this);
            labelSummary.setId(200 + count + 1);
            Integer summary = Integer.valueOf(tvQty.getText().toString()) * Integer.valueOf(tvPrice.getText().toString());
            labelSummary.setText(summary.toString());
            tableRow.addView(labelSummary);

            theGrid.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            returs.add(retur);
        } else if (view == findViewById(R.id.button_proses)) {
            saveAllRetur();
            finish();

        } else if (view == findViewById(R.id.button_batal)) {
            finish();
        }
    }

    private void saveAllRetur() {
        returRepo.insertAll(returs);
    }
}
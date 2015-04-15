package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by desu sudarsana on 4/13/2015.
 */
public class OrderPenjualanActivity extends Activity implements android.view.View.OnClickListener {

    private Button btnAdd, btnProses, btnBatal;
    private TableLayout theGrid;
    private EditText tvCode, tvName, tvPrice, tvQty, tvUnit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_penjualan);

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
    }

    @Override
    public void onClick(View view) {
        if(view == findViewById(R.id.button_add)){
            int count = theGrid.getChildCount();
            TableRow tableRow = new TableRow(this);
            tableRow.setId(count+1);

            TextView labelCode = new TextView(this);
            labelCode.setId(200+count+1);
            labelCode.setText(tvCode.getText());
            tableRow.addView(labelCode);

            TextView labelName = new TextView(this);
            labelName.setId(200+count+1);
            labelName.setText(tvName.getText());
            tableRow.addView(labelName);

            TextView labelPrice = new TextView(this);
            labelPrice.setId(200+count+1);
            labelPrice.setText(tvPrice.getText());
            tableRow.addView(labelPrice);

            TextView labelQty = new TextView(this);
            labelQty.setId(200+count+1);
            labelQty.setText(tvQty.getText());
            tableRow.addView(labelQty);

            TextView labelUnit = new TextView(this);
            labelUnit.setId(200+count+1);
            labelUnit.setText(tvUnit.getText());
            tableRow.addView(labelUnit);

            theGrid.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        } else if(view == findViewById(R.id.button_proses)) {

        } else if(view == findViewById(R.id.button_batal)) {
            finish();
        }
    }
}

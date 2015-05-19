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

import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.model.Order;
import com.bali.nusadua.productmonitor.repo.OrderRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderPenjualanActivity extends Activity implements android.view.View.OnClickListener {

    private Button btnAdd, btnProses, btnBatal;
    private TableLayout theGrid;
    private EditText tvCode, tvName, tvPrice, tvQty, tvUnit;
    private String customerID;

    private Map<String, Order> mapOrders = new HashMap<String, Order>();
    OrderRepo orderRepo = new OrderRepo(this);


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

        Intent intent = getIntent();
        customerID = intent.getStringExtra(Customer.CUST_ID);
        Log.i("Customer ID : ", customerID);
    }

    @Override
    public void onClick(View view) {
        Order order = new Order();
        int padding_in_dp = 8;  // 6 dps
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);

        if (view == findViewById(R.id.button_add) && mapOrders.get(tvCode.getText().toString()) == null) {
            if (!tvCode.getText().toString().isEmpty() && tvCode.getText().toString().trim() != ""
                    && !tvName.getText().toString().isEmpty() && tvName.getText().toString().trim() != ""
                    && !tvPrice.getText().toString().isEmpty() && tvPrice.getText().toString().trim() != ""
                    && !tvQty.getText().toString().isEmpty() && tvQty.getText().toString().trim() != ""
                    && !tvUnit.getText().toString().isEmpty() && tvUnit.getText().toString().trim() != "") {

                int count = theGrid.getChildCount();
                TableRow tableRow = new TableRow(this);
                tableRow.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
                if (count % 2 == 0) {
                    tableRow.setBackgroundResource(R.drawable.table_row_even_shape);
                } else {
                    tableRow.setBackgroundResource(R.drawable.table_row_odd_shape);
                }

                tableRow.setId(count + 1);

                TextView labelCode = new TextView(this);
                labelCode.setId(200 + count + 1);
                labelCode.setText(tvCode.getText() + " " + tvName.getText());
                labelCode.setTextAppearance(OrderPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                labelCode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 40f));
                tableRow.addView(labelCode);
                order.setKode(tvCode.getText().toString());
                order.setNamaBarang(tvName.getText().toString());

                TextView labelPrice = new TextView(this);
                labelPrice.setId(200 + count + 1);
                labelPrice.setText(tvPrice.getText());
                labelPrice.setTextAppearance(OrderPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                labelCode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 20f));
                tableRow.addView(labelPrice);
                order.setHarga(Integer.valueOf(tvPrice.getText().toString()));

                TextView labelQty = new TextView(this);
                labelQty.setId(200 + count + 1);
                labelQty.setTextAppearance(OrderPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                labelCode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 20f));
                labelQty.setText(tvQty.getText() + "/" + tvUnit.getText());
                tableRow.addView(labelQty);
                order.setQty(Integer.valueOf(tvQty.getText().toString()));
                order.setUnit(tvUnit.getText().toString());
                order.setKodeOutlet(customerID);

                TextView labelSummary = new TextView(this);
                labelSummary.setId(200 + count + 1);
                labelSummary.setTextAppearance(OrderPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                labelCode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 20f));
                Integer summary = Integer.valueOf(tvQty.getText().toString()) * Integer.valueOf(tvPrice.getText().toString());
                labelSummary.setText(summary.toString());
                tableRow.addView(labelSummary);

                //tableRow.setOnLongClickListener();

                theGrid.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                mapOrders.put(order.getKode(), order);

                tvCode.setText(null);

            }
        } else if (view == findViewById(R.id.button_proses)) {
            saveAllOrder();
            finish();

        } else if (view == findViewById(R.id.button_batal)) {
            finish();
        }
    }

    private void saveAllOrder() {
        orderRepo.insertAll(new ArrayList<Order>(mapOrders.values()));
    }
}

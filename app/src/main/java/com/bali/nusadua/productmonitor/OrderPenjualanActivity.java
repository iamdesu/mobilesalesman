package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.model.Order;
import com.bali.nusadua.productmonitor.model.StockBilling;
import com.bali.nusadua.productmonitor.model.StockPrice;
import com.bali.nusadua.productmonitor.repo.OrderRepo;
import com.bali.nusadua.productmonitor.repo.StockBillingRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderPenjualanActivity extends ActionBarActivity implements android.view.View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int VIEW_STOCK_ACTIVITY = 1;

    private Button btnAdd, btnProses, btnBatal;
    private TableLayout theGrid;
    private EditText tvCode, tvName, tvPrice, tvQty;
    private Spinner unitSpinner;
    private String customerID, prevPrice;
    private final Context context = this;
    private int countID;

    private Map<String, Order> mapOrders = new HashMap<String, Order>();
    OrderRepo orderRepo = new OrderRepo(this);
    private StockBillingRepo stockBillingRepo = new StockBillingRepo(this);


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
        unitSpinner = (Spinner) findViewById(R.id.unit_spinner);

        btnAdd.setOnClickListener(this);
        btnProses.setOnClickListener(this);
        btnBatal.setOnClickListener(this);
        unitSpinner.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        customerID = intent.getStringExtra(Customer.CUST_ID);
        Log.i("Customer ID : ", customerID);
        countID = 0;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you medirecords_adminspecify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_show_stock) {
            Intent intent = new Intent(OrderPenjualanActivity.this, ViewStockActivity.class);
            intent.putExtra(Customer.CUST_ID, customerID);
            startActivityForResult(intent, VIEW_STOCK_ACTIVITY);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (VIEW_STOCK_ACTIVITY) : {
                if (resultCode == Activity.RESULT_OK) {
                    // Update your TextView
                    tvCode.setText(data.getStringExtra(StockBilling.SCODE));
                    tvName.setText(data.getStringExtra(StockBilling.DESCRIPTION));
                    tvPrice.setText(data.getStringExtra(StockPrice.PRICE));
                }
                break;
            }
        }
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
                    && !unitSpinner.getSelectedItem().toString().isEmpty()) {

                if (stockBillingRepo.getByStockCode(tvCode.getText().toString()) != null) {
                    countID = countID + 1;
                    int count = countID;
                    TableRow tableRow = new TableRow(this);
                    tableRow.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);

                    tableRow.setBackgroundResource(R.drawable.table_row_even_shape);
                    tableRow.setId(count + 1);

                    TextView labelCode = new TextView(this);
                    labelCode.setId(200 + count + 1);
                    labelCode.setText(tvCode.getText() + " | " + tvName.getText());
                    labelCode.setTextAppearance(OrderPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                    labelCode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 40f));
                    tableRow.addView(labelCode);
                    order.setKode(tvCode.getText().toString());
                    order.setNamaBarang(tvName.getText().toString());

                    TextView labelPrice = new TextView(this);
                    labelPrice.setId(300 + count + 1);
                    String selectedUnit = unitSpinner.getSelectedItem().toString();
                    if (selectedUnit.equals("Pcs")) {
                        Double doubleValue = Double.parseDouble(tvPrice.getText().toString());
                        Double price = doubleValue / 12;
                        price = new BigDecimal(price).setScale(0, RoundingMode.HALF_UP).doubleValue();
                        labelPrice.setText(String.valueOf(price));
                        order.setHarga(price);
                    } else {
                        labelPrice.setText(tvPrice.getText().toString());
                        order.setHarga(Double.valueOf(tvPrice.getText().toString()));
                    }
                    labelPrice.setTextAppearance(OrderPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                    labelPrice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 20f));
                    tableRow.addView(labelPrice);

                    TextView labelQty = new TextView(this);
                    labelQty.setId(400 + count + 1);
                    labelQty.setTextAppearance(OrderPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                    labelQty.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 20f));
                    labelQty.setText(tvQty.getText() + "/" + unitSpinner.getSelectedItem().toString());
                    tableRow.addView(labelQty);
                    order.setQty(Integer.valueOf(tvQty.getText().toString()));
                    order.setUnit(unitSpinner.getSelectedItem().toString());
                    order.setKodeOutlet(customerID);

                    TextView labelSummary = new TextView(this);
                    labelSummary.setId(500 + count + 1);
                    labelSummary.setTextAppearance(OrderPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                    labelSummary.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 20f));
                    Double summary = order.getQty() * order.getHarga();
                    labelSummary.setText(summary.toString());
                    tableRow.addView(labelSummary);

                    tableRow.setOnLongClickListener(
                            new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    final TableRow selectedRow = (TableRow) v;
                                    TextView labelCode = (TextView) findViewById(200 + selectedRow.getId());
                                    final String tvOrderCode = labelCode.getText().toString().split("\\|")[0].trim();
                                    final Order order = mapOrders.get(tvOrderCode);

                                    // custom dialog
                                    final Dialog dialog = new Dialog(context);
                                    dialog.setContentView(R.layout.popup_edit_order);
                                    dialog.setTitle(getResources().getString(R.string.pop_up_order_edit));

                                    // set the custom dialog components - text, image and button
                                    final EditText edOrderCode = (EditText) dialog.findViewById(R.id.popup_order_code);
                                    edOrderCode.setText(order.getKode());
                                    edOrderCode.setEnabled(false);
                                    final EditText edOrderNamaBrg = (EditText) dialog.findViewById(R.id.popup_nama_brg);
                                    edOrderNamaBrg.setText(order.getNamaBarang());
                                    final EditText edOrderPrice = (EditText) dialog.findViewById(R.id.popup_order_price);
                                    edOrderPrice.setText(String.valueOf(order.getHarga()));
                                    final EditText edOrderQty = (EditText) dialog.findViewById(R.id.popup_order_qty);
                                    edOrderQty.setText(String.valueOf(order.getQty()));

                                    Button popupSaveButton = (Button) dialog.findViewById(R.id.popup_btn_save_data);
                                    Button popupDeleteButton = (Button) dialog.findViewById(R.id.popup_btn_delete_data);

                                    // if button is clicked, close the custom dialog
                                    popupSaveButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Order order = mapOrders.get(edOrderCode.getText().toString());
                                            order.setNamaBarang(edOrderNamaBrg.getText().toString());
                                            order.setHarga(Double.parseDouble(edOrderPrice.getText().toString()));
                                            order.setQty(Integer.valueOf(edOrderQty.getText().toString()));

                                            TextView labelCode = (TextView) findViewById(200 + selectedRow.getId());
                                            labelCode.setText(order.getKode() + " | " + order.getNamaBarang());
                                            TextView labelPrice = (TextView) findViewById(300 + selectedRow.getId());
                                            labelPrice.setText(order.getHarga().toString());
                                            TextView labelQty = (TextView) findViewById(400 + selectedRow.getId());
                                            labelQty.setText(String.valueOf(order.getQty()) + "/" + order.getUnit());
                                            TextView labelSummary = (TextView) findViewById(500 + selectedRow.getId());
                                            Double summary = order.getQty() * order.getHarga();
                                            labelSummary.setText(summary.toString());

                                            dialog.dismiss();
                                        }
                                    });

                                    popupDeleteButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mapOrders.remove(edOrderCode.getText().toString());
                                            theGrid.removeView(selectedRow);
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();
                                    return true;
                                }
                            }
                    );

                    theGrid.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    mapOrders.put(order.getKode(), order);

                    tvCode.setText(null);
                    tvName.setText(null);
                    tvPrice.setText(null);
                    tvQty.setText(null);
                    unitSpinner.setSelection(0);

                } else {
                    showToast(getResources().getString(R.string.code_not_found));
                }

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

    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }
}

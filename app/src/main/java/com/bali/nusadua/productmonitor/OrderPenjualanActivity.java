package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.model.OrderItem;
import com.bali.nusadua.productmonitor.model.StockBilling;
import com.bali.nusadua.productmonitor.model.StockPrice;
import com.bali.nusadua.productmonitor.repo.OrderRepo;
import com.bali.nusadua.productmonitor.repo.StockBillingRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderPenjualanActivity extends ActionBarActivity implements android.view.View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int VIEW_STOCK_ACTIVITY = 1;
    private static final int VIEW_EDIT_STOCK_ACTIVITY = 2;

    private final Context context = this;
    private Button btnAdd, btnProses;
    private TableLayout theGrid;
    private EditText tvStockID, tvName, tvPrice, tvQty;
    private TextView tvTotal;
    private Spinner unitSpinner;
    private String customerID, prevPrice;
    private int countID;
    private Double total = 0d;

    private Map<String, OrderItem> mapOrders = new HashMap<String, OrderItem>();
    private Map<String, Integer> mapCheckBoxs = new HashMap<String, Integer>();
    private OrderRepo orderRepo = new OrderRepo(this);
    private StockBillingRepo stockBillingRepo = new StockBillingRepo(this);
    private NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);
    private Menu actionBarMenu;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_penjualan);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Declare UI component
        btnAdd = (Button) findViewById(R.id.button_add);
        btnProses = (Button) findViewById(R.id.button_proses);
        theGrid = (TableLayout) findViewById(R.id.tableLayoutData);
        tvStockID = (EditText) findViewById(R.id.order_code);
        tvName = (EditText) findViewById(R.id.order_name);
        tvPrice = (EditText) findViewById(R.id.order_price);
        tvQty = (EditText) findViewById(R.id.order_qty);
        unitSpinner = (Spinner) findViewById(R.id.unit_spinner);
        tvTotal = (TextView) findViewById(R.id.text_total);

        btnAdd.setOnClickListener(this);
        btnProses.setOnClickListener(this);
        unitSpinner.setOnItemSelectedListener(this);
        tvTotal.setText("Rp 0");

        Intent intent = getIntent();
        customerID = intent.getStringExtra(Customer.CUST_ID);
        Log.i("Customer ID : ", customerID);
        countID = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.actionBarMenu = menu;
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you medirecords_adminspecify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();

                break;

            case R.id.action_show_stock:
                intent = new Intent(OrderPenjualanActivity.this, ViewStockActivity.class);
                intent.putExtra(Customer.CUST_ID, customerID);
                startActivityForResult(intent, VIEW_STOCK_ACTIVITY);

                break;

            case R.id.action_delete_stock:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Do your Yes progress
                                for (Integer i : mapCheckBoxs.values()) {
                                    mapOrders.remove(String.valueOf(i));
                                    TableRow selectedRow = (TableRow) findViewById(i);
                                    theGrid.removeView(selectedRow);
                                }
                                calculateTotal(new ArrayList<OrderItem>(mapOrders.values()));
                                showToast(mapCheckBoxs.values().size() + " telah di batalkan.");
                                mapCheckBoxs.clear();
                                actionBarMenu.setGroupVisible(R.id.menu_group_edit, false);
                                actionBarMenu.setGroupVisible(R.id.menu_group_delete, false);

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //Do your No progress
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(this);
                ab.setMessage(getResources().getString(R.string.delete_stock_confirm)).setPositiveButton(getResources().getString(R.string.action_yes), dialogClickListener)
                        .setNegativeButton(getResources().getString(R.string.action_no), dialogClickListener).show();

                break;

            case R.id.action_edit_stock:
                Integer i = new ArrayList<Integer>(mapCheckBoxs.values()).get(0);
                OrderItem order = mapOrders.get(String.valueOf(i));
                intent = new Intent(OrderPenjualanActivity.this, EditOrderActivity.class);
                intent.putExtra(OrderItem.TABLE, order);
                startActivityForResult(intent, VIEW_EDIT_STOCK_ACTIVITY);
                break;
        }

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (VIEW_STOCK_ACTIVITY): {
                if (resultCode == Activity.RESULT_OK) {
                    // Update your TextView
                    tvStockID.setText(data.getStringExtra(StockBilling.STOCK_ID));
                    tvName.setText(data.getStringExtra(StockBilling.DESCRIPTION));
                    tvPrice.setText(data.getStringExtra(StockPrice.PRICE));
                }
                break;
            }
            case (VIEW_EDIT_STOCK_ACTIVITY): {
                if (resultCode == Activity.RESULT_OK) {
                    OrderItem resultOrder = (OrderItem) data.getSerializableExtra(OrderItem.TABLE);
                    OrderItem order = mapOrders.get(String.valueOf(resultOrder.getId()));
                    order.setNamaBarang(resultOrder.getNamaBarang());
                    order.setHarga(resultOrder.getHarga());
                    order.setQty(resultOrder.getQty());
                    order.setUnit(resultOrder.getUnit());

                    TableRow selectedRow = (TableRow) findViewById(resultOrder.getId());
                    TextView textCodeAndName = (TextView) selectedRow.getChildAt(1);
                    textCodeAndName.setText(order.getKode() + " | " + order.getNamaBarang());
                    TextView textPrice = (TextView) selectedRow.getChildAt(2);
                    textPrice.setText(format.format(order.getHarga()).toString());
                    TextView textQtyUnit = (TextView) selectedRow.getChildAt(3);
                    textQtyUnit.setText(order.getQty() + "/" + order.getUnit());
                    TextView textSummary = (TextView) selectedRow.getChildAt(4);
                    textSummary.setText(format.format(order.getQty() * order.getHarga()).toString());

                    calculateTotal(new ArrayList<OrderItem>(mapOrders.values()));
                }
                break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        OrderItem order = new OrderItem();
        int padding_in_dp = 1;  // 8 = 6 dps
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);

        if (view == findViewById(R.id.button_add)) {
            if (!tvStockID.getText().toString().isEmpty() && tvStockID.getText().toString().trim() != ""
                    && !tvName.getText().toString().isEmpty() && tvName.getText().toString().trim() != ""
                    && !tvPrice.getText().toString().isEmpty() && tvPrice.getText().toString().trim() != ""
                    && !tvQty.getText().toString().isEmpty() && tvQty.getText().toString().trim() != "") {

                if (stockBillingRepo.getByStockId(tvStockID.getText().toString()) != null) {
                    countID = countID + 1;
                    int count = countID;
                    final TableRow tableRow = new TableRow(this);
                    tableRow.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0f));
                    tableRow.setId(count + 1);
                    order.setId(tableRow.getId());

                    CheckBox box = new CheckBox(this);
                    box.setBackgroundColor(getResources().getColor(android.R.color.white));
                    TableRow.LayoutParams layoutCheckBox = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 0f);
                    layoutCheckBox.setMargins(1, 0, 1, 0);
                    box.setLayoutParams(layoutCheckBox);
                    box.setOnCheckedChangeListener(
                            new CompoundButton.OnCheckedChangeListener() {

                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if (b) {
                                        mapCheckBoxs.put(String.valueOf(tableRow.getId()), tableRow.getId());
                                        if (mapCheckBoxs.values().size() == 1) {
                                            actionBarMenu.setGroupVisible(R.id.menu_group_edit, true);
                                        } else {
                                            actionBarMenu.setGroupVisible(R.id.menu_group_edit, false);
                                        }
                                        actionBarMenu.setGroupVisible(R.id.menu_group_delete, true);
                                    } else {
                                        mapCheckBoxs.remove(String.valueOf(tableRow.getId()));
                                        if (mapCheckBoxs.values().size() == 1) {
                                            actionBarMenu.setGroupVisible(R.id.menu_group_edit, true);
                                        } else {
                                            actionBarMenu.setGroupVisible(R.id.menu_group_edit, false);
                                        }

                                        if (mapCheckBoxs.values().size() == 0) {
                                            actionBarMenu.setGroupVisible(R.id.menu_group_delete, false);
                                        }
                                    }
                                }
                            }
                    );

                    tableRow.addView(box);

                    TextView labelCode = new TextView(this);
                    labelCode.setId(200 + count + 1);
                    labelCode.setText(tvStockID.getText() + " | " + tvName.getText());
                    labelCode.setTextAppearance(OrderPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                    TableRow.LayoutParams layoutCode = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 40f);
                    layoutCode.setMargins(1, 0, 1, 0);
                    labelCode.setLayoutParams(layoutCode);
                    labelCode.setBackgroundColor(getResources().getColor(android.R.color.white));
                    labelCode.setMaxEms(7);
                    tableRow.addView(labelCode);
                    order.setKode(tvStockID.getText().toString());
                    order.setNamaBarang(tvName.getText().toString());

                    TextView labelPrice = new TextView(this);
                    labelPrice.setId(300 + count + 1);
                    String selectedUnit = unitSpinner.getSelectedItem().toString();
                    Double doubleValue = Double.parseDouble(tvPrice.getText().toString());
                    if (selectedUnit.equals("Pcs")) {
                        Double price = doubleValue / 12;
                        price = new BigDecimal(price).setScale(0, RoundingMode.HALF_UP).doubleValue();
                        labelPrice.setText(format.format(price).toString());
                        order.setHarga(price);
                    } else {
                        labelPrice.setText(format.format(doubleValue).toString());
                        order.setHarga(Double.valueOf(tvPrice.getText().toString()));
                    }

                    labelPrice.setTextAppearance(OrderPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                    TableRow.LayoutParams layoutPrice = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 20f);
                    layoutPrice.setMargins(1, 0, 1, 0);
                    labelPrice.setLayoutParams(layoutPrice);
                    labelPrice.setGravity(Gravity.END);
                    labelPrice.setBackgroundColor(getResources().getColor(android.R.color.white));
                    labelPrice.setMaxEms(3);
                    tableRow.addView(labelPrice);

                    //Qty
                    TextView labelQty = new TextView(this);
                    labelQty.setId(400 + count + 1);
                    labelQty.setTextAppearance(OrderPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                    TableRow.LayoutParams layoutQty = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 20f);
                    layoutQty.setMargins(1, 0, 1, 0);
                    labelQty.setLayoutParams(layoutQty);
                    labelQty.setGravity(Gravity.END);
                    labelQty.setBackgroundColor(getResources().getColor(android.R.color.white));
                    labelQty.setText(tvQty.getText() + "/" + unitSpinner.getSelectedItem().toString());
                    labelQty.setMaxEms(3);
                    tableRow.addView(labelQty);
                    order.setQty(Integer.valueOf(tvQty.getText().toString()));
                    order.setUnit(unitSpinner.getSelectedItem().toString());
                    order.setKodeOutlet(customerID);

                    //Summary
                    TextView labelSummary = new TextView(this);
                    labelSummary.setId(500 + count + 1);
                    labelSummary.setTextAppearance(OrderPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                    TableRow.LayoutParams layoutSummary = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 20f);
                    layoutSummary.setMargins(1, 0, 1, 0);
                    labelSummary.setLayoutParams(layoutSummary);
                    labelSummary.setGravity(Gravity.END);
                    labelSummary.setBackgroundColor(getResources().getColor(android.R.color.white));
                    labelSummary.setMaxEms(3);
                    Double summary = order.getQty() * order.getHarga();
                    labelSummary.setText(format.format(summary).toString());
                    tableRow.addView(labelSummary);

                    /*tableRow.setOnLongClickListener(
                            new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    final TableRow selectedRow = (TableRow) v;
                                    TextView labelCode = (TextView) findViewById(200 + selectedRow.getId());
                                    final String tvOrderCode = labelCode.getText().toString().split("\\|")[0].trim();
                                    final OrderItem order = mapOrders.get(tvOrderCode);

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
                                            OrderItem order = mapOrders.get(edOrderCode.getText().toString());
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
                                            OrderItem removeorder = mapOrders.get(edOrderCode.getText().toString());
                                            subTotal(removeorder);
                                            mapOrders.remove(edOrderCode.getText().toString());
                                            theGrid.removeView(selectedRow);
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();
                                    return true;
                                }
                            }
                    );*/

                    theGrid.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    mapOrders.put(String.valueOf(tableRow.getId()), order);
                    addTotal(order);

                    tvStockID.setText(null);
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

        }
    }

    private void saveAllOrder() {
        orderRepo.insertAll(new ArrayList<OrderItem>(mapOrders.values()));
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }

    private void addTotal(OrderItem order) {
        total = total + (order.getQty() * order.getHarga());
        tvTotal.setText(getResources().getString(R.string.currency_symbol) + " " + format.format(total).toString());
    }

    private void subTotal(OrderItem order) {
        total = total - (order.getQty() * order.getHarga());
        tvTotal.setText(getResources().getString(R.string.currency_symbol) + " " + format.format(total).toString());
    }

    private void calculateTotal(List<OrderItem> orders) {
        total = 0d;
        for (OrderItem order : orders) {
            total = total + (order.getQty() * order.getHarga());
        }
        tvTotal.setText(getResources().getString(R.string.currency_symbol) + " " + format.format(total).toString());
    }
}

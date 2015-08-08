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
import com.bali.nusadua.productmonitor.model.Retur;
import com.bali.nusadua.productmonitor.model.StockBilling;
import com.bali.nusadua.productmonitor.model.StockPrice;
import com.bali.nusadua.productmonitor.repo.ReturRepo;
import com.bali.nusadua.productmonitor.repo.StockBillingRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReturPenjualanActivity extends ActionBarActivity implements android.view.View.OnClickListener {

    private static final int VIEW_STOCK_ACTIVITY = 1;
    private static final int VIEW_EDIT_STOCK_ACTIVITY = 2;

    private final Context context = this;
    private Button btnAdd, btnProses, btnBatal;
    private TableLayout theGrid;
    private EditText tvStockID, tvName, tvPrice, tvQty;
    private TextView tvTotal;
    private Spinner unitSpinner;
    private String customerID;
    private int countID;
    private Double total = 0d;

    private Map<String, Retur> mapReturs = new HashMap<String, Retur>();
    private Map<String, Integer> mapCheckBoxs = new HashMap<String, Integer>();
    private ReturRepo returRepo = new ReturRepo(this);
    private StockBillingRepo stockBillingRepo = new StockBillingRepo(this);
    private NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);
    private Menu actionBarMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retur_penjualan);
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
        //unitSpinner.setOnItemSelectedListener(this);
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
                intent = new Intent(ReturPenjualanActivity.this, ViewStockActivity.class);
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
                                    mapReturs.remove(String.valueOf(i));
                                    TableRow selectedRow = (TableRow) findViewById(i);
                                    theGrid.removeView(selectedRow);
                                }
                                calculateTotal(new ArrayList<Retur>(mapReturs.values()));
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
                Retur retur = mapReturs.get(String.valueOf(i));
                intent = new Intent(ReturPenjualanActivity.this, EditReturActivity.class);
                intent.putExtra(Retur.TABLE, retur);
                startActivityForResult(intent, VIEW_EDIT_STOCK_ACTIVITY);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (VIEW_STOCK_ACTIVITY): {
                if (resultCode == Activity.RESULT_OK) {
                    //Update your TextView
                    tvStockID.setText(data.getStringExtra(StockBilling.STOCK_ID));
                    tvName.setText(data.getStringExtra(StockBilling.DESCRIPTION));
                    tvPrice.setText(data.getStringExtra(StockPrice.PRICE));
                }
                break;
            }

            case (VIEW_EDIT_STOCK_ACTIVITY): {
                if (resultCode == Activity.RESULT_OK) {
                    Retur resultRetur = (Retur) data.getSerializableExtra(Retur.TABLE);
                    Retur retur = mapReturs.get(String.valueOf(resultRetur.getId()));
                    retur.setNamaBarang(resultRetur.getNamaBarang());
                    retur.setHarga(resultRetur.getHarga());
                    retur.setQty(resultRetur.getQty());
                    retur.setUnit(resultRetur.getUnit());

                    TableRow selectedRow = (TableRow) findViewById(resultRetur.getId());
                    TextView textCodeAndName = (TextView) selectedRow.getChildAt(1);
                    textCodeAndName.setText(retur.getKode() + " | " + retur.getNamaBarang());
                    TextView textPrice = (TextView) selectedRow.getChildAt(2);
                    textPrice.setText(format.format(retur.getHarga()).toString());
                    TextView textQtyUnit = (TextView) selectedRow.getChildAt(3);
                    textQtyUnit.setText(retur.getQty() + "/" + retur.getUnit());
                    TextView textSummary = (TextView) selectedRow.getChildAt(4);
                    textSummary.setText(format.format(retur.getQty() * retur.getHarga()).toString());

                    calculateTotal(new ArrayList<Retur>(mapReturs.values()));
                }
                break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        Retur retur = new Retur();
        int padding_in_dp = 1;  // 6 dps
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
                    retur.setId(tableRow.getId());

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
                    labelCode.setTextAppearance(ReturPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                    TableRow.LayoutParams layoutCode = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 40f);
                    layoutCode.setMargins(1, 0, 1, 0);
                    labelCode.setLayoutParams(layoutCode);
                    labelCode.setBackgroundColor(getResources().getColor(android.R.color.white));
                    labelCode.setMaxEms(7);
                    tableRow.addView(labelCode);
                    retur.setKode(tvStockID.getText().toString());
                    retur.setNamaBarang(tvName.getText().toString());

                    TextView labelPrice = new TextView(this);
                    labelPrice.setId(300 + count + 1);
                    String selectedUnit = unitSpinner.getSelectedItem().toString();
                    Double doubleValue = Double.parseDouble(tvPrice.getText().toString());
                    if (selectedUnit.equals("Pcs")) {
                        Double price = doubleValue / 12;
                        price = new BigDecimal(price).setScale(0, RoundingMode.HALF_UP).doubleValue();
                        labelPrice.setText(String.valueOf(price));
                        retur.setHarga(price);
                    } else {
                        labelPrice.setText(format.format(doubleValue).toString());
                        retur.setHarga(Double.valueOf(tvPrice.getText().toString()));
                    }
                    labelPrice.setTextAppearance(ReturPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                    labelPrice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 20f));
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
                    labelQty.setText(tvQty.getText() + "/" + unitSpinner.getSelectedItem().toString());
                    labelQty.setTextAppearance(ReturPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                    TableRow.LayoutParams layoutQty = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 20f);
                    layoutQty.setMargins(1, 0, 1, 0);
                    labelQty.setLayoutParams(layoutQty);
                    labelQty.setGravity(Gravity.END);
                    labelQty.setBackgroundColor(getResources().getColor(android.R.color.white));
                    labelQty.setMaxEms(3);
                    tableRow.addView(labelQty);
                    retur.setQty(Integer.valueOf(tvQty.getText().toString()));
                    retur.setUnit(unitSpinner.getSelectedItem().toString());
                    retur.setKodeOutlet(customerID);

                    //Summary
                    TextView labelSummary = new TextView(this);
                    labelSummary.setId(500 + count + 1);
                    labelSummary.setTextAppearance(ReturPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                    TableRow.LayoutParams layoutSummary = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 20f);
                    layoutSummary.setMargins(1, 0, 1, 0);
                    labelSummary.setLayoutParams(layoutSummary);
                    labelSummary.setGravity(Gravity.END);
                    labelSummary.setBackgroundColor(getResources().getColor(android.R.color.white));
                    labelSummary.setMaxEms(3);
                    Double summary = retur.getQty() * retur.getHarga();
                    labelSummary.setText(summary.toString());
                    tableRow.addView(labelSummary);


                    theGrid.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    mapReturs.put(String.valueOf(tableRow.getId()), retur);
                    addTotal(retur);

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
            saveAllRetur();
            finish();

        }
    }

    private void saveAllRetur() {
        List<Retur> returs = new ArrayList<Retur>(mapReturs.values());
        returRepo.insertAll(returs);
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }

    private void addTotal(Retur retur) {
        total = total + (retur.getQty() * retur.getHarga());
        tvTotal.setText(getResources().getString(R.string.currency_symbol) + " " + format.format(total).toString());
    }

    private void subTotal(Retur retur) {
        total = total - (retur.getQty() * retur.getHarga());
        tvTotal.setText(getResources().getString(R.string.currency_symbol) + " " + format.format(total).toString());
    }

    private void calculateTotal(List<Retur> returs) {
        total = 0d;
        for (Retur retur : returs) {
            total = total + (retur.getQty() * retur.getHarga());
        }
        tvTotal.setText(getResources().getString(R.string.currency_symbol) + " " + format.format(total).toString());
    }
}

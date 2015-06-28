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
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturPenjualanActivity extends ActionBarActivity implements android.view.View.OnClickListener {

    private static final int VIEW_STOCK_ACTIVITY = 1;

    private Button btnAdd, btnProses, btnBatal;
    private TableLayout theGrid;
    private EditText tvCode, tvName, tvPrice, tvQty;
    private Spinner unitSpinner;
    private String customerID;
    private final Context context = this;
    private int countID;

    private Map<String, Retur> mapReturs = new HashMap<String, Retur>();
    private ReturRepo returRepo = new ReturRepo(this);
    private StockBillingRepo stockBillingRepo = new StockBillingRepo(this);

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
        unitSpinner = (Spinner) findViewById(R.id.unit_spinner);

        btnAdd.setOnClickListener(this);
        btnProses.setOnClickListener(this);
        btnBatal.setOnClickListener(this);

        Intent intent = getIntent();
        customerID = intent.getStringExtra(Customer.CUST_ID);
        Log.i("Customer ID : ", customerID);
        countID = 0;
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
            Intent intent = new Intent(ReturPenjualanActivity.this, ViewStockActivity.class);
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
                    //Update your TextView
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
        Retur retur = new Retur();
        int padding_in_dp = 8;  // 6 dps
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);

        if (view == findViewById(R.id.button_add) && mapReturs.get(tvCode.getText().toString()) == null) {
            if (!tvCode.getText().toString().isEmpty() && tvCode.getText().toString().trim() != ""
                    && !tvName.getText().toString().isEmpty() && tvName.getText().toString().trim() != ""
                    && !tvPrice.getText().toString().isEmpty() && tvPrice.getText().toString().trim() != ""
                    && !tvQty.getText().toString().isEmpty() && tvQty.getText().toString().trim() != "") {

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
                    labelCode.setTextAppearance(ReturPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                    labelCode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 40f));
                    tableRow.addView(labelCode);
                    retur.setKode(tvCode.getText().toString());
                    retur.setNamaBarang(tvName.getText().toString());

                    TextView labelPrice = new TextView(this);
                    labelPrice.setId(300 + count + 1);
                    String selectedUnit = unitSpinner.getSelectedItem().toString();
                    if (selectedUnit.equals("Pcs")) {
                        Double doubleValue = Double.parseDouble(tvPrice.getText().toString());
                        Double price = doubleValue / 12;
                        price = new BigDecimal(price).setScale(0, RoundingMode.HALF_UP).doubleValue();
                        labelPrice.setText(String.valueOf(price));
                        retur.setHarga(price);
                    } else {
                        labelPrice.setText(tvPrice.getText().toString());
                        retur.setHarga(Double.valueOf(tvPrice.getText().toString()));
                    }
                    labelPrice.setTextAppearance(ReturPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                    labelPrice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 20f));
                    tableRow.addView(labelPrice);
                    retur.setHarga(Double.valueOf(tvPrice.getText().toString()));

                    TextView labelQty = new TextView(this);
                    labelQty.setId(400 + count + 1);
                    labelQty.setText(tvQty.getText() + "/" + unitSpinner.getSelectedItem().toString());
                    labelQty.setTextAppearance(ReturPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                    labelQty.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 20f));
                    tableRow.addView(labelQty);
                    retur.setQty(Integer.valueOf(tvQty.getText().toString()));
                    retur.setUnit(unitSpinner.getSelectedItem().toString());
                    retur.setKodeOutlet(customerID);

                    TextView labelSummary = new TextView(this);
                    labelSummary.setId(500 + count + 1);
                    labelSummary.setTextAppearance(ReturPenjualanActivity.this, android.R.style.TextAppearance_Medium);
                    labelSummary.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 20f));
                    Double summary = Integer.valueOf(tvQty.getText().toString()) * Double.valueOf(tvPrice.getText().toString());
                    labelSummary.setText(summary.toString());
                    tableRow.addView(labelSummary);

                    tableRow.setOnLongClickListener(
                        new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                final TableRow selectedRow = (TableRow) v;
                                TextView labelCode = (TextView) findViewById(200 + selectedRow.getId());
                                final String tvReturCode = labelCode.getText().toString().split("\\|")[0].trim();
                                final Retur retur = mapReturs.get(tvReturCode);

                                // custom dialog
                                final Dialog dialog = new Dialog(context);
                                dialog.setContentView(R.layout.popup_edit_retur);
                                dialog.setTitle(getResources().getString(R.string.pop_up_retur_edit));

                                // set the custom dialog components - text, image and button
                                final EditText edReturCode = (EditText) dialog.findViewById(R.id.popup_retur_code);
                                edReturCode.setText(retur.getKode());
                                edReturCode.setEnabled(false);
                                final EditText edReturNamaBrg = (EditText) dialog.findViewById(R.id.popup_retur_nama_brg);
                                edReturNamaBrg.setText(retur.getNamaBarang());
                                final EditText edReturPrice = (EditText) dialog.findViewById(R.id.popup_retur_price);
                                edReturPrice.setText(String.valueOf(retur.getHarga()));
                                final EditText edReturQty = (EditText) dialog.findViewById(R.id.popup_retur_qty);
                                edReturQty.setText(String.valueOf(retur.getQty()));

                                Button popupSaveButton = (Button) dialog.findViewById(R.id.popup_btn_save_data_retur);
                                Button popupDeleteButton = (Button) dialog.findViewById(R.id.popup_btn_delete_data_retur);

                                // if button is clicked, close the custom dialog
                                popupSaveButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Retur retur = mapReturs.get(edReturCode.getText().toString());
                                        retur.setNamaBarang(edReturNamaBrg.getText().toString());
                                        retur.setHarga(Double.valueOf(edReturPrice.getText().toString()));
                                        retur.setQty(Integer.valueOf(edReturQty.getText().toString()));

                                        TextView labelCode = (TextView) findViewById(200 + selectedRow.getId());
                                        labelCode.setText(retur.getKode() + " | " + retur.getNamaBarang());
                                        TextView labelPrice = (TextView) findViewById(300 + selectedRow.getId());
                                        labelPrice.setText(retur.getHarga().toString());
                                        TextView labelQty = (TextView) findViewById(400 + selectedRow.getId());
                                        labelQty.setText(String.valueOf(retur.getQty()) + "/" + retur.getUnit());
                                        TextView labelSummary = (TextView) findViewById(500 + selectedRow.getId());
                                        Double summary = retur.getQty() * retur.getHarga();
                                        labelSummary.setText(summary.toString());

                                        dialog.dismiss();
                                    }
                                });

                                popupDeleteButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mapReturs.remove(edReturCode.getText().toString());
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
                    mapReturs.put(retur.getKode(), retur);

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
            saveAllRetur();
            finish();

        } else if (view == findViewById(R.id.button_batal)) {
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
}

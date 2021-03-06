package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bali.nusadua.productmonitor.businessObject.OrderBO;
import com.bali.nusadua.productmonitor.businessObject.SettlementBO;
import com.bali.nusadua.productmonitor.model.Billing;
import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.model.OrderItem;
import com.bali.nusadua.productmonitor.model.SettlementItem;
import com.bali.nusadua.productmonitor.repo.BillingRepo;
import com.bali.nusadua.productmonitor.repo.SettlementItemRepo;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SettlementActivity extends ActionBarActivity implements View.OnClickListener {

    static final int DATE_DIALOG_ID = 999;
    static final int DATE_POPUP_DIALOG_ID = 998;
    private static final int VIEW_BILLING_ACTIVITY = 1;
    private static final int VIEW_EDIT_SETTLEMENT_ACTIVITY = 2;

    private final Context context = this;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private Button btnAdd, btnProses;
    private TableLayout theGrid;
    private EditText etInvoiceNumber, etInvoiceDate, etCredit, etPaymentMethod, etNominalPayment;
    private TextView tvTotal;
    private DatePicker datePickerField, datePickerPopupField;
    private String customerID;
    private int initYear, initMonth, initDay, initPopupYear, initPopupMonth, initPopupDay;
    private DatePickerDialog.OnDateSetListener datePickerListener, datePickerPopupListener;
    private int countID;
    private Double total = 0d;

    private Map<String, SettlementItem> mapSettlements = new HashMap<String, SettlementItem>();
    private Map<String, Integer> mapCheckBoxs = new HashMap<String, Integer>();
    private SettlementItemRepo settlementItemRepo = new SettlementItemRepo(this);
    private SettlementBO settlementBO = new SettlementBO(this);
    private BillingRepo billingRepo = new BillingRepo(this);
    private NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);
    private Menu actionBarMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Declare UI component
        btnAdd = (Button) findViewById(R.id.button_add);
        btnProses = (Button) findViewById(R.id.button_proses);
        theGrid = (TableLayout) findViewById(R.id.tableLayoutData);
        etInvoiceNumber = (EditText) findViewById(R.id.invoice_number);
        etInvoiceDate = (EditText) findViewById(R.id.invoice_date);
        etCredit = (EditText) findViewById(R.id.settlement_credit);
        etPaymentMethod = (EditText) findViewById(R.id.payment_method);
        etNominalPayment = (EditText) findViewById(R.id.nominal_payment);
        tvTotal = (TextView) findViewById(R.id.text_total);
        setCurrentDateOnDatePicker();

        //Declare Listener
        btnAdd.setOnClickListener(this);
        btnProses.setOnClickListener(this);
        tvTotal.setText("Rp 0");
        etInvoiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        datePickerListener = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {

                initYear = selectedYear;
                initMonth = selectedMonth;
                initDay = selectedDay;

                // set selected date into textview
                etInvoiceDate.setText(new StringBuilder()
                        .append(initDay < 10 ? "0" + initDay : initDay)
                        .append("-")
                        .append(initMonth + 1 < 10 ? "0" + (initMonth + 1) : (initMonth + 1))
                        .append("-")
                        .append(initYear)
                        .append(" "));

                // set selected date into datepicker also
                datePickerField.init(initYear, initMonth, initDay, null);

            }
        };

        Intent intent = getIntent();
        customerID = intent.getStringExtra(Customer.CUST_ID);
        Log.i("Customer ID : ", customerID);
        countID = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.actionBarMenu = menu;
        getMenuInflater().inflate(R.menu.menu_settlement, menu);
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

            case R.id.action_show_billing:
                intent = new Intent(SettlementActivity.this, ViewBillingActivity.class);
                intent.putExtra(Customer.CUST_ID, customerID);
                startActivityForResult(intent, VIEW_BILLING_ACTIVITY);

                break;

            case R.id.action_delete_settlement:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Do your Yes progress
                                for (Integer i : mapCheckBoxs.values()) {
                                    mapSettlements.remove(String.valueOf(i));
                                    TableRow selectedRow = (TableRow) findViewById(i);
                                    theGrid.removeView(selectedRow);
                                }
                                calculateTotal(new ArrayList<SettlementItem>(mapSettlements.values()));
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
                ab.setMessage(getResources().getString(R.string.delete_settlement_confirm)).setPositiveButton(getResources().getString(R.string.action_yes), dialogClickListener)
                        .setNegativeButton(getResources().getString(R.string.action_no), dialogClickListener).show();

                break;

            case R.id.action_edit_settlement:
                Integer i = new ArrayList<Integer>(mapCheckBoxs.values()).get(0);
                SettlementItem settlementItem = mapSettlements.get(String.valueOf(i));
                intent = new Intent(SettlementActivity.this, EditSettlementActivity.class);
                intent.putExtra(SettlementItem.TABLE, settlementItem);
                startActivityForResult(intent, VIEW_EDIT_SETTLEMENT_ACTIVITY);
                break;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (VIEW_BILLING_ACTIVITY): {
                if (resultCode == Activity.RESULT_OK) {
                    //Update your TextView
                    etInvoiceNumber.setText(data.getStringExtra(Billing.INVOICE_NO));
                    etInvoiceDate.setText(sdf.format(new Date()));
                    etCredit.setText(data.getStringExtra(Billing.TOTAL_AMOUNT));
                }
                break;
            }

            case (VIEW_EDIT_SETTLEMENT_ACTIVITY): {
                if (resultCode == Activity.RESULT_OK) {
                    SettlementItem resultSettlementItem = (SettlementItem) data.getSerializableExtra(SettlementItem.TABLE);
                    SettlementItem settlementItem = mapSettlements.get(String.valueOf(resultSettlementItem.getId()));
                    settlementItem.setInvoiceNumber(resultSettlementItem.getInvoiceNumber());
                    settlementItem.setInvoiceDate(resultSettlementItem.getInvoiceDate());
                    settlementItem.setCredit(resultSettlementItem.getCredit());
                    settlementItem.setPaymentMethod(resultSettlementItem.getPaymentMethod());
                    settlementItem.setNominalPayment(resultSettlementItem.getNominalPayment());

                    TableRow selectedRow = (TableRow) findViewById(resultSettlementItem.getId());
                    TextView textInvNumber = (TextView) selectedRow.getChildAt(1);
                    textInvNumber.setText(settlementItem.getInvoiceNumber());
                    TextView textCredit = (TextView) selectedRow.getChildAt(2);
                    textCredit.setText(settlementItem.getCredit().toString());
                    TextView textPayMethod = (TextView) selectedRow.getChildAt(3);
                    textPayMethod.setText(settlementItem.getPaymentMethod());
                    TextView textNominal = (TextView) selectedRow.getChildAt(4);
                    textNominal.setText(format.format(settlementItem.getNominalPayment()).toString());

                    calculateTotal(new ArrayList<SettlementItem>(mapSettlements.values()));
                }
                break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        SettlementItem settlementItem = new SettlementItem();
        int padding_in_dp = 1;  // 8 = 6 dps
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);

        if (view == findViewById(R.id.button_add)) {
            if (!etInvoiceNumber.getText().toString().isEmpty() && etInvoiceNumber.getText().toString().trim() != ""
                    && !etCredit.getText().toString().isEmpty() && etCredit.getText().toString().trim() != ""
                    && !etPaymentMethod.getText().toString().isEmpty() && etPaymentMethod.getText().toString().trim() != ""
                    && !etNominalPayment.getText().toString().isEmpty() && etNominalPayment.getText().toString().trim() != "") {

                if (billingRepo.getBillingByCustoMerInvoiceNo(customerID, etInvoiceNumber.getText().toString()) != null) {
                    countID = countID + 1;
                    int count = countID;
                    final TableRow tableRow = new TableRow(this);
                    tableRow.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0f));
                    tableRow.setId(count + 1);
                    settlementItem.setId(tableRow.getId());

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
                    labelCode.setText(etInvoiceNumber.getText() + " | " + etInvoiceDate.getText());
                    labelCode.setTextAppearance(SettlementActivity.this, android.R.style.TextAppearance_Medium);
                    TableRow.LayoutParams layoutCode = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 30f);
                    layoutCode.setMargins(1, 0, 1, 0);
                    labelCode.setLayoutParams(layoutCode);
                    labelCode.setBackgroundColor(getResources().getColor(android.R.color.white));
                    labelCode.setMaxEms(5);
                    tableRow.addView(labelCode);
                    settlementItem.setInvoiceNumber(etInvoiceNumber.getText().toString());
                    try {
                        settlementItem.setInvoiceDate(sdf.parse(etInvoiceDate.getText().toString()));
                    } catch (ParseException e) {
                        settlementItem.setInvoiceDate(null);
                    }


                    TextView labelPrice = new TextView(this);
                    labelPrice.setId(300 + count + 1);
                    labelPrice.setText(etCredit.getText());
                    labelPrice.setTextAppearance(SettlementActivity.this, android.R.style.TextAppearance_Medium);
                    TableRow.LayoutParams layoutPrice = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 20f);
                    layoutPrice.setMargins(1, 0, 1, 0);
                    labelPrice.setLayoutParams(layoutPrice);
                    labelPrice.setGravity(Gravity.END);
                    labelPrice.setBackgroundColor(getResources().getColor(android.R.color.white));
                    labelPrice.setMaxEms(3);
                    tableRow.addView(labelPrice);
                    settlementItem.setCredit(Long.valueOf(etCredit.getText().toString()));

                    TextView labelQty = new TextView(this);
                    labelQty.setId(400 + count + 1);
                    labelQty.setText(etPaymentMethod.getText());
                    labelQty.setTextAppearance(SettlementActivity.this, android.R.style.TextAppearance_Medium);
                    TableRow.LayoutParams layoutQty = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 30f);
                    layoutQty.setMargins(1, 0, 1, 0);
                    labelQty.setLayoutParams(layoutQty);
                    labelQty.setGravity(Gravity.END);
                    labelQty.setBackgroundColor(getResources().getColor(android.R.color.white));
                    labelQty.setMaxEms(5);
                    tableRow.addView(labelQty);
                    settlementItem.setPaymentMethod(etPaymentMethod.getText().toString());

                    TextView labelSummary = new TextView(this);
                    labelSummary.setId(500 + count + 1);
                    labelSummary.setTextAppearance(SettlementActivity.this, android.R.style.TextAppearance_Medium);
                    TableRow.LayoutParams layoutSummary = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 20f);
                    layoutSummary.setMargins(1, 0, 1, 0);
                    labelSummary.setLayoutParams(layoutSummary);
                    labelSummary.setText(etNominalPayment.getText().toString());
                    labelSummary.setGravity(Gravity.END);
                    labelSummary.setBackgroundColor(getResources().getColor(android.R.color.white));
                    labelSummary.setMaxEms(3);
                    settlementItem.setNominalPayment(Long.valueOf(etNominalPayment.getText().toString()));
                    tableRow.addView(labelSummary);

                    theGrid.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    mapSettlements.put(String.valueOf(tableRow.getId()), settlementItem);
                    addTotal(settlementItem);

                    etInvoiceNumber.setText(null);
                    etInvoiceDate.setText(null);
                    etCredit.setText(null);
                    etPaymentMethod.setText(null);
                    etNominalPayment.setText(null);
                } else {
                    showToast(getResources().getString(R.string.invoice_no_not_found));
                }
            }
        } else if (view == findViewById(R.id.button_proses)) {
            saveAllSettlement();
            finish();

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        initYear, initMonth, initDay);
            case DATE_POPUP_DIALOG_ID:
                return new DatePickerDialog(this, datePickerPopupListener,
                        initPopupYear, initPopupMonth, initPopupDay);
        }
        return null;
    }

    private void saveAllSettlement() {
        settlementBO.insertAll(customerID, new ArrayList<SettlementItem>(mapSettlements.values()));
    }

    private void setCurrentDateOnDatePicker() {
        datePickerField = (DatePicker) findViewById(R.id.dp_fields);

        final Calendar c = Calendar.getInstance();
        initYear = c.get(Calendar.YEAR);
        initMonth = c.get(Calendar.MONTH);
        initDay = c.get(Calendar.DAY_OF_MONTH);

        // set current date into datepicker
        datePickerField.init(initYear, initMonth, initDay, null);
    }

    private void setCurrentDateOnDatePickerPopup() {
        datePickerPopupField = (DatePicker) findViewById(R.id.popup_dp_fields);

        final Calendar c = Calendar.getInstance();
        initPopupYear = c.get(Calendar.YEAR);
        initPopupMonth = c.get(Calendar.MONTH);
        initPopupDay = c.get(Calendar.DAY_OF_MONTH);

        // set current date into datepicker
        datePickerPopupField.init(initYear, initMonth, initDay, null);
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }

    private void addTotal(SettlementItem settlementItem) {
        total = total + settlementItem.getNominalPayment();
        tvTotal.setText(getResources().getString(R.string.currency_symbol) + " " + format.format(total).toString());
    }

    private void subTotal(SettlementItem settlementItem) {
        total = total - settlementItem.getNominalPayment();
        tvTotal.setText(getResources().getString(R.string.currency_symbol) + " " + format.format(total).toString());
    }

    private void calculateTotal(List<SettlementItem> settlementItems) {
        total = 0d;
        for (SettlementItem settlementItem : settlementItems) {
            total = total + settlementItem.getNominalPayment();
        }
        tvTotal.setText(getResources().getString(R.string.currency_symbol) + " " + format.format(total).toString());
    }
}

package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bali.nusadua.productmonitor.model.Billing;
import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.model.Settlement;
import com.bali.nusadua.productmonitor.repo.BillingRepo;
import com.bali.nusadua.productmonitor.repo.SettlementRepo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettlementActivity extends ActionBarActivity implements View.OnClickListener {

    private static final int VIEW_BILLING_ACTIVITY = 1;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private Button btnAdd, btnProses, btnBatal;
    private TableLayout theGrid;
    private EditText etInvoiceNumber, etInvoiceDate, etCredit, etPaymentMethod, etNominalPayment;
    private DatePicker datePickerField, datePickerPopupField;
    private String customerID;
    private int initYear, initMonth, initDay, initPopupYear, initPopupMonth, initPopupDay;
    private DatePickerDialog.OnDateSetListener datePickerListener, datePickerPopupListener;
    private final Context context = this;
    private int countID;

    static final int DATE_DIALOG_ID = 999;
    static final int DATE_POPUP_DIALOG_ID = 998;

    private Map<Integer, Settlement> mapSettlements = new HashMap<Integer, Settlement>();
    private SettlementRepo settlementRepo = new SettlementRepo(this);
    private BillingRepo billingRepo = new BillingRepo(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);

        //Declare UI component
        btnAdd = (Button) findViewById(R.id.button_add);
        btnProses = (Button) findViewById(R.id.button_proses);
        btnBatal = (Button) findViewById(R.id.button_batal);
        theGrid = (TableLayout) findViewById(R.id.tableLayoutData);
        etInvoiceNumber = (EditText) findViewById(R.id.invoice_number);
        etInvoiceDate = (EditText) findViewById(R.id.invoice_date);
        etCredit = (EditText) findViewById(R.id.settlement_credit);
        etPaymentMethod = (EditText) findViewById(R.id.payment_method);
        etNominalPayment = (EditText) findViewById(R.id.nominal_payment);
        setCurrentDateOnDatePicker();

        //Declare Listener
        btnAdd.setOnClickListener(this);
        btnProses.setOnClickListener(this);
        btnBatal.setOnClickListener(this);
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
        getMenuInflater().inflate(R.menu.menu_settlement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you medirecords_adminspecify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_show_billing) {
            Intent intent = new Intent(SettlementActivity.this, ViewBillingActivity.class);
            intent.putExtra(Customer.CUST_ID, customerID);
            startActivityForResult(intent, VIEW_BILLING_ACTIVITY);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (VIEW_BILLING_ACTIVITY) : {
                if (resultCode == Activity.RESULT_OK) {
                    //Update your TextView
                    etInvoiceNumber.setText(data.getStringExtra(Billing.INVOICE_NO));
                    etInvoiceDate.setText(sdf.format(new Date()));
                }
                break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        Settlement settlement = new Settlement();
        int padding_in_dp = 8;  // 6 dps
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
                    TableRow tableRow = new TableRow(this);
                    tableRow.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);

                    tableRow.setBackgroundResource(R.drawable.table_row_even_shape);
                    tableRow.setId(count + 1);

                    TextView labelCode = new TextView(this);
                    labelCode.setId(200 + count + 1);
                    labelCode.setText(etInvoiceNumber.getText() + " | " + etInvoiceDate.getText());
                    labelCode.setTextAppearance(SettlementActivity.this, android.R.style.TextAppearance_Medium);
                    labelCode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 40f));
                    tableRow.addView(labelCode);
                    settlement.setInvoiceNumber(etInvoiceNumber.getText().toString());
                    try {
                        settlement.setInvoiceDate(sdf.parse(etInvoiceDate.getText().toString()));
                    } catch (ParseException e) {
                        settlement.setInvoiceDate(null);
                    }


                    TextView labelPrice = new TextView(this);
                    labelPrice.setId(300 + count + 1);
                    labelPrice.setText(etCredit.getText());
                    labelPrice.setTextAppearance(SettlementActivity.this, android.R.style.TextAppearance_Medium);
                    labelPrice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 20f));
                    tableRow.addView(labelPrice);
                    settlement.setCredit(Long.valueOf(etCredit.getText().toString()));

                    TextView labelQty = new TextView(this);
                    labelQty.setId(400 + count + 1);
                    labelQty.setText(etPaymentMethod.getText());
                    labelQty.setTextAppearance(SettlementActivity.this, android.R.style.TextAppearance_Medium);
                    labelQty.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 20f));
                    tableRow.addView(labelQty);
                    settlement.setPaymentMethod(etPaymentMethod.getText().toString());
                    settlement.setKodeOutlet(customerID);

                    TextView labelSummary = new TextView(this);
                    labelSummary.setId(500 + count + 1);
                    labelSummary.setTextAppearance(SettlementActivity.this, android.R.style.TextAppearance_Medium);
                    labelSummary.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 20f));
                    labelSummary.setText(etNominalPayment.getText().toString());
                    settlement.setNominalPayment(Long.valueOf(etNominalPayment.getText().toString()));
                    tableRow.addView(labelSummary);

                    tableRow.setOnLongClickListener(
                            new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    final TableRow selectedRow = (TableRow) v;
                                    TextView labelCode = (TextView) findViewById(200 + selectedRow.getId());
                                    final Settlement settlement = mapSettlements.get(selectedRow.getId());

                                    // custom dialog
                                    final Dialog dialog = new Dialog(context);
                                    dialog.setContentView(R.layout.popup_edit_settlement);
                                    dialog.setTitle(getResources().getString(R.string.pop_up_settlement_edit));

                                    // set the custom dialog components - text, image and button
                                    final EditText edSettlementCode = (EditText) dialog.findViewById(R.id.popup_settlement_code);
                                    edSettlementCode.setText(settlement.getInvoiceNumber());
                                    edSettlementCode.setEnabled(false);
                                    final EditText edInvoiceDate = (EditText) dialog.findViewById(R.id.popup_invoice_date);
                                    edInvoiceDate.setText(sdf.format(settlement.getInvoiceDate()));
                                    edInvoiceDate.setEnabled(false);
                                    //setCurrentDateOnDatePickerPopup();

                                    /*datePickerPopupField = (DatePicker) dialog.findViewById(R.id.popup_dp_fields);

                                    final Calendar c = Calendar.getInstance();
                                    initPopupYear = c.get(Calendar.YEAR);
                                    initPopupMonth = c.get(Calendar.MONTH);
                                    initPopupDay = c.get(Calendar.DAY_OF_MONTH);

                                    // set current date into datepicker
                                    datePickerPopupField.init(initPopupYear, initPopupMonth, initPopupDay, null);

                                    edInvoiceDate.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            showDialog(DATE_POPUP_DIALOG_ID);
                                        }
                                    });

                                    datePickerPopupListener = new DatePickerDialog.OnDateSetListener() {

                                        // when dialog box is closed, below method will be called.
                                        public void onDateSet(DatePicker view, int selectedYear,
                                                              int selectedMonth, int selectedDay) {

                                            initPopupYear = selectedYear;
                                            initPopupMonth = selectedMonth;
                                            initPopupDay = selectedDay;

                                            // set selected date into textview
                                            edInvoiceDate.setText(new StringBuilder().append(initPopupMonth + 1)
                                                    .append("-").append(initPopupDay).append("-").append(initPopupYear)
                                                    .append(" "));

                                            // set selected date into datepicker also
                                            datePickerPopupField.init(initPopupYear, initPopupMonth, initPopupDay, null);

                                        }
                                    };*/

                                    final EditText edSettlementCredit = (EditText) dialog.findViewById(R.id.popup_settlement_credit);
                                    edSettlementCredit.setText(String.valueOf(settlement.getCredit()));
                                    final EditText edPaymentMethod = (EditText) dialog.findViewById(R.id.popup_payment_method);
                                    edPaymentMethod.setText(settlement.getPaymentMethod().toString());
                                    final EditText edSettlementNominal = (EditText) dialog.findViewById(R.id.popup_settlement_nominal);
                                    edSettlementNominal.setText(settlement.getNominalPayment().toString());


                                    Button popupSaveButton = (Button) dialog.findViewById(R.id.popup_btn_save_data_settlement);
                                    Button popupDeleteButton = (Button) dialog.findViewById(R.id.popup_btn_delete_data_settlement);

                                    // if button is clicked, close the custom dialog
                                    popupSaveButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Settlement settlement = mapSettlements.get(selectedRow.getId());
                                            /*try {
                                                settlement.setInvoiceDate(sdf.parse(edInvoiceDate.getText().toString()));
                                            } catch (ParseException e) {
                                                settlement.setInvoiceDate(null);
                                            }*/
                                            settlement.setCredit(Long.valueOf(edSettlementCredit.getText().toString()));
                                            settlement.setPaymentMethod(edPaymentMethod.getText().toString());
                                            settlement.setNominalPayment(Long.valueOf(edSettlementNominal.getText().toString()));

                                            TextView labelCode = (TextView) findViewById(200 + selectedRow.getId());
                                            labelCode.setText(edSettlementCode.getText() + " | " + edInvoiceDate.getText());
                                            TextView labelCredit = (TextView) findViewById(300 + selectedRow.getId());
                                            labelCredit.setText(edSettlementCredit.getText());
                                            TextView labelPaymentMethod = (TextView) findViewById(400 + selectedRow.getId());
                                            labelPaymentMethod.setText(edPaymentMethod.getText());
                                            TextView labelNominalPayment = (TextView) findViewById(500 + selectedRow.getId());
                                            labelNominalPayment.setText(edSettlementNominal.getText());

                                            dialog.dismiss();
                                        }
                                    });

                                    popupDeleteButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mapSettlements.remove(edSettlementCode.getText().toString());
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
                    mapSettlements.put(tableRow.getId(), settlement);

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

        } else if (view == findViewById(R.id.button_batal)) {
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
        List<Settlement> settlements = new ArrayList<Settlement>(mapSettlements.values());
        settlementRepo.insertAll(settlements);
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
}

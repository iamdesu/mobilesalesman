package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bali.nusadua.productmonitor.model.Settlement;
import com.bali.nusadua.productmonitor.repo.SettlementRepo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettlementActivity extends Activity implements View.OnClickListener {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private Button btnAdd, btnProses, btnBatal;
    private TableLayout theGrid;
    private EditText etInvoiceNumber, etInvoiceDate, etCredit, etPaymentMethod, etNominalPayment;
    private DatePicker datePickerField;
    private String kodeOutlet;
    private int initYear, initMonth, initDay;
    private DatePickerDialog.OnDateSetListener datePickerListener;

    static final int DATE_DIALOG_ID = 999;

    private Map<String, Settlement> mapSettlements = new HashMap<String, Settlement>();
    private SettlementRepo settlementRepo = new SettlementRepo(this);

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
                etInvoiceDate.setText(new StringBuilder().append(initMonth + 1)
                        .append("-").append(initDay).append("-").append(initYear)
                        .append(" "));

                // set selected date into datepicker also
                datePickerField.init(initYear, initMonth, initDay, null);

            }
        };

        Intent intent = getIntent();
        kodeOutlet = intent.getStringExtra("kode_outlet");
        Log.i("Outlet GUID : ", kodeOutlet);
    }

    @Override
    public void onClick(View view) {
        Settlement settlement = new Settlement();
        if (view == findViewById(R.id.button_add) && mapSettlements.get(etInvoiceNumber.getText().toString()) == null) {
            if (!etInvoiceNumber.getText().toString().isEmpty() && etInvoiceNumber.getText().toString().trim() != ""
                    && !etCredit.getText().toString().isEmpty() && etCredit.getText().toString().trim() != ""
                    && !etPaymentMethod.getText().toString().isEmpty() && etPaymentMethod.getText().toString().trim() != ""
                    && !etNominalPayment.getText().toString().isEmpty() && etNominalPayment.getText().toString().trim() != "") {

                int count = theGrid.getChildCount();
                TableRow tableRow = new TableRow(this);
                tableRow.setId(count + 1);

                TextView labelCode = new TextView(this);
                labelCode.setId(200 + count + 1);
                labelCode.setText(etInvoiceNumber.getText() + " " + etInvoiceDate.getText());
                tableRow.addView(labelCode);
                settlement.setInvoiceNumber(etInvoiceNumber.getText().toString());
                try{
                    settlement.setInvoiceDate(sdf.parse(etInvoiceDate.getText().toString()));
                } catch (ParseException e) {
                    settlement.setInvoiceDate(null);
                }


                TextView labelPrice = new TextView(this);
                labelPrice.setId(200 + count + 1);
                labelPrice.setText(etCredit.getText());
                tableRow.addView(labelPrice);
                settlement.setCredit(Long.valueOf(etCredit.getText().toString()));

                TextView labelQty = new TextView(this);
                labelQty.setId(200 + count + 1);
                labelQty.setText(etPaymentMethod.getText());
                tableRow.addView(labelQty);
                settlement.setPaymentMethod(etPaymentMethod.getText().toString());
                settlement.setKodeOutlet(kodeOutlet);

                TextView labelSummary = new TextView(this);
                labelSummary.setId(200 + count + 1);
                labelSummary.setText(etNominalPayment.getText().toString());
                tableRow.addView(labelSummary);

                theGrid.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                mapSettlements.put(settlement.getInvoiceNumber(), settlement);
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
}

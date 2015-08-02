package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.bali.nusadua.productmonitor.model.Settlement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by desu on 7/28/15.
 */
public class EditSettlementActivity extends ActionBarActivity implements android.view.View.OnClickListener {

    private static final int DATE_DIALOG_ID = 999;

    private Settlement settlement = new Settlement();
    private EditText etInvoiceNumber, etInvoiceDate, etCredit, etPayMethod, etNominalPayment;
    private DatePicker datePickerField;
    private Button btnSave;
    private DatePickerDialog.OnDateSetListener datePickerListener;
    private int initYear, initMonth, initDay;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_settlement);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        etInvoiceNumber = (EditText) findViewById(R.id.invoice_number);
        etInvoiceDate = (EditText) findViewById(R.id.invoice_date);
        etCredit = (EditText) findViewById(R.id.settlement_credit);
        etPayMethod = (EditText) findViewById(R.id.payment_method);
        etNominalPayment = (EditText) findViewById(R.id.nominal_payment);

        btnSave = (Button) findViewById(R.id.button_save);

        Intent intent = getIntent();
        settlement = (Settlement) intent.getSerializableExtra(Settlement.TABLE);

        etInvoiceNumber.setText(settlement.getInvoiceNumber());
        etInvoiceDate.setText(sdf.format(settlement.getInvoiceDate()));
        etCredit.setText(settlement.getCredit().toString());
        etPayMethod.setText(settlement.getPaymentMethod());
        etNominalPayment.setText(settlement.getNominalPayment().toString());
        setCurrentDateOnDatePicker();

        //Declare Listener
        btnSave.setOnClickListener(this);
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_settlement, menu);
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
                setResult(RESULT_CANCELED, intent);
                finish();

                break;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.button_save)) {
            Settlement resultSettlement = new Settlement();
            resultSettlement.setId(settlement.getId());
            resultSettlement.setInvoiceNumber(etInvoiceNumber.getText().toString());
            try {
                resultSettlement.setInvoiceDate(sdf.parse(etInvoiceDate.getText().toString()));
            } catch (ParseException e) {
                settlement.setInvoiceDate(null);
            }
            resultSettlement.setCredit(Long.parseLong(etCredit.getText().toString()));
            resultSettlement.setPaymentMethod(etPayMethod.getText().toString());
            resultSettlement.setNominalPayment(Long.parseLong(etNominalPayment.getText().toString()));
            resultSettlement.setKodeOutlet(settlement.getKodeOutlet());

            Intent resultIntent = new Intent();
            resultIntent.putExtra(Settlement.TABLE, resultSettlement);

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, initYear, initMonth, initDay);
        }
        return null;
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

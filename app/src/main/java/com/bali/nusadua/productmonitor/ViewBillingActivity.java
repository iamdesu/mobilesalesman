package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.bali.nusadua.productmonitor.adapter.BillingListViewAdapter;
import com.bali.nusadua.productmonitor.model.Billing;
import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.repo.BillingRepo;

/**
 * Created by desu sudarsana on 6/7/2015.
 */
public class ViewBillingActivity extends ActionBarActivity {
    BillingListViewAdapter adapter;
    private ListView lv;
    private EditText inputSearch;
    private String customerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_billing);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        lv = (ListView) findViewById(R.id.billing_list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearchBilling);

        BillingRepo billingRepo = new BillingRepo(this);

        Intent intent = getIntent();
        customerID = intent.getStringExtra(Customer.CUST_ID);

        adapter = new BillingListViewAdapter(this, billingRepo.getBillingByCustomer(customerID));
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Billing billing = (Billing) lv.getItemAtPosition(position);

                Intent resultIntent = new Intent();
                resultIntent.putExtra(Billing.INVOICE_NO, billing.getInvoiceNo());
                resultIntent.putExtra(Billing.TOTAL_AMOUNT, billing.getTotalAmount());

                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {
                //test

            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                // When user changed the Text
                ViewBillingActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void afterTextChanged(Editable cs) {
                // When user changed the Text

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_billing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you medirecords_adminspecify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();

                break;
        }

        return true;
    }
}

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

import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.model.StockBilling;
import com.bali.nusadua.productmonitor.model.StockPrice;
import com.bali.nusadua.productmonitor.repo.CustomerRepo;

/**
 * Created by desu on 6/28/15.
 */
public class ViewOutletActivity extends ActionBarActivity {
    private static final int TRANSACTION_ACTIVITY = 1;

    private CustomerListViewAdapter adapter;

    private ListView lv;
    private EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_outlet);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearchOutlet);

        CustomerRepo customerRepo = new CustomerRepo(this);
        adapter = new CustomerListViewAdapter(this, customerRepo.getAll());
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Customer customer = (Customer) lv.getItemAtPosition(position);

                Intent intent = new Intent(ViewOutletActivity.this, TransactionActivity.class);
                intent.putExtra(Customer.CUST_ID, customer.getCustomerId());

                //startActivity(intent);
                startActivityForResult(intent, TRANSACTION_ACTIVITY);
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
                ViewOutletActivity.this.adapter.getFilter().filter(cs);
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
        getMenuInflater().inflate(R.menu.menu_view_outlet, menu);
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

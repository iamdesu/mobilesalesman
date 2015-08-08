package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.bali.nusadua.productmonitor.adapter.StockListViewAdapter;
import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.model.StockBilling;
import com.bali.nusadua.productmonitor.model.StockPrice;
import com.bali.nusadua.productmonitor.modelView.StockView;
import com.bali.nusadua.productmonitor.repo.CustomerRepo;
import com.bali.nusadua.productmonitor.repo.StockPriceRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by desu sudarsana on 6/7/2015.
 */
public class ViewStockActivity extends ActionBarActivity {
    private ListView lv;
    private EditText inputSearch;
    private String customerID;

    StockListViewAdapter adapter;

    ArrayList<HashMap<String, String>> stockList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_stock);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearchStock);

        Intent intent = getIntent();
        customerID = intent.getStringExtra(Customer.CUST_ID);
        CustomerRepo customerRepo = new CustomerRepo(this);
        Customer customer = customerRepo.findByCustomerID(customerID);

        StockPriceRepo stockPriceRepo = new StockPriceRepo(this);
        Log.i("Customer Level : ", customer.getPriceLevel().toString());
        List<StockView> stockViews = stockPriceRepo.getStockByCustomerLevel(customer.getPriceLevel());
        adapter = new StockListViewAdapter(this, stockViews);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                StockView stockView = (StockView) lv.getItemAtPosition(position);

                Intent resultIntent = new Intent();
                resultIntent.putExtra(StockBilling.STOCK_ID, stockView.getStockBilling().getStockId());
                resultIntent.putExtra(StockBilling.DESCRIPTION, stockView.getStockBilling().getDescription());
                resultIntent.putExtra(StockPrice.PRICE, stockView.getStockPrice().getPrice().toString());

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
                ViewStockActivity.this.adapter.getFilter().filter(cs);
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
        getMenuInflater().inflate(R.menu.menu_view_stock, menu);
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

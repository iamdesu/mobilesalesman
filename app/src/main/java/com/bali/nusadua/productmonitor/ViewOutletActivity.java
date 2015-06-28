package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.repo.CustomerRepo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by desu on 6/28/15.
 */
public class ViewOutletActivity extends Activity {
    CustomerListViewAdapter adapter;
    ArrayList<HashMap<String, String>> stockList;
    private ListView lv;
    private EditText inputSearch;
    private String customerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_outlet);
        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearchOutlet);

        CustomerRepo customerRepo = new CustomerRepo(this);
        adapter = new CustomerListViewAdapter(this, customerRepo.getAll());
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Customer customer = (Customer) lv.getItemAtPosition(position);

                Intent intent = new Intent(ViewOutletActivity.this, TransaksiActivity.class);
                intent.putExtra(Customer.CUST_ID, customer.getCustomerId());

                startActivity(intent);
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
}

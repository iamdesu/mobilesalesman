package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bali.nusadua.productmonitor.adapter.BillingRecycleViewAdapter;
import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.repo.BillingRepo;

/**
 * Created by desu sudarsana on 6/7/2015.
 */
public class ViewBillingActivity extends Activity {
    private String customerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_billing);
        RecyclerView recList = (RecyclerView) findViewById(R.id.rv_billing);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        BillingRepo billingRepo = new BillingRepo(this);

        Intent intent = getIntent();
        customerID = intent.getStringExtra(Customer.CUST_ID);

        BillingRecycleViewAdapter brva = new BillingRecycleViewAdapter(billingRepo.getBillingByCustomer(customerID));
        recList.setAdapter(brva);
    }
}

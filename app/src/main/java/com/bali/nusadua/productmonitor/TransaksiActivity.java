package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.bali.nusadua.productmonitor.adapter.SpinnerCustomerAdapter;
import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.repo.CustomerRepo;

import java.util.List;

public class TransaksiActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Button btnSummary, btnProses;
    private LinearLayout blockOrderPenjualan, blockRetur, blockPelunasan;
    private RelativeLayout orderCard, returCard, settlementCard;
    private Spinner spinnerCustomer;
    private Customer customer = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        //Declare component UI
        btnProses = (Button) findViewById(R.id.btn_proses);
        btnSummary = (Button) findViewById(R.id.btn_summary);
        /*blockOrderPenjualan = (LinearLayout) findViewById(R.id.block_order);
        blockRetur = (LinearLayout) findViewById(R.id.block_retur);
        blockPelunasan = (LinearLayout) findViewById(R.id.block_pelunasan);*/
        orderCard = (RelativeLayout) findViewById(R.id.order_card);
        returCard = (RelativeLayout) findViewById(R.id.retur_card);
        settlementCard = (RelativeLayout) findViewById(R.id.settlement_card);
        spinnerCustomer = (Spinner) findViewById(R.id.spinnerOutlet);

        btnProses.setOnClickListener(this);
        btnSummary.setOnClickListener(this);
        /*blockOrderPenjualan.setOnClickListener(this);
        blockRetur.setOnClickListener(this);
        blockPelunasan.setOnClickListener(this);*/
        orderCard.setOnClickListener(this);
        returCard.setOnClickListener(this);
        settlementCard.setOnClickListener(this);
        spinnerCustomer.setOnItemSelectedListener(this);

        loadCustomer();
    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.order_card) && customer != null) {
            Intent intent = new Intent(TransaksiActivity.this, OrderPenjualanActivity.class);
            intent.putExtra(Customer.CUST_ID, customer.getCustomerId());
            startActivity(intent);
        } else if (view == findViewById(R.id.retur_card) && customer != null) {
            Intent intent = new Intent(TransaksiActivity.this, ReturPenjualanActivity.class);
            intent.putExtra(Customer.CUST_ID, customer.getCustomerId());
            startActivity(intent);
        } else if (view == findViewById(R.id.settlement_card) && customer != null) {
            Intent intent = new Intent(TransaksiActivity.this, SettlementActivity.class);
            intent.putExtra(Customer.CUST_ID, customer.getCustomerId());
            startActivity(intent);
        } else if (view == findViewById(R.id.btn_summary)) {
            Intent intent = new Intent(TransaksiActivity.this, SummaryActivity.class);
            startActivity(intent);
        }
    }

    /*private void loadOutlet() {
        SpinnerCustomerAdapter adapter = null;
        CustomerRepo repo = new CustomerRepo(getApplicationContext());
        List<Customer> customers = repo.getAll();
        int size = customers.size();
        Log.i("List size : ", Integer.toString(size));
        *//*for(int i = 0; i < size; i++){
            Log.i("Outlets", outlets.get(i).getName());
        }*//*
        adapter = new SpinnerCustomerAdapter(TransaksiActivity.this, android.R.layout.simple_spinner_item, customers);
        spinnerOutlet.setAdapter(adapter);

        outlet = customers.get(0);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }*/

    private void loadCustomer() {
        SpinnerCustomerAdapter adapter = null;
        CustomerRepo repo = new CustomerRepo(getApplicationContext());
        List<Customer> customers = repo.getAll();
        int size = customers.size();
        Log.i("List size : ", Integer.toString(size));

        adapter = new SpinnerCustomerAdapter(TransaksiActivity.this, android.R.layout.simple_spinner_item, customers);
        spinnerCustomer.setAdapter(adapter);

        customer = customers.get(0);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == findViewById(R.id.spinnerOutlet)) {
            Customer selectedCustomer = (Customer) parent.getItemAtPosition(position);
            customer = selectedCustomer;
            Log.i("Customer ID : ", customer.getCustomerId());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        customer = null;
    }
}

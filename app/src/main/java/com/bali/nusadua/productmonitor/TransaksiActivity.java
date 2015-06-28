package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bali.nusadua.productmonitor.adapter.SpinnerCustomerAdapter;
import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.model.Order;
import com.bali.nusadua.productmonitor.model.Retur;
import com.bali.nusadua.productmonitor.model.Settlement;
import com.bali.nusadua.productmonitor.repo.CustomerRepo;
import com.bali.nusadua.productmonitor.repo.OrderRepo;
import com.bali.nusadua.productmonitor.repo.ReturRepo;
import com.bali.nusadua.productmonitor.repo.SettlementRepo;

import java.util.ArrayList;
import java.util.List;

public class TransaksiActivity extends Activity implements View.OnClickListener {

    private Button btnSummary, btnProses;
    private LinearLayout blockOrderPenjualan, blockRetur, blockPelunasan;
    private RelativeLayout orderCard, returCard, settlementCard;
    private EditText companyName;
    private TextView labelOrderData, labelReturData, labelSettlementData;
    private Customer customer = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        //Declare component UI
        btnProses = (Button) findViewById(R.id.btn_proses);
        btnSummary = (Button) findViewById(R.id.btn_summary);
        orderCard = (RelativeLayout) findViewById(R.id.order_card);
        returCard = (RelativeLayout) findViewById(R.id.retur_card);
        settlementCard = (RelativeLayout) findViewById(R.id.settlement_card);
        companyName = (EditText) findViewById(R.id.company_name);
        labelOrderData = (TextView) findViewById(R.id.label_order_data);
        labelReturData = (TextView) findViewById(R.id.label_retur_data);
        labelSettlementData = (TextView) findViewById(R.id.label_settlement_data);

        btnProses.setOnClickListener(this);
        btnSummary.setOnClickListener(this);
        orderCard.setOnClickListener(this);
        returCard.setOnClickListener(this);
        settlementCard.setOnClickListener(this);

        Intent intent = getIntent();
        customer = (new CustomerRepo(this)).findByCustomerID(intent.getStringExtra(Customer.CUST_ID));
        companyName.setText(customer.getCompanyName());

        setTransactionCount();
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

    @Override
    protected void onResume() {
        super.onResume();
        setTransactionCount();
    }

    /*private void loadCustomer() {
        SpinnerCustomerAdapter adapter = null;
        CustomerRepo repo = new CustomerRepo(getApplicationContext());
        List<Customer> customers = repo.getAll();
        int size = customers.size();
        Log.i("List size : ", Integer.toString(size));

        adapter = new SpinnerCustomerAdapter(TransaksiActivity.this, android.R.layout.simple_spinner_item, customers);
        spinnerCustomer.setAdapter(adapter);

        customer = customers.get(0);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }*/

    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == findViewById(R.id.spinnerOutlet)) {
            Customer selectedCustomer = (Customer) parent.getItemAtPosition(position);
            customer = selectedCustomer;
            Log.i("Customer ID : ", customer.getCustomerId());
            setTransactionCount();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }*/

    private void setTransactionCount() {
        OrderRepo orderRepo = new OrderRepo(getApplicationContext());
        List<Order> orders = orderRepo.getOrderByCustomer(customer.getCustomerId());
        labelOrderData.setText(String.valueOf(orders.size()));

        ReturRepo returRepo = new ReturRepo(getApplicationContext());
        List<Retur> returs = returRepo.getReturByCustomer(customer.getCustomerId());
        labelReturData.setText(String.valueOf(returs.size()));

        SettlementRepo settlementRepo = new SettlementRepo(getApplicationContext());
        List<Settlement> settlements = settlementRepo.getSettlementByCustomer(customer.getCustomerId());
        labelSettlementData.setText(String.valueOf(settlements.size()));
    }
}

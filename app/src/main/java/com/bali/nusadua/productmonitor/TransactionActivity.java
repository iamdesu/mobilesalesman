package com.bali.nusadua.productmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.model.Order;
import com.bali.nusadua.productmonitor.model.Retur;
import com.bali.nusadua.productmonitor.model.Settlement;
import com.bali.nusadua.productmonitor.repo.CustomerRepo;
import com.bali.nusadua.productmonitor.repo.OrderRepo;
import com.bali.nusadua.productmonitor.repo.ReturRepo;
import com.bali.nusadua.productmonitor.repo.SettlementRepo;

import java.util.List;

public class TransactionActivity extends ActionBarActivity implements View.OnClickListener {

    private RelativeLayout orderCard, returCard, settlementCard, summaryCard;
    private TextView labelOrderData, labelReturData, labelSettlementData, companyName, companyAddress;
    private Customer customer = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Declare component UI
        orderCard = (RelativeLayout) findViewById(R.id.order_card);
        returCard = (RelativeLayout) findViewById(R.id.retur_card);
        settlementCard = (RelativeLayout) findViewById(R.id.settlement_card);
        summaryCard = (RelativeLayout) findViewById(R.id.summary_card);
        companyName = (TextView) findViewById(R.id.company_name);
        companyAddress = (TextView) findViewById(R.id.company_address);
        labelOrderData = (TextView) findViewById(R.id.label_order_data);
        labelReturData = (TextView) findViewById(R.id.label_retur_data);
        labelSettlementData = (TextView) findViewById(R.id.label_settlement_data);

        orderCard.setOnClickListener(this);
        returCard.setOnClickListener(this);
        settlementCard.setOnClickListener(this);
        summaryCard.setOnClickListener(this);

        Intent intent = getIntent();
        customer = (new CustomerRepo(this)).findByCustomerID(intent.getStringExtra(Customer.CUST_ID));
        companyName.setText(customer.getCompanyName());
        companyAddress.setText(customer.getAddress() + " " + customer.getRegion() + " " + customer.getCity());

        setTransactionCount();
    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.order_card) && customer != null) {
            Intent intent = new Intent(TransactionActivity.this, OrderPenjualanActivity.class);
            intent.putExtra(Customer.CUST_ID, customer.getCustomerId());
            startActivity(intent);
        } else if (view == findViewById(R.id.retur_card) && customer != null) {
            Intent intent = new Intent(TransactionActivity.this, ReturPenjualanActivity.class);
            intent.putExtra(Customer.CUST_ID, customer.getCustomerId());
            startActivity(intent);
        } else if (view == findViewById(R.id.settlement_card) && customer != null) {
            Intent intent = new Intent(TransactionActivity.this, SettlementActivity.class);
            intent.putExtra(Customer.CUST_ID, customer.getCustomerId());
            startActivity(intent);
        } else if (view == findViewById(R.id.summary_card) && customer != null) {
            Intent intent = new Intent(TransactionActivity.this, SummaryActivity.class);
            intent.putExtra(Customer.CUST_ID, customer.getCustomerId());
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTransactionCount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transaction, menu);
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

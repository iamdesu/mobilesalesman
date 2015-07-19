package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.bali.nusadua.productmonitor.model.Order;
import com.bali.nusadua.productmonitor.model.StockBilling;
import com.bali.nusadua.productmonitor.model.StockPrice;

import java.io.Serializable;

/**
 * Created by desu on 7/18/15.
 */
public class EditOrderActivity extends ActionBarActivity implements android.view.View.OnClickListener {

    private Order order = new Order();
    private EditText orderCode, orderName, price, qty;
    private Spinner unit;
    private Button btnSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        orderCode = (EditText) findViewById(R.id.order_code);
        orderName = (EditText) findViewById(R.id.order_name);
        price = (EditText) findViewById(R.id.order_price);
        qty = (EditText) findViewById(R.id.order_qty);
        unit = (Spinner) findViewById(R.id.unit_spinner);
        btnSave = (Button) findViewById(R.id.button_save);

        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra(Order.TABLE);

        orderCode.setText(order.getKode());
        orderName.setText(order.getNamaBarang());
        price.setText(String.valueOf(order.getHarga()));
        qty.setText(String.valueOf(order.getQty()));
        unit.setSelection(order.getUnit().toLowerCase() == Order.LOOKUP_DUS ? 0 : 1);

        btnSave.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_order, menu);
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
            Order resultOrder = new Order();
            resultOrder.setId(order.getId());
            resultOrder.setKode(orderCode.getText().toString());
            resultOrder.setNamaBarang(orderName.getText().toString());
            resultOrder.setHarga(Double.parseDouble(price.getText().toString()));
            resultOrder.setQty(Integer.parseInt(qty.getText().toString()));
            resultOrder.setUnit(unit.getSelectedItem().toString());

            Intent resultIntent = new Intent();
            resultIntent.putExtra(Order.TABLE, resultOrder);

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}

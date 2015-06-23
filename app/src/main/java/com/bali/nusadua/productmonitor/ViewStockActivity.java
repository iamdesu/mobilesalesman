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

import com.bali.nusadua.productmonitor.adapter.StockListViewAdapter;
import com.bali.nusadua.productmonitor.model.StockBilling;
import com.bali.nusadua.productmonitor.repo.StockBillingRepo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by desu sudarsana on 6/7/2015.
 */
public class ViewStockActivity extends Activity {
    private ListView lv;
    private EditText inputSearch;

    StockListViewAdapter adapter;

    ArrayList<HashMap<String, String>> stockList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_stock);
        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearchStock);

        StockBillingRepo stockBillingRepo = new StockBillingRepo(this);

        adapter = new StockListViewAdapter(this, stockBillingRepo.getStockBillings());
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                StockBilling stockBilling = (StockBilling) lv.getItemAtPosition(position);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("kode", stockBilling.getScode());
                resultIntent.putExtra("nama_barang", stockBilling.getDescription());

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
}

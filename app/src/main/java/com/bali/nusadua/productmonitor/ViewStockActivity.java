package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bali.nusadua.productmonitor.adapter.StockRecycleViewAdapter;
import com.bali.nusadua.productmonitor.repo.StockBillingRepo;

/**
 * Created by desu sudarsana on 6/7/2015.
 */
public class ViewStockActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_stock);
        RecyclerView recList = (RecyclerView) findViewById(R.id.rv);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        StockBillingRepo stockBillingRepo = new StockBillingRepo(this);

        StockRecycleViewAdapter srva = new StockRecycleViewAdapter(stockBillingRepo.getAll());
        recList.setAdapter(srva);
    }
}

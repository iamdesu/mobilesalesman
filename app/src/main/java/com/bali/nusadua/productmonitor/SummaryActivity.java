package com.bali.nusadua.productmonitor;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.TextView;

import com.bali.nusadua.productmonitor.fragment.SummaryOrderFragmentTab;
import com.bali.nusadua.productmonitor.fragment.SummaryReturFragmentTab;
import com.bali.nusadua.productmonitor.fragment.SummarySettlementFragmentTab;

public class SummaryActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tabOrder").setIndicator("OrderItem", null), SummaryOrderFragmentTab.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tabRetur").setIndicator("ReturItem", null), SummaryReturFragmentTab.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tabLunas").setIndicator("Lunas", null), SummarySettlementFragmentTab.class, null);
    }

}

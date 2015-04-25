package com.bali.nusadua.productmonitor;

import android.app.TabActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.bali.nusadua.productmonitor.fragment.FragmentTab;
import com.bali.nusadua.productmonitor.fragment.SummaryOrderFragmentTab;

/**
 * Created by desu sudarsana on 4/26/2015.
 */
public class SummaryActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tabOrder").setIndicator("Order", null), SummaryOrderFragmentTab.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tabRetur").setIndicator("Retur", null), FragmentTab.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tabLunas").setIndicator("Lunas", null), FragmentTab.class, null);
    }

}

package com.bali.nusadua.productmonitor.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bali.nusadua.productmonitor.R;
import com.bali.nusadua.productmonitor.model.Retur;
import com.bali.nusadua.productmonitor.repo.ReturRepo;

import java.util.List;

public class SummaryReturFragmentTab extends Fragment {

    private ReturRepo returRepo;

    /* Widget */
    private TableLayout returGrid;
    private Context context;
    private List<Retur> returs;

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        View v = inflater.inflate(R.layout.fragment_summary_retur, container, false);
        context = getActivity();
        returRepo = new ReturRepo(context);
        returs = returRepo.getAll();
        Log.i("Summary Retur di database : ", Integer.toString(returs.size()));

        returGrid = (TableLayout) v.findViewById(R.id.tableLayoutData);

        for(Retur order : returs) {
            int count = returGrid.getChildCount();
            TableRow tableRow = new TableRow(context);
            tableRow.setId(count + 1);

            TextView labelCode = new TextView(context);
            labelCode.setId(200 + count + 1); //TODO: we need to redefine ID
            labelCode.setText(order.getKode() + " " + order.getNamaBarang());
            tableRow.addView(labelCode);

            TextView labelPrice = new TextView(context);
            labelPrice.setId(201 + count + 1);
            labelPrice.setText(String.valueOf(order.getHarga()));
            tableRow.addView(labelPrice);

            TextView labelQty = new TextView(context);
            labelQty.setId(202 + count + 1);
            labelQty.setText(String.valueOf(order.getQty()) + "/" + order.getUnit());
            tableRow.addView(labelQty);

            TextView labelSummary = new TextView(context);
            labelSummary.setId(203 + count + 1);
            Integer summary = order.getQty() * order.getHarga();
            labelSummary.setText(summary.toString());
            tableRow.addView(labelSummary);

            returGrid.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

        return v;
    }
}

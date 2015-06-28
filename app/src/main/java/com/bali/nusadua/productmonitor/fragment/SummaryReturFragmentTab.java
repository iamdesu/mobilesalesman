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

        int padding_in_dp = 8;  // 6 dps
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);

        for(Retur retur : returs) {
            int count = returGrid.getChildCount();
            TableRow tableRow = new TableRow(context);
            tableRow.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
            tableRow.setBackgroundResource(R.drawable.table_row_even_shape);
            tableRow.setId(count + 1);

            TextView labelCode = new TextView(context);
            labelCode.setId(200 + count + 1); //TODO: we need to redefine ID
            labelCode.setText(retur.getKode() + " " + retur.getNamaBarang());
            labelCode.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            tableRow.addView(labelCode);

            TextView labelPrice = new TextView(context);
            labelPrice.setId(201 + count + 1);
            labelPrice.setText(String.valueOf(retur.getHarga()));
            labelPrice.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            tableRow.addView(labelPrice);

            TextView labelQty = new TextView(context);
            labelQty.setId(202 + count + 1);
            labelQty.setText(String.valueOf(retur.getQty()) + "/" + retur.getUnit());
            labelQty.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            tableRow.addView(labelQty);

            TextView labelSummary = new TextView(context);
            labelSummary.setId(203 + count + 1);
            Double summary = retur.getQty() * retur.getHarga();
            labelSummary.setText(summary.toString());
            labelSummary.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            tableRow.addView(labelSummary);

            returGrid.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

        return v;
    }
}

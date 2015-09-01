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
import com.bali.nusadua.productmonitor.model.SettlementItem;
import com.bali.nusadua.productmonitor.repo.SettlementItemRepo;

import java.text.SimpleDateFormat;
import java.util.List;

public class SummarySettlementFragmentTab extends Fragment {

    private SettlementItemRepo settlementItemRepo;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    /*Widget*/
    private TableLayout settlementGrid;
    private Context context;
    private List<SettlementItem> settlementItems;

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        View v = inflater.inflate(R.layout.fragment_summary_settlement, container, false);
        context = getActivity();
        settlementItemRepo = new SettlementItemRepo(context);
        settlementItems = settlementItemRepo.getAll();
        Log.i("Summary ReturItem di database : ", Integer.toString(settlementItems.size()));

        settlementGrid = (TableLayout) v.findViewById(R.id.tableLayoutData);

        int padding_in_dp = 8;  // 6 dps
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);

        for(SettlementItem settlementItem : settlementItems) {
            int count = settlementGrid.getChildCount();
            TableRow tableRow = new TableRow(context);
            tableRow.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
            tableRow.setBackgroundResource(R.drawable.table_row_even_shape);
            tableRow.setId(count + 1);

            TextView labelNumber = new TextView(context);
            labelNumber.setId(200 + count + 1); //TODO: we need to redefine ID
            labelNumber.setText(settlementItem.getInvoiceNumber());
            labelNumber.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            tableRow.addView(labelNumber);

            TextView labelDate = new TextView(context);
            labelDate.setId(201 + count + 1);
            labelDate.setText(sdf.format(settlementItem.getInvoiceDate()));
            labelDate.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            tableRow.addView(labelDate);

            TextView lableCredit = new TextView(context);
            lableCredit.setId(202 + count + 1);
            lableCredit.setText(String.valueOf(settlementItem.getCredit()));
            lableCredit.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            tableRow.addView(lableCredit);

            TextView labelPayMethod = new TextView(context);
            labelPayMethod.setId(203 + count + 1);
            labelPayMethod.setText(settlementItem.getPaymentMethod());
            labelPayMethod.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            tableRow.addView(labelPayMethod);

            TextView labelNominalPay = new TextView(context);
            labelNominalPay.setId(204 + count + 1);
            labelNominalPay.setText(String.valueOf(settlementItem.getNominalPayment()));
            labelNominalPay.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            tableRow.addView(labelNominalPay);

            settlementGrid.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

        return v;
    }
}

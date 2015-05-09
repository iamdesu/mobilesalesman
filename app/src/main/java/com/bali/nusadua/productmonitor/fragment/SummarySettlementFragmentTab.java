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
import com.bali.nusadua.productmonitor.model.Settlement;
import com.bali.nusadua.productmonitor.repo.SettlementRepo;

import java.text.SimpleDateFormat;
import java.util.List;

public class SummarySettlementFragmentTab extends Fragment {

    private SettlementRepo settlementRepo;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    /*Widget*/
    private TableLayout settlementGrid;
    private Context context;
    private List<Settlement> settlements;

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        View v = inflater.inflate(R.layout.fragment_summary_settlement, container, false);
        context = getActivity();
        settlementRepo = new SettlementRepo(context);
        settlements = settlementRepo.getAll();
        Log.i("Summary Retur di database : ", Integer.toString(settlements.size()));

        settlementGrid = (TableLayout) v.findViewById(R.id.tableLayoutData);

        for(Settlement settlement : settlements) {
            int count = settlementGrid.getChildCount();
            TableRow tableRow = new TableRow(context);
            tableRow.setId(count + 1);

            TextView labelNumber = new TextView(context);
            labelNumber.setId(200 + count + 1); //TODO: we need to redefine ID
            labelNumber.setText(settlement.getInvoiceNumber());
            tableRow.addView(labelNumber);

            TextView labelDate = new TextView(context);
            labelDate.setId(201 + count + 1);
            labelDate.setText(sdf.format(settlement.getInvoiceDate()));
            tableRow.addView(labelDate);

            TextView lableCredit = new TextView(context);
            lableCredit.setId(202 + count + 1);
            lableCredit.setText(String.valueOf(settlement.getCredit()));
            tableRow.addView(lableCredit);

            TextView labelPayMethod = new TextView(context);
            labelPayMethod.setId(203 + count + 1);
            labelPayMethod.setText(settlement.getPaymentMethod());
            tableRow.addView(labelPayMethod);

            TextView labelNominalPay = new TextView(context);
            labelNominalPay.setId(204 + count + 1);
            labelNominalPay.setText(String.valueOf(settlement.getNominalPayment()));
            tableRow.addView(labelNominalPay);

            settlementGrid.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

        return v;
    }
}

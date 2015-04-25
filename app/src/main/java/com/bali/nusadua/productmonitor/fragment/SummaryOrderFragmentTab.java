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
import com.bali.nusadua.productmonitor.model.Order;
import com.bali.nusadua.productmonitor.repo.OrderRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by desu sudarsana on 4/26/2015.
 */
public class SummaryOrderFragmentTab extends Fragment {

    private TableLayout orderGrid;
    private OrderRepo orderRepo;
    private Context context;
    private List<Order> orders = new ArrayList<Order>();

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_summary_order, container, false);
        context = getActivity();
        orderRepo  = new OrderRepo(context);
        orders = orderRepo.getAll();
        Log.i("Summary order di database : ", Integer.toString(orders.size()));

        orderGrid = (TableLayout) v.findViewById(R.id.tableLayoutData);

        for(Order order : orders) {
            int count = orderGrid.getChildCount();
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

            orderGrid.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

        return v;
    }
}

package com.bali.nusadua.productmonitor.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bali.nusadua.productmonitor.R;
import com.bali.nusadua.productmonitor.model.StockBilling;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by desu sudarsana on 6/7/2015.
 */
public class StockRecycleViewAdapter extends RecyclerView.Adapter<StockRecycleViewAdapter.StockViewHolder> {

    private List<StockBilling> stockBillings;
    private List<StockBilling> filterStockBillings;

    public StockRecycleViewAdapter(List<StockBilling> stockBillings) {
        this.stockBillings = stockBillings;
        this.filterStockBillings = new ArrayList<StockBilling>();
    }

    @Override
    public int getItemCount() {
        return stockBillings.size();
    }

    @Override
    public void onBindViewHolder(StockViewHolder stockViewHolder, int i) {
        /*StockBilling sb;
        if(filterStockBillings.size() > 0) {
            sb = filterStockBillings.get(i);
        } else {
            sb = stockBillings.get(i);
        }*/
        StockBilling sb = stockBillings.get(i);
        //StockBilling sb = filterStockBillings.get(i);
        stockViewHolder.stock.setText(sb.getStockId());
        stockViewHolder.description.setText(sb.getScode() + " | " + sb.getDescription());
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_stock, viewGroup, false);
        StockViewHolder svh = new StockViewHolder(v);
        return svh;
    }

    public void flushFilter(){
        filterStockBillings = new ArrayList<>();
        filterStockBillings.addAll(stockBillings);
        notifyDataSetChanged();
    }

    public void setFilter(CharSequence queryText) {

        filterStockBillings = new ArrayList<>();
        /*if (queryText.length() == 0) {
            flushFilter();
        } else {*/
            queryText = queryText.toString().toLowerCase();
            for (StockBilling item: stockBillings) {
                if (item.getStockId().contains(queryText)) {
                    filterStockBillings.add(item);
                }
            }
            notifyDataSetChanged();
        /*}*/
    }

    public static class StockViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView stock;
        TextView description;

        StockViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            stock = (TextView) itemView.findViewById(R.id.stock_id);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}

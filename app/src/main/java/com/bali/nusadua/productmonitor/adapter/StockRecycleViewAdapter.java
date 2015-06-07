package com.bali.nusadua.productmonitor.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bali.nusadua.productmonitor.R;
import com.bali.nusadua.productmonitor.model.StockBilling;

import java.util.List;

/**
 * Created by desu sudarsana on 6/7/2015.
 */
public class StockRecycleViewAdapter extends RecyclerView.Adapter<StockRecycleViewAdapter.StockViewHolder> {

    List<StockBilling> stockBillings;

    public StockRecycleViewAdapter(List<StockBilling> stockBillings) {
        this.stockBillings = stockBillings;
    }

    @Override
    public int getItemCount() {
        return stockBillings.size();
    }

    @Override
    public void onBindViewHolder(StockViewHolder stockViewHolder, int i) {
        StockBilling sb = stockBillings.get(i);
        stockViewHolder.stock.setText(sb.getStockId());
        stockViewHolder.description.setText(sb.getScode() + " | " + sb.getDescription());
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_stock, viewGroup, false);
        StockViewHolder svh = new StockViewHolder(v);
        return svh;
    }

    public static class StockViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView stock;
        TextView description;

        StockViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            stock = (TextView)itemView.findViewById(R.id.stock_id);
            description = (TextView)itemView.findViewById(R.id.description);
        }
    }
}

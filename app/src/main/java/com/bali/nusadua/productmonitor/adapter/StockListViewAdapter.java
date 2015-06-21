package com.bali.nusadua.productmonitor.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bali.nusadua.productmonitor.R;
import com.bali.nusadua.productmonitor.model.StockBilling;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desu on 6/20/2015.
 */
public class StockListViewAdapter extends ArrayAdapter<StockBilling> implements Filterable {
    private Activity activity;
    private LayoutInflater inflater;
    private List<StockBilling> stockBillings;
    private List<StockBilling> filteredStockBillings;
    private ModelFilter filter;

    public StockListViewAdapter(Activity activity, List<StockBilling> listStockBilling) {
        super(activity, R.layout.list_view_stock, listStockBilling);
        this.activity = activity;
        this.stockBillings = new ArrayList<StockBilling>();
        this.stockBillings.addAll(listStockBilling);
        this.filteredStockBillings = new ArrayList<StockBilling>();
        this.filteredStockBillings.addAll(this.stockBillings);
        inflater = activity.getLayoutInflater();
        getFilter();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ModelFilter();
        }
        return filter;
    }

    static class ViewHolder {
        protected TextView stock_icon;
        protected TextView stock_code;
        protected TextView stock_description;
    }

    /*@Override
    public StockBilling getItem(int location) {
        return stockBillings.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        StockBilling sb = filteredStockBillings.get(position);
        ViewHolder viewHolder = null;

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_view_stock, null);
            viewHolder = new ViewHolder();
            viewHolder.stock_icon = (TextView) view.findViewById(R.id.stock_icon);
            viewHolder.stock_code = (TextView) view.findViewById(R.id.stock_code);
            viewHolder.stock_description = (TextView) view.findViewById(R.id.stock_description);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = ((ViewHolder) view.getTag());
        }

        viewHolder.stock_icon.setText(sb.getScode().toUpperCase().substring(0, 1));
        viewHolder.stock_code.setText(sb.getScode());
        viewHolder.stock_description.setText(sb.getDescription().trim());

        return view;
    }

    private class ModelFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<StockBilling> filteredItems = new ArrayList<StockBilling>();

                for (int i = 0, l = stockBillings.size(); i < l; i++) {
                    StockBilling sb = stockBillings.get(i);
                    if (sb.getScode().toLowerCase().contains(constraint) ||
                            sb.getDescription().toLowerCase().contains(constraint)) {
                        filteredItems.add(sb);
                    }
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.count = stockBillings.size();
                    result.values = stockBillings;
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredStockBillings = (ArrayList<StockBilling>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filteredStockBillings.size(); i < l; i++) {
                add(filteredStockBillings.get(i));
            }
            notifyDataSetChanged();
        }
    }
}

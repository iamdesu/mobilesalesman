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
import com.bali.nusadua.productmonitor.modelView.StockView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Desu on 6/20/2015.
 */
public class StockListViewAdapter extends ArrayAdapter<StockView> implements Filterable {
    private Activity activity;
    private LayoutInflater inflater;
    private List<StockView> stockViews;
    private List<StockView> filteredStockViews;
    private ModelFilter filter;

    public StockListViewAdapter(Activity activity, List<StockView> listStockView) {
        super(activity, R.layout.list_view_stock, listStockView);
        this.activity = activity;
        this.stockViews = new ArrayList<StockView>();
        this.stockViews.addAll(listStockView);
        this.filteredStockViews = new ArrayList<StockView>();
        this.filteredStockViews.addAll(this.stockViews);
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
        protected TextView stock_price;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        StockView sv = filteredStockViews.get(position);
        ViewHolder viewHolder = null;

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_view_stock, null);
            viewHolder = new ViewHolder();
            viewHolder.stock_icon = (TextView) view.findViewById(R.id.stock_icon);
            viewHolder.stock_code = (TextView) view.findViewById(R.id.stock_code);
            viewHolder.stock_description = (TextView) view.findViewById(R.id.stock_description);
            viewHolder.stock_price = (TextView) view.findViewById(R.id.stock_price);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = ((ViewHolder) view.getTag());
        }

        NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);

        viewHolder.stock_icon.setText(sv.getStockBilling().getScode().toUpperCase().substring(0, 1));
        viewHolder.stock_code.setText(sv.getStockBilling().getStockId() + " - " + sv.getStockBilling().getScode());
        viewHolder.stock_description.setText(sv.getStockBilling().getDescription().trim());
        viewHolder.stock_price.setText(parent.getResources().getString(R.string.currency_symbol) + " " + format.format(sv.getStockPrice().getPrice()).toString());

        return view;
    }

    private class ModelFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<StockView> filteredItems = new ArrayList<StockView>();

                for (int i = 0, l = stockViews.size(); i < l; i++) {
                    StockView sv = stockViews.get(i);
                    if (sv.getStockBilling().getStockId().toLowerCase().contains(constraint) ||
                            sv.getStockBilling().getScode().toLowerCase().contains(constraint)) {
                        filteredItems.add(sv);
                    }
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.count = stockViews.size();
                    result.values = stockViews;
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredStockViews = (ArrayList<StockView>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filteredStockViews.size(); i < l; i++) {
                add(filteredStockViews.get(i));
            }
            notifyDataSetChanged();
        }
    }
}

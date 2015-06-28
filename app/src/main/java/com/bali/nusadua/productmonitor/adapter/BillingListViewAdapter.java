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
import com.bali.nusadua.productmonitor.model.Billing;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by desu on 6/27/15.
 */
public class BillingListViewAdapter extends ArrayAdapter<Billing> implements Filterable {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Billing> billingViews;
    private List<Billing> filteredBillingViews;
    private ModelFilter filter;

    public BillingListViewAdapter(Activity activity, List<Billing> listBilling) {
        super(activity, R.layout.list_view_billing, listBilling);
        this.activity = activity;
        this.billingViews = new ArrayList<Billing>();
        this.billingViews.addAll(listBilling);
        this.filteredBillingViews = new ArrayList<Billing>();
        this.filteredBillingViews.addAll(this.billingViews);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        Billing billing = filteredBillingViews.get(position);
        ViewHolder viewHolder = null;

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_view_billing, null);
            viewHolder = new ViewHolder();
            viewHolder.billing_icon = (TextView) view.findViewById(R.id.billing_icon);
            viewHolder.billing_invoice_no = (TextView) view.findViewById(R.id.billing_invoice_no);
            viewHolder.billing_total_amount = (TextView) view.findViewById(R.id.billing_total_amount);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = ((ViewHolder) view.getTag());
        }

        NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);

        viewHolder.billing_icon.setText(billing.getInvoiceNo().toUpperCase().substring(0, 1));
        viewHolder.billing_invoice_no.setText(billing.getInvoiceNo());
        viewHolder.billing_total_amount.setText(parent.getResources().getString(R.string.currency_symbol) + " " + format.format(billing.getTotalAmount()).toString());

        return view;
    }

    static class ViewHolder {
        protected TextView billing_icon;
        protected TextView billing_invoice_no;
        protected TextView billing_total_amount;
    }

    private class ModelFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<Billing> filteredItems = new ArrayList<Billing>();

                for (int i = 0, l = billingViews.size(); i < l; i++) {
                    Billing billing = billingViews.get(i);
                    if (billing.getInvoiceNo().toLowerCase().contains(constraint) ||
                            billing.getTotalAmount().toString().contains(constraint)) {
                        filteredItems.add(billing);
                    }
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.count = billingViews.size();
                    result.values = billingViews;
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredBillingViews = (ArrayList<Billing>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filteredBillingViews.size(); i < l; i++) {
                add(filteredBillingViews.get(i));
            }
            notifyDataSetChanged();
        }
    }
}

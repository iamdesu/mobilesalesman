package com.bali.nusadua.productmonitor;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bali.nusadua.productmonitor.model.Customer;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by desu on 6/28/15.
 */
public class CustomerListViewAdapter extends ArrayAdapter<Customer> implements Filterable {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Customer> customers;
    private List<Customer> filteredCustomers;
    private ModelFilter filter;

    public CustomerListViewAdapter(Activity activity, List<Customer> listCustomer) {
        super(activity, R.layout.list_view_stock, listCustomer);
        this.activity = activity;
        this.customers = new ArrayList<Customer>();
        this.customers.addAll(listCustomer);
        this.filteredCustomers = new ArrayList<Customer>();
        this.filteredCustomers.addAll(this.customers);
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

        Customer cs = filteredCustomers.get(position);
        ViewHolder viewHolder = null;

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_view_outlet, null);
            viewHolder = new ViewHolder();
            viewHolder.outlet_icon = (TextView) view.findViewById(R.id.outlet_icon);
            viewHolder.company_name = (TextView) view.findViewById(R.id.company_name);
            viewHolder.address_region = (TextView) view.findViewById(R.id.address_region);
            viewHolder.person = (TextView) view.findViewById(R.id.person);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = ((ViewHolder) view.getTag());
        }

        viewHolder.outlet_icon.setText(cs.getCompanyName().toUpperCase().substring(0, 1));
        viewHolder.company_name.setText(cs.getCompanyName());
        viewHolder.address_region.setText(cs.getAddress() + " " + cs.getRegion() + " " + cs.getCity());
        viewHolder.person.setText(cs.getPersonName());

        return view;
    }

    static class ViewHolder {
        protected TextView outlet_icon;
        protected TextView company_name;
        protected TextView address_region;
        protected TextView person;
    }

    private class ModelFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<Customer> filteredItems = new ArrayList<Customer>();

                for (int i = 0, l = customers.size(); i < l; i++) {
                    Customer cs = customers.get(i);
                    if (cs.getCompanyName().toLowerCase().contains(constraint) ||
                            (cs.getAddress() != null ? cs.getAddress().toLowerCase().contains(constraint) : true) ||
                            (cs.getRegion() != null ? cs.getRegion().toLowerCase().contains(constraint) : true) ||
                            (cs.getCity() != null ? cs.getCity().toLowerCase().contains(constraint) : true)) {
                        filteredItems.add(cs);
                    }
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.count = customers.size();
                    result.values = customers;
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredCustomers = (ArrayList<Customer>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filteredCustomers.size(); i < l; i++) {
                add(filteredCustomers.get(i));
            }
            notifyDataSetChanged();
        }
    }
}

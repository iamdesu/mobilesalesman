package com.bali.nusadua.productmonitor.adapter;

import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bali.nusadua.productmonitor.R;
import com.bali.nusadua.productmonitor.model.Billing;

import java.util.List;

/**
 * Created by desu sudarsana on 6/7/2015.
 */
public class BillingRecycleViewAdapter extends RecyclerView.Adapter<BillingRecycleViewAdapter.BillingViewHolder> {

    List<Billing> billings;
    Resources resources;

    public BillingRecycleViewAdapter(List<Billing> billings) {
        this.billings = billings;
    }

    @Override
    public int getItemCount() {
        return billings.size();
    }

    @Override
    public void onBindViewHolder(BillingViewHolder billingViewHolder, int i) {
        Billing billing = billings.get(i);
        billingViewHolder.invoiceNo.setText(resources.getString(R.string.invoice_no) + " " + billing.getInvoiceNo());
        billingViewHolder.totalAmount.setText(resources.getString(R.string.currency_symbol) + " " + String.valueOf(billing.getTotalAmount()));
    }

    @Override
    public BillingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_billing, viewGroup, false);
        BillingViewHolder bvh = new BillingViewHolder(v);
        resources = v.getResources();
        return bvh;
    }

    public static class BillingViewHolder extends RecyclerView.ViewHolder {
        CardView cvBilling;
        TextView totalAmount;
        TextView invoiceNo;

        public BillingViewHolder(View itemView){
            super(itemView);
            cvBilling = (CardView) itemView.findViewById(R.id.cvBilling);
            totalAmount = (TextView)itemView.findViewById(R.id.billing_total_amount);
            invoiceNo = (TextView)itemView.findViewById(R.id.billing_invoice_no);
        }

    }

}

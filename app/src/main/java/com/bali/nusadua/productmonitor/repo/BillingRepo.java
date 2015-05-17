package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bali.nusadua.productmonitor.model.Billing;
import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

public class BillingRepo {
    private DBHelper dbHelper;

    public BillingRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(Billing billing) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Billing.INVOICE_NO, billing.getInvoiceNo());
        values.put(Billing.CUST_ID, billing.getCustomerId());
        values.put(Billing.TOTAL_AMOUNT, billing.getTotalAmount());
        values.put(Billing.PAID_AMOUNT, billing.getPaidAmount());

        //Inserting row
        long id = db.insert(Billing.TABLE, null, values);
        db.close();
        return (int) id;
    }
}

package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bali.nusadua.productmonitor.model.Billing;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

import java.util.ArrayList;
import java.util.List;

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

    public List<Billing> getBillingByCustomer(String customerId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                Billing.ID + ", " +
                Billing.INVOICE_NO + ", " +
                Billing.CUST_ID + ", " +
                Billing.TOTAL_AMOUNT + ", " +
                Billing.PAID_AMOUNT + " FROM " +
                Billing.TABLE + " WHERE " +
                Billing.CUST_ID + " = ?";

        List<Billing> billings = new ArrayList<Billing>();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{customerId});

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Billing billing = new Billing();
                billing.setId(cursor.getInt(cursor.getColumnIndex(Billing.ID)));
                billing.setInvoiceNo(cursor.getString(cursor.getColumnIndex(Billing.INVOICE_NO)));
                billing.setCustomerId(cursor.getString(cursor.getColumnIndex(Billing.CUST_ID)));
                billing.setTotalAmount(cursor.getDouble(cursor.getColumnIndex(Billing.TOTAL_AMOUNT)));
                billing.setPaidAmount(cursor.getDouble(cursor.getColumnIndex(Billing.PAID_AMOUNT)));

                billings.add(billing);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return billings;
    }

    public Billing getBillingByInvoiceNo(String invoiceNo) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                Billing.ID + ", " +
                Billing.INVOICE_NO + ", " +
                Billing.CUST_ID + ", " +
                Billing.TOTAL_AMOUNT + ", " +
                Billing.PAID_AMOUNT + " FROM " +
                Billing.TABLE + " WHERE " +
                Billing.INVOICE_NO + " = ?";

        Billing billing = null;
        Cursor cursor = db.rawQuery(selectQuery, new String[]{invoiceNo});

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                billing = new Billing();
                billing.setId(cursor.getInt(cursor.getColumnIndex(Billing.ID)));
                billing.setInvoiceNo(cursor.getString(cursor.getColumnIndex(Billing.INVOICE_NO)));
                billing.setCustomerId(cursor.getString(cursor.getColumnIndex(Billing.CUST_ID)));
                billing.setTotalAmount(cursor.getDouble(cursor.getColumnIndex(Billing.TOTAL_AMOUNT)));
                billing.setPaidAmount(cursor.getDouble(cursor.getColumnIndex(Billing.PAID_AMOUNT)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return billing;
    }

    public Billing getBillingByCustoMerInvoiceNo(String customerID, String invoiceNo) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                Billing.ID + ", " +
                Billing.INVOICE_NO + ", " +
                Billing.CUST_ID + ", " +
                Billing.TOTAL_AMOUNT + ", " +
                Billing.PAID_AMOUNT + " FROM " +
                Billing.TABLE + " WHERE " +
                Billing.CUST_ID + " = ? AND " +
                Billing.INVOICE_NO + " = ?";

        Billing billing = null;
        Cursor cursor = db.rawQuery(selectQuery, new String[]{customerID, invoiceNo});

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                billing = new Billing();
                billing.setId(cursor.getInt(cursor.getColumnIndex(Billing.ID)));
                billing.setInvoiceNo(cursor.getString(cursor.getColumnIndex(Billing.INVOICE_NO)));
                billing.setCustomerId(cursor.getString(cursor.getColumnIndex(Billing.CUST_ID)));
                billing.setTotalAmount(cursor.getDouble(cursor.getColumnIndex(Billing.TOTAL_AMOUNT)));
                billing.setPaidAmount(cursor.getDouble(cursor.getColumnIndex(Billing.PAID_AMOUNT)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return billing;
    }
}

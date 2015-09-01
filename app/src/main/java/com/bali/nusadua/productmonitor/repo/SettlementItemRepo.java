package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bali.nusadua.productmonitor.model.Outlet;
import com.bali.nusadua.productmonitor.model.SettlementItem;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SettlementItemRepo {
    private DBHelper dbHelper;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    public SettlementItemRepo(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    public int insert(SettlementItem settlementItem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        settlementItem.setGuid(UUID.randomUUID().toString());
        values.put(SettlementItem.GUID, settlementItem.getGuid());
        values.put(SettlementItem.SETTLEMENT_HEADER_ID, settlementItem.getSettlementHeaderId());
        values.put(SettlementItem.INVOICE_NUMBER, settlementItem.getInvoiceNumber());
        values.put(SettlementItem.INVOICE_DATE, sdf.format(settlementItem.getInvoiceDate()));
        values.put(SettlementItem.CREDIT, settlementItem.getCredit());
        values.put(SettlementItem.PAYMENT_METHOD, settlementItem.getPaymentMethod());
        values.put(SettlementItem.NOMINAL_PAYMENT, settlementItem.getNominalPayment());
        values.put(SettlementItem.CREATE_DATE, sdf.format(new Date()));

        //Inserting row
        long settlement_id = db.insert(SettlementItem.TABLE, null, values);
        db.close();
        return (int) settlement_id;
    }

    public void insertAll(List<SettlementItem> settlementItems) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (SettlementItem settlementItem : settlementItems) {
            ContentValues contentValues = new ContentValues();

            settlementItem.setGuid(UUID.randomUUID().toString());
            contentValues.put(SettlementItem.GUID, settlementItem.getGuid());
            contentValues.put(SettlementItem.SETTLEMENT_HEADER_ID, settlementItem.getSettlementHeaderId());
            contentValues.put(SettlementItem.INVOICE_NUMBER, settlementItem.getInvoiceNumber());
            contentValues.put(SettlementItem.INVOICE_DATE, sdf.format(settlementItem.getInvoiceDate()));
            contentValues.put(SettlementItem.CREDIT, settlementItem.getCredit());
            contentValues.put(SettlementItem.PAYMENT_METHOD, settlementItem.getPaymentMethod());
            contentValues.put(SettlementItem.NOMINAL_PAYMENT, settlementItem.getNominalPayment());
            contentValues.put(SettlementItem.CREATE_DATE, sdf.format(new Date()));

            db.insert(SettlementItem.TABLE, null, contentValues);
        }
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(SettlementItem.TABLE, null, null);
        db.close();
    }

    public void update(SettlementItem settlementItem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SettlementItem.INVOICE_NUMBER, settlementItem.getInvoiceNumber());
        contentValues.put(SettlementItem.INVOICE_DATE, sdf.format(settlementItem.getInvoiceDate()));
        contentValues.put(SettlementItem.CREDIT, settlementItem.getCredit());
        contentValues.put(SettlementItem.PAYMENT_METHOD, settlementItem.getPaymentMethod());
        contentValues.put(SettlementItem.NOMINAL_PAYMENT, settlementItem.getNominalPayment());

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(SettlementItem.TABLE, contentValues, SettlementItem.GUID + "= ?", new String[]{settlementItem.getGuid()});
        db.close();
    }

    public SettlementItem findByGUID(String guid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                SettlementItem.ID + ", " +
                SettlementItem.GUID + ", " +
                SettlementItem.SETTLEMENT_HEADER_ID + ", " +
                SettlementItem.INVOICE_NUMBER + ", " +
                SettlementItem.INVOICE_DATE + ", " +
                SettlementItem.CREDIT + ", " +
                SettlementItem.PAYMENT_METHOD + ", " +
                SettlementItem.NOMINAL_PAYMENT + ", " +
                SettlementItem.CREATE_DATE + " FROM " +
                SettlementItem.TABLE + " WHERE " +
                SettlementItem.GUID + " = ?";

        SettlementItem settlementItem = new SettlementItem();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{guid});

        if (cursor.moveToFirst()) {
            do {
                settlementItem.setId(cursor.getInt(cursor.getColumnIndex(SettlementItem.ID)));
                settlementItem.setGuid(cursor.getString(cursor.getColumnIndex(SettlementItem.GUID)));
                settlementItem.setSettlementHeaderId(cursor.getInt(cursor.getColumnIndex(SettlementItem.SETTLEMENT_HEADER_ID)));
                settlementItem.setInvoiceNumber(cursor.getString(cursor.getColumnIndex(SettlementItem.INVOICE_NUMBER)));
                try {
                    Date invoiceDate = sdf.parse(cursor.getString(cursor.getColumnIndex(SettlementItem.INVOICE_DATE)));
                    settlementItem.setInvoiceDate(invoiceDate);
                } catch (ParseException e) {
                    settlementItem.setInvoiceDate(null);
                }
                settlementItem.setCredit(cursor.getLong(cursor.getColumnIndex(SettlementItem.CREDIT)));
                settlementItem.setPaymentMethod(cursor.getString(cursor.getColumnIndex(SettlementItem.PAYMENT_METHOD)));
                settlementItem.setNominalPayment(cursor.getLong(cursor.getColumnIndex(SettlementItem.NOMINAL_PAYMENT)));

                try {
                    Date createdDate = sdf.parse(cursor.getString(cursor.getColumnIndex(SettlementItem.CREATE_DATE)));
                    settlementItem.setCreatedDate(createdDate);
                } catch (ParseException e) {
                    settlementItem.setCreatedDate(null);
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return settlementItem;
    }

    /**
     * Retrieve all records and populate List<ReturItem>
     */
    public List<SettlementItem> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + SettlementItem.TABLE;

        List<SettlementItem> settlementItems = new ArrayList<SettlementItem>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list  (cursor.getColumnIndex(Team.ID
        if (cursor.moveToFirst()) {
            do {
                SettlementItem settlementItem = new SettlementItem();
                settlementItem.setId((int) cursor.getLong(cursor.getColumnIndex(SettlementItem.ID)));
                settlementItem.setGuid(cursor.getString(cursor.getColumnIndex(SettlementItem.GUID)));
                settlementItem.setSettlementHeaderId(cursor.getInt(cursor.getColumnIndex(SettlementItem.SETTLEMENT_HEADER_ID)));
                settlementItem.setInvoiceNumber(cursor.getString(cursor.getColumnIndex(SettlementItem.INVOICE_NUMBER)));
                try {
                    Date invoiceDate = sdf.parse(cursor.getString(cursor.getColumnIndex(SettlementItem.INVOICE_DATE)));
                    settlementItem.setInvoiceDate(invoiceDate);
                } catch (ParseException e) {
                    settlementItem.setInvoiceDate(null);
                }
                settlementItem.setCredit(cursor.getLong(cursor.getColumnIndex(SettlementItem.CREDIT)));
                settlementItem.setPaymentMethod(cursor.getString(cursor.getColumnIndex(SettlementItem.PAYMENT_METHOD)));
                settlementItem.setNominalPayment(cursor.getLong(cursor.getColumnIndex(SettlementItem.NOMINAL_PAYMENT)));

                try {
                    Date createDate = sdf.parse(cursor.getString(cursor.getColumnIndex(SettlementItem.CREATE_DATE)));
                    settlementItem.setCreatedDate(createDate);
                } catch (ParseException e) {
                    settlementItem.setCreatedDate(null);
                }

                settlementItems.add(settlementItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return settlementItems;
    }
}

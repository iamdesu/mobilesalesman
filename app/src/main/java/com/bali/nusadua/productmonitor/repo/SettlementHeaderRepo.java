package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bali.nusadua.productmonitor.model.SettlementHeader;
import com.bali.nusadua.productmonitor.model.SettlementItem;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by desu on 8/30/15.
 */
public class SettlementHeaderRepo {
    private DBHelper dbHelper;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    public SettlementHeaderRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public List<String> getCustomerOnSettlementHeader() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                SettlementHeader.KODE_OUTLET + " FROM " +
                SettlementHeader.TABLE;

        List<String> listCustomerID = new ArrayList<String>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String customerID = cursor.getString(cursor.getColumnIndex(SettlementHeader.KODE_OUTLET));
                listCustomerID.add(customerID);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listCustomerID;
    }

    public List<SettlementHeader> getSettlementHeaderItemByCustomer(String kodeOutlet) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT ");
        selectQuery.append(SettlementHeader.TABLE + "." + SettlementHeader.ID + " as " + SettlementHeader.TABLE + SettlementHeader.ID + ", ");
        selectQuery.append(SettlementHeader.TABLE + "." + SettlementHeader.KODE_OUTLET + " as " + SettlementHeader.TABLE + SettlementHeader.KODE_OUTLET + ", ");
        selectQuery.append(SettlementHeader.TABLE + "." + SettlementHeader.UPDATE_DATE + " as " + SettlementHeader.TABLE + SettlementHeader.UPDATE_DATE + ", ");
        selectQuery.append(SettlementHeader.TABLE + "." + SettlementHeader.CREATE_DATE + " as " + SettlementHeader.TABLE + SettlementHeader.CREATE_DATE + " ");
        selectQuery.append("FROM " + SettlementHeader.TABLE + " ");
        selectQuery.append("WHERE " + SettlementHeader.TABLE + "." + SettlementHeader.KODE_OUTLET + " = ? ");
        selectQuery.append("ORDER BY " + SettlementHeader.TABLE + "." + SettlementHeader.ID + " ASC");

        Log.i("getSetlementHeaderItem:", selectQuery.toString());

        List<SettlementHeader> settlementHeaders = new ArrayList<SettlementHeader>();
        Cursor cursor = db.rawQuery(selectQuery.toString(), new String[]{kodeOutlet});

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SettlementHeader settlementHeader = new SettlementHeader();
                settlementHeader.setId(cursor.getInt(cursor.getColumnIndex(SettlementHeader.TABLE + SettlementHeader.ID)));
                settlementHeader.setKodeOutlet(cursor.getString(cursor.getColumnIndex(SettlementHeader.TABLE + SettlementHeader.KODE_OUTLET)));
                settlementHeader.setUpdateDate(getDateFromCursor(cursor, SettlementHeader.TABLE + SettlementHeader.UPDATE_DATE));
                settlementHeader.setCreateDate(getDateFromCursor(cursor, SettlementHeader.TABLE + SettlementHeader.CREATE_DATE));

                settlementHeader.setSettlementItems(searchSettlementItemByHeaderID(settlementHeader.getId()));
                settlementHeaders.add(settlementHeader);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return settlementHeaders;
    }

    public int insert(SettlementHeader settlementHeader) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(settlementHeader.KODE_OUTLET, settlementHeader.getKodeOutlet());
        values.put(settlementHeader.UPDATE_DATE, "");
        values.put(settlementHeader.CREATE_DATE, sdf.format(new Date()));

        //Inserting row
        long order_header_id = db.insert(SettlementHeader.TABLE, null, values);
        db.close();
        return (int) order_header_id;
    }

    private Date getDateFromCursor(Cursor cursor, String columnIndex) {
        try {
            Date createDate = sdf.parse(cursor.getString(cursor.getColumnIndex(columnIndex)));
            return createDate;
        } catch (ParseException e) {
            return null;
        }
    }

    public Integer getCountByCustomer(String kodeOutlet) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT COUNT (*) FROM " + SettlementHeader.TABLE + " WHERE " + SettlementHeader.KODE_OUTLET + " = ? ";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{kodeOutlet});
        Integer count = 0;

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }

    private List<SettlementItem> searchSettlementItemByHeaderID(int orderHeaderId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder selectItemQuery = new StringBuilder();
        selectItemQuery.append("SELECT ");
        selectItemQuery.append(SettlementItem.TABLE + "." + SettlementItem.ID + " as " + SettlementItem.TABLE + SettlementItem.ID + ", ");
        selectItemQuery.append(SettlementItem.TABLE + "." + SettlementItem.GUID + " as " + SettlementItem.TABLE + SettlementItem.GUID + ", ");
        selectItemQuery.append(SettlementItem.TABLE + "." + SettlementItem.SETTLEMENT_HEADER_ID + " as " + SettlementItem.TABLE + SettlementItem.SETTLEMENT_HEADER_ID + ", ");
        selectItemQuery.append(SettlementItem.TABLE + "." + SettlementItem.INVOICE_NUMBER + " as " + SettlementItem.TABLE + SettlementItem.INVOICE_NUMBER + ", ");
        selectItemQuery.append(SettlementItem.TABLE + "." + SettlementItem.INVOICE_DATE + " as " + SettlementItem.TABLE + SettlementItem.INVOICE_DATE + ", ");
        selectItemQuery.append(SettlementItem.TABLE + "." + SettlementItem.CREDIT + " as " + SettlementItem.TABLE + SettlementItem.CREDIT + ", ");
        selectItemQuery.append(SettlementItem.TABLE + "." + SettlementItem.PAYMENT_METHOD + " as " + SettlementItem.TABLE + SettlementItem.PAYMENT_METHOD + ", ");
        selectItemQuery.append(SettlementItem.TABLE + "." + SettlementItem.NOMINAL_PAYMENT + " as " + SettlementItem.TABLE + SettlementItem.NOMINAL_PAYMENT + ", ");
        selectItemQuery.append(SettlementItem.TABLE + "." + SettlementItem.CREATE_DATE + " as " + SettlementItem.TABLE + SettlementItem.CREATE_DATE + " ");
        selectItemQuery.append("FROM " + SettlementItem.TABLE + " ");
        selectItemQuery.append("WHERE " + SettlementItem.TABLE + "." + SettlementItem.SETTLEMENT_HEADER_ID + " = ? ");
        selectItemQuery.append("ORDER BY " + SettlementItem.TABLE + "." + SettlementItem.ID + " ASC");

        List<SettlementItem> settlementItems = new ArrayList<SettlementItem>();
        Cursor cursorItem = db.rawQuery(selectItemQuery.toString(), new String[]{String.valueOf(orderHeaderId)});

        if (cursorItem.moveToFirst()) {
            do {
                SettlementItem settlementItem = new SettlementItem();
                settlementItem.setId(cursorItem.getInt(cursorItem.getColumnIndex(SettlementItem.TABLE + SettlementItem.ID)));
                settlementItem.setGuid(cursorItem.getString(cursorItem.getColumnIndex(SettlementItem.TABLE + SettlementItem.GUID)));
                settlementItem.setSettlementHeaderId(cursorItem.getInt(cursorItem.getColumnIndex(SettlementItem.TABLE + SettlementItem.SETTLEMENT_HEADER_ID)));
                settlementItem.setInvoiceNumber(cursorItem.getString(cursorItem.getColumnIndex(SettlementItem.TABLE + SettlementItem.INVOICE_NUMBER)));
                settlementItem.setInvoiceDate(getDateFromCursor(cursorItem, SettlementItem.TABLE + SettlementItem.INVOICE_DATE));
                settlementItem.setCredit(cursorItem.getLong(cursorItem.getColumnIndex(SettlementItem.TABLE + SettlementItem.CREDIT)));
                settlementItem.setNominalPayment(cursorItem.getLong(cursorItem.getColumnIndex(SettlementItem.TABLE + SettlementItem.NOMINAL_PAYMENT)));
                settlementItem.setCreatedDate(getDateFromCursor(cursorItem, SettlementItem.TABLE + SettlementItem.CREATE_DATE));

                settlementItems.add(settlementItem);
            } while (cursorItem.moveToNext());
        }

        return settlementItems;
    }
}

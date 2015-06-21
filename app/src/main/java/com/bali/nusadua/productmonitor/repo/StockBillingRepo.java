package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bali.nusadua.productmonitor.model.StockBilling;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class StockBillingRepo {
    private DBHelper dbHelper;

    public StockBillingRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(StockBilling stockBilling) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(StockBilling.STOCK_ID, stockBilling.getStockId());
        values.put(StockBilling.SCODE, stockBilling.getScode());
        values.put(StockBilling.DESCRIPTION, stockBilling.getDescription());

        //Inserting row
        long id = db.insert(StockBilling.TABLE, null, values);
        db.close();

        return (int) id;
    }

    //Retrieve all records and populate List<StockBilling>
    public List<StockBilling> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + StockBilling.TABLE + " order by " + StockBilling.DESCRIPTION + ", " + StockBilling.SCODE;

        List<StockBilling> listStockBilling = new ArrayList<StockBilling>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                StockBilling stockBilling = new StockBilling();
                stockBilling.setId((int) cursor.getLong(cursor.getColumnIndex(StockBilling.ID)));
                stockBilling.setStockId(cursor.getString(cursor.getColumnIndex(StockBilling.STOCK_ID)));
                stockBilling.setScode(cursor.getString(cursor.getColumnIndex(StockBilling.SCODE)));
                stockBilling.setDescription(cursor.getString(cursor.getColumnIndex(StockBilling.DESCRIPTION)));

                listStockBilling.add(stockBilling);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listStockBilling;
    }

    public List<StockBilling> getStockBillings() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //String selectQuery = "SELECT * FROM " + StockBilling.TABLE + " order by " + StockBilling.DESCRIPTION + ", " + StockBilling.SCODE;
        String selectQuery = "SELECT " +
                StockBilling.ID + ", " +
                StockBilling.STOCK_ID + ", " +
                StockBilling.SCODE + ", " +
                StockBilling.DESCRIPTION + " FROM " +
                StockBilling.TABLE + " WHERE " +
                StockBilling.SCODE + " IS NOT NULL AND " +
                StockBilling.SCODE + " <> ? " +
                "ORDER BY " + StockBilling.SCODE + ", " + StockBilling.DESCRIPTION;

        List<StockBilling> listStockBilling = new ArrayList<StockBilling>();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{""});

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                StockBilling stockBilling = new StockBilling();
                stockBilling.setId((int) cursor.getLong(cursor.getColumnIndex(StockBilling.ID)));
                stockBilling.setStockId(cursor.getString(cursor.getColumnIndex(StockBilling.STOCK_ID)));
                stockBilling.setScode(cursor.getString(cursor.getColumnIndex(StockBilling.SCODE)));
                stockBilling.setDescription(cursor.getString(cursor.getColumnIndex(StockBilling.DESCRIPTION)));

                listStockBilling.add(stockBilling);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listStockBilling;
    }

    public StockBilling getByStockId(String stockId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                StockBilling.ID + ", " +
                StockBilling.STOCK_ID + ", " +
                StockBilling.SCODE + ", " +
                StockBilling.DESCRIPTION + " FROM " +
                StockBilling.TABLE + " WHERE " +
                StockBilling.STOCK_ID + " = ?";

        StockBilling stockBilling = null;
        Cursor cursor = db.rawQuery(selectQuery, new String[]{stockId});

        if (cursor.moveToFirst()) {
            do {
                stockBilling = new StockBilling();
                stockBilling.setId(cursor.getInt(cursor.getColumnIndex(StockBilling.ID)));
                stockBilling.setStockId(cursor.getString(cursor.getColumnIndex(StockBilling.STOCK_ID)));
                stockBilling.setScode(cursor.getString(cursor.getColumnIndex(StockBilling.SCODE)));
                stockBilling.setDescription(cursor.getString(cursor.getColumnIndex(StockBilling.DESCRIPTION)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return stockBilling;
    }
}

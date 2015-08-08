package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.model.StockBilling;
import com.bali.nusadua.productmonitor.model.StockPrice;
import com.bali.nusadua.productmonitor.modelView.StockView;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class StockPriceRepo {
    private DBHelper dbHelper;

    public StockPriceRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(StockPrice stockPrice) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(StockPrice.STOCK_ID, stockPrice.getStockId());
        values.put(StockPrice.PRICE_LEVEL, stockPrice.getPriceLevel());
        values.put(StockPrice.PRICE, stockPrice.getPrice());

        //Inserting row
        long id = db.insert(StockPrice.TABLE, null, values);
        db.close();

        return (int) id;
    }

    //Retrieve all records and populate List<Order>
    public List<StockPrice> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + StockPrice.TABLE;

        List<StockPrice> listStockPrice = new ArrayList<StockPrice>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list  (cursor.getColumnIndex(Team.ID
        if (cursor.moveToFirst()) {
            do {
                StockPrice stockPrice = new StockPrice();
                stockPrice.setId((int) cursor.getLong(cursor.getColumnIndex(StockPrice.ID)));
                stockPrice.setStockId(cursor.getString(cursor.getColumnIndex(StockPrice.STOCK_ID)));
                stockPrice.setPriceLevel(cursor.getInt(cursor.getColumnIndex(StockPrice.PRICE_LEVEL)));
                stockPrice.setPrice(cursor.getDouble(cursor.getColumnIndex(StockPrice.PRICE)));

                listStockPrice.add(stockPrice);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listStockPrice;
    }

    public List<StockView> getStockByCustomerLevel(Integer customerLevel) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT "
                + StockBilling.TABLE + "." + StockBilling.STOCK_ID + ", "
                + StockBilling.TABLE + "." + StockBilling.SCODE + ", "
                + StockBilling.TABLE + "." + StockBilling.DESCRIPTION + ", "
                + StockPrice.TABLE + "." + StockPrice.STOCK_ID + ", "
                + StockPrice.TABLE + "." + StockPrice.PRICE_LEVEL + ", "
                + StockPrice.TABLE + "." + StockPrice.PRICE
                + " FROM " + StockBilling.TABLE + " INNER JOIN " + StockPrice.TABLE
                + " ON "+ StockBilling.TABLE + "." + StockBilling.STOCK_ID + " = " + StockPrice.TABLE + "." + StockPrice.STOCK_ID
                + " WHERE " + StockPrice.TABLE + "." + StockPrice.PRICE_LEVEL + "= ? "
                + " ORDER BY " + StockBilling.TABLE + "." + StockBilling.SCODE + " ASC";

        List<StockView> listStock = new ArrayList<StockView>();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{ customerLevel.toString() } );

        //Looping through all rows and adding to list
        if(cursor.moveToFirst()){
            do {
                StockView stockView = new StockView();

                StockBilling stockBilling = new StockBilling();
                stockBilling.setStockId(cursor.getString(0));
                stockBilling.setScode(cursor.getString(1));
                stockBilling.setDescription(cursor.getString(2));

                StockPrice stockPrice = new StockPrice();
                stockPrice.setStockId(cursor.getString(3));
                stockPrice.setPriceLevel(cursor.getInt(4));
                stockPrice.setPrice(cursor.getDouble(5));

                stockView.setStockBilling(stockBilling);
                stockView.setStockPrice(stockPrice);

                listStock.add(stockView);
            } while (cursor.moveToNext());
        }

        return listStock;
    }
}

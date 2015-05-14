package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bali.nusadua.productmonitor.model.StockBilling;
import com.bali.nusadua.productmonitor.model.StockPrice;
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
}

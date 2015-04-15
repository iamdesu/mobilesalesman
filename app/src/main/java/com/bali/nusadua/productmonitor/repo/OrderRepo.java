package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bali.nusadua.productmonitor.model.Order;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

import java.util.UUID;

/**
 * Created by desu sudarsana on 4/15/2015.
 */
public class OrderRepo {
    private DBHelper dbHelper;

    public OrderRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(Order order) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        order.setGuid(UUID.randomUUID().toString());
        values.put(Order.GUID, order.getGuid());
        values.put(Order.KODE, order.getKode());
        values.put(Order.NAMA_BARANG, order.getNamaBarang());
        values.put(Order.HARGA, order.getHarga());
        values.put(Order.QTY, order.getQty());
        values.put(Order.UNIT, order.getUnit());

        //Inserting row
        long order_id = db.insert(Order.TABLE, null, values);
        db.close();
        return (int) order_id;
    }

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Order.TABLE, null, null);
        db.close();
    }

    public void update(Order order) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Order.KODE, order.getKode());
        values.put(Order.NAMA_BARANG, order.getNamaBarang());
        values.put(Order.HARGA, order.getHarga());
        values.put(Order.QTY, order.getQty());
        values.put(Order.UNIT, order.getUnit());

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(Order.TABLE, values, Order.GUID + "= ?", new String[] { order.getGuid() });
        db.close();
    }

    public Order findByGUID(String guid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                Order.ID + ", " +
                Order.GUID + ", " +
                Order.KODE + ", " +
                Order.NAMA_BARANG + ", " +
                Order.HARGA + ", " +
                Order.QTY + ", " +
                Order.UNIT + " FROM " +
                Order.TABLE + " WHERE " +
                Order.GUID + " = ?";

        Order order = new Order();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{ guid } );

        if(cursor.moveToFirst()) {
            do {
                order.setId(cursor.getInt(cursor.getColumnIndex(Order.ID)));
                order.setGuid(cursor.getString(cursor.getColumnIndex(Order.GUID)));
                order.setKode(cursor.getString(cursor.getColumnIndex(Order.KODE)));
                order.setNamaBarang(cursor.getString(cursor.getColumnIndex(Order.NAMA_BARANG)));
                order.setHarga(cursor.getInt(cursor.getColumnIndex(Order.HARGA)));
                order.setQty(cursor.getInt(cursor.getColumnIndex(Order.QTY)));
                order.setUnit(cursor.getString(cursor.getColumnIndex(Order.UNIT)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return order;
    }
}
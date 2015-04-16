package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bali.nusadua.productmonitor.model.Order;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

import java.util.ArrayList;
import java.util.List;
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

    public void insertAll(List<Order> orders){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int size = orders.size();
        for(int i = 0; i < size; i++){
            Order order = new Order();
            ContentValues contentValues = new ContentValues();
            order = orders.get(i);
            order.setGuid(UUID.randomUUID().toString());
            contentValues.put(Order.GUID, order.getGuid());
            contentValues.put(Order.KODE, order.getKode());
            contentValues.put(Order.GUID, order.getGuid());
            contentValues.put(Order.NAMA_BARANG, order.getNamaBarang());
            contentValues.put(Order.HARGA, order.getHarga());
            contentValues.put(Order.QTY, order.getQty());
            contentValues.put(Order.UNIT, order.getUnit());

            db.insert(Order.TABLE, null, contentValues);
        }
        db.close();
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

    //Retrieve all records and populate List<Order>
    public List<Order> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Order.TABLE;

        List<Order> listOrder = new ArrayList<Order>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list  (cursor.getColumnIndex(Team.ID
        if(cursor.moveToFirst()){
            do {
                Order order = new Order();
                order.setId((int) cursor.getLong(cursor.getColumnIndex(Order.ID)));
                order.setUnit(cursor.getString(cursor.getColumnIndex(Order.UNIT)));
                order.setHarga(cursor.getInt(cursor.getColumnIndex(Order.HARGA)));
                order.setNamaBarang(cursor.getString(cursor.getColumnIndex(Order.NAMA_BARANG)));
                order.setGuid(cursor.getString(cursor.getColumnIndex(Order.GUID)));
                order.setQty(cursor.getInt(cursor.getColumnIndex(Order.QTY)));
                order.setKode(cursor.getString(cursor.getColumnIndex(Order.KODE)));

                listOrder.add(order);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listOrder;
    }
}
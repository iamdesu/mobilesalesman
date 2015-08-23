package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bali.nusadua.productmonitor.model.OrderItem;
import com.bali.nusadua.productmonitor.model.Outlet;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OrderRepo {
    private DBHelper dbHelper;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    public OrderRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(OrderItem order) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        order.setGuid(UUID.randomUUID().toString());
        values.put(OrderItem.GUID, order.getGuid());
        values.put(OrderItem.ORDER_HEADER_GUID, order.getOrderHeaderGuid());
        values.put(OrderItem.KODE, order.getKode());
        values.put(OrderItem.NAMA_BARANG, order.getNamaBarang());
        values.put(OrderItem.HARGA, order.getHarga());
        values.put(OrderItem.QTY, order.getQty());
        values.put(OrderItem.UNIT, order.getUnit());
        values.put(OrderItem.CREATE_DATE, sdf.format(new Date()));

        //Inserting row
        long order_id = db.insert(OrderItem.TABLE, null, values);
        db.close();
        return (int) order_id;
    }

    public void insertAll(List<OrderItem> orders) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int size = orders.size();
        for (int i = 0; i < size; i++) {
            OrderItem order = new OrderItem();
            ContentValues contentValues = new ContentValues();
            order = orders.get(i);
            order.setGuid(UUID.randomUUID().toString());
            contentValues.put(OrderItem.GUID, order.getGuid());
            contentValues.put(OrderItem.KODE, order.getKode());
            contentValues.put(OrderItem.NAMA_BARANG, order.getNamaBarang());
            contentValues.put(OrderItem.HARGA, order.getHarga());
            contentValues.put(OrderItem.QTY, order.getQty());
            contentValues.put(OrderItem.UNIT, order.getUnit());
            contentValues.put(OrderItem.KODE_OUTLET, order.getKodeOutlet());
            contentValues.put(OrderItem.CREATE_DATE, sdf.format(new Date()));

            db.insert(OrderItem.TABLE, null, contentValues);
        }
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(OrderItem.TABLE, null, null);
        db.close();
    }

    public void update(OrderItem order) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OrderItem.KODE, order.getKode());
        values.put(OrderItem.NAMA_BARANG, order.getNamaBarang());
        values.put(OrderItem.HARGA, order.getHarga());
        values.put(OrderItem.QTY, order.getQty());
        values.put(OrderItem.UNIT, order.getUnit());
        values.put(OrderItem.KODE_OUTLET, order.getKodeOutlet());

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(OrderItem.TABLE, values, OrderItem.GUID + "= ?", new String[]{order.getGuid()});
        db.close();
    }

    public OrderItem findByGUID(String guid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                OrderItem.ID + ", " +
                OrderItem.GUID + ", " +
                OrderItem.KODE + ", " +
                OrderItem.NAMA_BARANG + ", " +
                OrderItem.HARGA + ", " +
                OrderItem.QTY + ", " +
                OrderItem.UNIT + ", " +
                OrderItem.KODE_OUTLET + ", " +
                OrderItem.CREATE_DATE + " FROM " +
                OrderItem.TABLE + " WHERE " +
                OrderItem.GUID + " = ?";

        OrderItem order = new OrderItem();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{guid});

        if (cursor.moveToFirst()) {
            do {
                order.setId(cursor.getInt(cursor.getColumnIndex(OrderItem.ID)));
                order.setGuid(cursor.getString(cursor.getColumnIndex(OrderItem.GUID)));
                order.setKode(cursor.getString(cursor.getColumnIndex(OrderItem.KODE)));
                order.setNamaBarang(cursor.getString(cursor.getColumnIndex(OrderItem.NAMA_BARANG)));
                order.setHarga(cursor.getDouble(cursor.getColumnIndex(OrderItem.HARGA)));
                order.setQty(cursor.getInt(cursor.getColumnIndex(OrderItem.QTY)));
                order.setUnit(cursor.getString(cursor.getColumnIndex(OrderItem.UNIT)));
                order.setKodeOutlet(cursor.getString(cursor.getColumnIndex(OrderItem.KODE_OUTLET)));

                try {
                    Date createDate = sdf.parse(cursor.getString(cursor.getColumnIndex(OrderItem.CREATE_DATE)));
                    order.setCreateDate(createDate);
                } catch (ParseException e) {
                    order.setCreateDate(null);
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return order;
    }

    //Retrieve all records and populate List<OrderItem>
    public List<OrderItem> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + OrderItem.TABLE;

        List<OrderItem> listOrder = new ArrayList<OrderItem>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list  (cursor.getColumnIndex(Team.ID
        if (cursor.moveToFirst()) {
            do {
                OrderItem order = new OrderItem();
                order.setId((int) cursor.getLong(cursor.getColumnIndex(OrderItem.ID)));
                order.setUnit(cursor.getString(cursor.getColumnIndex(OrderItem.UNIT)));
                order.setHarga(cursor.getDouble(cursor.getColumnIndex(OrderItem.HARGA)));
                order.setNamaBarang(cursor.getString(cursor.getColumnIndex(OrderItem.NAMA_BARANG)));
                order.setGuid(cursor.getString(cursor.getColumnIndex(OrderItem.GUID)));
                order.setQty(cursor.getInt(cursor.getColumnIndex(OrderItem.QTY)));
                order.setKode(cursor.getString(cursor.getColumnIndex(OrderItem.KODE)));
                order.setKodeOutlet(cursor.getString(cursor.getColumnIndex(OrderItem.KODE_OUTLET)));

                try {
                    Date createDate = sdf.parse(cursor.getString(cursor.getColumnIndex(OrderItem.CREATE_DATE)));
                    order.setCreateDate(createDate);
                } catch (ParseException e) {
                    order.setCreateDate(null);
                }

                listOrder.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listOrder;
    }

    public List<OrderItem> getOrderByCustomer(String customerId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                OrderItem.ID + ", " +
                OrderItem.GUID + ", " +
                OrderItem.KODE + ", " +
                OrderItem.NAMA_BARANG + ", " +
                OrderItem.HARGA + ", " +
                OrderItem.QTY + ", " +
                OrderItem.UNIT + ", " +
                OrderItem.KODE_OUTLET + ", " +
                OrderItem.CREATE_DATE + " FROM " +
                OrderItem.TABLE + " WHERE " +
                OrderItem.KODE_OUTLET + " = ?";

        List<OrderItem> listOrder = new ArrayList<OrderItem>();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{customerId});

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OrderItem order = new OrderItem();
                order.setId(cursor.getInt(cursor.getColumnIndex(OrderItem.ID)));
                order.setGuid(cursor.getString(cursor.getColumnIndex(OrderItem.GUID)));
                order.setKode(cursor.getString(cursor.getColumnIndex(OrderItem.KODE)));
                order.setNamaBarang(cursor.getString(cursor.getColumnIndex(OrderItem.NAMA_BARANG)));
                order.setHarga(cursor.getDouble(cursor.getColumnIndex(OrderItem.HARGA)));
                order.setQty(cursor.getInt(cursor.getColumnIndex(OrderItem.QTY)));
                order.setUnit(cursor.getString(cursor.getColumnIndex(OrderItem.UNIT)));
                order.setKodeOutlet(cursor.getString(cursor.getColumnIndex(OrderItem.KODE_OUTLET)));

                try {
                    Date createDate = sdf.parse(cursor.getString(cursor.getColumnIndex(OrderItem.CREATE_DATE)));
                    order.setCreateDate(createDate);
                } catch (ParseException e) {
                    order.setCreateDate(null);
                }

                listOrder.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listOrder;
    }

    /*public List<OrderItem> getAllWithOutlet() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //String selectQuery = "SELECT * FROM " + OrderItem.TABLE + " order join " + Outlet.TABLE + " outlet ON order.kode_outlet = outlet.kode";
        String selectQuery = "SELECT " +
                OrderItem.TABLE + "." + OrderItem.ID + ", " +
                OrderItem.TABLE + "." + OrderItem.GUID + ", " +
                OrderItem.TABLE + "." + OrderItem.KODE + ", " +
                OrderItem.TABLE + "." + OrderItem.NAMA_BARANG + ", " +
                OrderItem.TABLE + "." + OrderItem.HARGA + ", " +
                OrderItem.TABLE + "." + OrderItem.QTY + ", " +
                OrderItem.TABLE + "." + OrderItem.UNIT + ", " +
                OrderItem.TABLE + "." + OrderItem.KODE_OUTLET + ", " +
                OrderItem.TABLE + "." + OrderItem.CREATE_DATE + ", " +
                Outlet.TABLE + "." + Outlet.ID + " as outletID, " +
                Outlet.TABLE + "." + Outlet.GUID + " as outletGUID, " +
                Outlet.TABLE + "." + Outlet.KODE + " as outletKode, " +
                Outlet.TABLE + "." + Outlet.NAME + " as outletName" +
                " FROM " + OrderItem.TABLE + " LEFT JOIN " + Outlet.TABLE +
                " ON " + OrderItem.TABLE + "." + OrderItem.KODE_OUTLET + " = " + Outlet.TABLE + "." + Outlet.KODE;

        List<OrderItem> listOrder = new ArrayList<OrderItem>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OrderItem order = new OrderItem();
                order.setId((int) cursor.getLong(cursor.getColumnIndex(OrderItem.ID)));
                order.setUnit(cursor.getString(cursor.getColumnIndex(OrderItem.UNIT)));
                order.setHarga(cursor.getDouble(cursor.getColumnIndex(OrderItem.HARGA)));
                order.setNamaBarang(cursor.getString(cursor.getColumnIndex(OrderItem.NAMA_BARANG)));
                order.setGuid(cursor.getString(cursor.getColumnIndex(OrderItem.GUID)));
                order.setQty(cursor.getInt(cursor.getColumnIndex(OrderItem.QTY)));
                order.setKode(cursor.getString(cursor.getColumnIndex(OrderItem.KODE)));
                order.setKodeOutlet(cursor.getString(cursor.getColumnIndex(OrderItem.KODE_OUTLET)));

                try {
                    Date createDate = sdf.parse(cursor.getString(cursor.getColumnIndex(OrderItem.CREATE_DATE)));
                    order.setCreateDate(createDate);
                } catch (ParseException e) {
                    order.setCreateDate(null);
                }

                Outlet outlet = new Outlet();
                outlet.setId((int) cursor.getLong(cursor.getColumnIndex("outletID")));
                outlet.setGuid(cursor.getString(cursor.getColumnIndex("outletGUID")));
                outlet.setKode(cursor.getString(cursor.getColumnIndex("outletKode")));
                outlet.setName(cursor.getString(cursor.getColumnIndex("outletName")));

                order.setOutlet(outlet);

                listOrder.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listOrder;
    }*/

    /*public List<String> getCustomerOnOrder() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT DISTINCT " +
                OrderItem.KODE_OUTLET + " FROM " +
                OrderItem.TABLE;

        List<String> listCustomerID = new ArrayList<String>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String customerID = cursor.getString(cursor.getColumnIndex(OrderItem.KODE_OUTLET));
                listCustomerID.add(customerID);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listCustomerID;
    }*/
}
package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bali.nusadua.productmonitor.model.OrderHeader;
import com.bali.nusadua.productmonitor.model.OrderItem;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by desu on 8/23/15.
 */
public class OrderHeaderRepo {

    private DBHelper dbHelper;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    public OrderHeaderRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public List<String> getCustomerOnOrderHeader() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                OrderHeader.KODE_OUTLET + " FROM " +
                OrderHeader.TABLE;

        List<String> listCustomerID = new ArrayList<String>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String customerID = cursor.getString(cursor.getColumnIndex(OrderHeader.KODE_OUTLET));
                listCustomerID.add(customerID);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listCustomerID;
    }

    public List<OrderHeader> getOrderHeaderItemByCustomer(String kodeOutlet) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT ");
        selectQuery.append(OrderHeader.TABLE + "." + OrderHeader.ID + " as " + OrderHeader.TABLE + OrderHeader.ID + ", ");
        selectQuery.append(OrderHeader.TABLE + "." + OrderHeader.KODE_OUTLET + " as " + OrderHeader.TABLE + OrderHeader.KODE_OUTLET + ", ");
        selectQuery.append(OrderHeader.TABLE + "." + OrderHeader.UPDATE_DATE + " as " + OrderHeader.TABLE + OrderHeader.UPDATE_DATE + ", ");
        selectQuery.append(OrderHeader.TABLE + "." + OrderHeader.CREATE_DATE + " as " + OrderHeader.TABLE + OrderHeader.CREATE_DATE + " ");
        selectQuery.append("FROM " + OrderHeader.TABLE + " ");
        selectQuery.append("WHERE " + OrderHeader.TABLE + "." + OrderHeader.KODE_OUTLET + " = ? ");
        selectQuery.append("ORDER BY " + OrderHeader.TABLE + "." + OrderHeader.ID + " ASC");

        Log.i("getOrderHeaderItem : ", selectQuery.toString());

        List<OrderHeader> orderHeaders = new ArrayList<OrderHeader>();
        Cursor cursor = db.rawQuery(selectQuery.toString(), new String[]{kodeOutlet});

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OrderHeader orderHeader = new OrderHeader();
                orderHeader.setId(cursor.getInt(cursor.getColumnIndex(OrderHeader.TABLE + OrderHeader.ID)));
                orderHeader.setKodeOutlet(cursor.getString(cursor.getColumnIndex(OrderHeader.TABLE + OrderHeader.KODE_OUTLET)));
                orderHeader.setUpdateDate(getDateFromCursor(cursor, OrderHeader.TABLE + OrderHeader.UPDATE_DATE));
                orderHeader.setCreateDate(getDateFromCursor(cursor, OrderHeader.TABLE + OrderHeader.CREATE_DATE));

                orderHeader.setOrderItems(searchOrderItemByHeaderID(orderHeader.getId()));
                orderHeaders.add(orderHeader);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return orderHeaders;
    }

    public int insert(OrderHeader orderHeader) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(orderHeader.KODE_OUTLET, orderHeader.getKodeOutlet());
        values.put(orderHeader.UPDATE_DATE, "");
        values.put(orderHeader.CREATE_DATE, sdf.format(new Date()));

        //Inserting row
        long order_header_id = db.insert(OrderHeader.TABLE, null, values);
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
        String selectQuery = "SELECT COUNT (*) FROM " + OrderHeader.TABLE + " WHERE " + OrderHeader.KODE_OUTLET + " = ? ";

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

    private List<OrderItem> searchOrderItemByHeaderID(int orderHeaderId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder selectItemQuery = new StringBuilder();
        selectItemQuery.append("SELECT ");
        selectItemQuery.append(OrderItem.TABLE + "." + OrderItem.ID + " as " + OrderItem.TABLE + OrderItem.ID + ", ");
        selectItemQuery.append(OrderItem.TABLE + "." + OrderItem.GUID + " as " + OrderItem.TABLE + OrderItem.GUID + ", ");
        selectItemQuery.append(OrderItem.TABLE + "." + OrderItem.ORDER_HEADER_ID + " as " + OrderItem.TABLE + OrderItem.ORDER_HEADER_ID + ", ");
        selectItemQuery.append(OrderItem.TABLE + "." + OrderItem.KODE + " as " + OrderItem.TABLE + OrderItem.KODE + ", ");
        selectItemQuery.append(OrderItem.TABLE + "." + OrderItem.NAMA_BARANG + " as " + OrderItem.TABLE + OrderItem.NAMA_BARANG + ", ");
        selectItemQuery.append(OrderItem.TABLE + "." + OrderItem.HARGA + " as " + OrderItem.TABLE + OrderItem.HARGA + ", ");
        selectItemQuery.append(OrderItem.TABLE + "." + OrderItem.QTY + " as " + OrderItem.TABLE + OrderItem.QTY + ", ");
        selectItemQuery.append(OrderItem.TABLE + "." + OrderItem.UNIT + " as " + OrderItem.TABLE + OrderItem.UNIT + ", ");
        selectItemQuery.append(OrderItem.TABLE + "." + OrderItem.CREATE_DATE + " as " + OrderItem.TABLE + OrderItem.CREATE_DATE + " ");
        selectItemQuery.append("FROM " + OrderItem.TABLE + " ");
        selectItemQuery.append("WHERE " + OrderItem.TABLE + "." + OrderItem.ORDER_HEADER_ID + " = ? ");
        selectItemQuery.append("ORDER BY " + OrderItem.TABLE + "." + OrderItem.ID + " ASC");

        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        Cursor cursorItem = db.rawQuery(selectItemQuery.toString(), new String[]{String.valueOf(orderHeaderId)});

        if (cursorItem.moveToFirst()) {
            do {
                OrderItem orderItem = new OrderItem();
                orderItem.setId(cursorItem.getInt(cursorItem.getColumnIndex(OrderItem.TABLE + OrderItem.ID)));
                orderItem.setGuid(cursorItem.getString(cursorItem.getColumnIndex(OrderItem.TABLE + OrderItem.GUID)));
                orderItem.setKode(cursorItem.getString(cursorItem.getColumnIndex(OrderItem.TABLE + OrderItem.KODE)));
                orderItem.setNamaBarang(cursorItem.getString(cursorItem.getColumnIndex(OrderItem.TABLE + OrderItem.NAMA_BARANG)));
                orderItem.setHarga(cursorItem.getDouble(cursorItem.getColumnIndex(OrderItem.TABLE + OrderItem.HARGA)));
                orderItem.setQty(cursorItem.getInt(cursorItem.getColumnIndex(OrderItem.TABLE + OrderItem.QTY)));
                orderItem.setUnit(cursorItem.getString(cursorItem.getColumnIndex(OrderItem.TABLE + OrderItem.UNIT)));
                orderItem.setCreateDate(getDateFromCursor(cursorItem, OrderItem.TABLE + OrderItem.CREATE_DATE));

                orderItems.add(orderItem);
            } while (cursorItem.moveToNext());
        }

        return orderItems;
    }
}

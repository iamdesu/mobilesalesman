package com.bali.nusadua.productmonitor.repo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bali.nusadua.productmonitor.model.OrderHeader;
import com.bali.nusadua.productmonitor.model.OrderItem;
import com.bali.nusadua.productmonitor.modelView.OrderHeaderItemView;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

    public List<OrderHeaderItemView> getOrderHeaderItem() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT ");
        selectQuery.append(OrderHeader.TABLE + "." + OrderHeader.ID + ", ");
        selectQuery.append(OrderHeader.TABLE + "." + OrderHeader.GUID + ", ");
        selectQuery.append(OrderHeader.TABLE + "." + OrderHeader.KODE_OUTLET + ", ");
        selectQuery.append(OrderHeader.TABLE + "." + OrderHeader.UPDATE_DATE + ", ");
        selectQuery.append(OrderHeader.TABLE + "." + OrderHeader.CREATE_DATE + ", ");
        selectQuery.append(OrderHeader.TABLE + "." + OrderHeader.CREATE_DATE + ", ");
        selectQuery.append(OrderItem.TABLE + "." + OrderItem.ID + ", ");
        selectQuery.append(OrderItem.TABLE + "." + OrderItem.GUID + ", ");
        selectQuery.append(OrderItem.TABLE + "." + OrderItem.ORDER_HEADER_GUID + ", ");
        selectQuery.append(OrderItem.TABLE + "." + OrderItem.KODE + ", ");
        selectQuery.append(OrderItem.TABLE + "." + OrderItem.NAMA_BARANG + ", ");
        selectQuery.append(OrderItem.TABLE + "." + OrderItem.HARGA + ", ");
        selectQuery.append(OrderItem.TABLE + "." + OrderItem.QTY + ", ");
        selectQuery.append(OrderItem.TABLE + "." + OrderItem.UNIT + ", ");
        selectQuery.append(OrderItem.TABLE + "." + OrderItem.CREATE_DATE + " ");
        selectQuery.append("FROM " + OrderHeader.TABLE + ", " + OrderItem.TABLE + " ");
        selectQuery.append("WHERE " + OrderHeader.TABLE + "." + OrderHeader.GUID + " = " + OrderItem.TABLE + "." + OrderItem.ORDER_HEADER_GUID);

        Log.i("getOrderHeaderItem : ", selectQuery.toString());

        List<OrderHeaderItemView> listOrderHeaderItem = new ArrayList<OrderHeaderItemView>();
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

        return null;
    }
}

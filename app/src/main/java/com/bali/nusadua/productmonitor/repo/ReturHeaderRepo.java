package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bali.nusadua.productmonitor.model.ReturHeader;
import com.bali.nusadua.productmonitor.model.ReturItem;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by desu on 8/29/15.
 */
public class ReturHeaderRepo {
    private DBHelper dbHelper;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    public ReturHeaderRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public List<String> getCustomerOnReturHeader() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                ReturHeader.KODE_OUTLET + " FROM " +
                ReturHeader.TABLE;

        List<String> listCustomerID = new ArrayList<String>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String customerID = cursor.getString(cursor.getColumnIndex(ReturHeader.KODE_OUTLET));
                listCustomerID.add(customerID);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listCustomerID;
    }

    public List<ReturHeader> getReturHeaderItemByCustomer(String kodeOutlet) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT ");
        selectQuery.append(ReturHeader.TABLE + "." + ReturHeader.ID + " as " + ReturHeader.TABLE + ReturHeader.ID + ", ");
        selectQuery.append(ReturHeader.TABLE + "." + ReturHeader.KODE_OUTLET + " as " + ReturHeader.TABLE + ReturHeader.KODE_OUTLET + ", ");
        selectQuery.append(ReturHeader.TABLE + "." + ReturHeader.UPDATE_DATE + " as " + ReturHeader.TABLE + ReturHeader.UPDATE_DATE + ", ");
        selectQuery.append(ReturHeader.TABLE + "." + ReturHeader.CREATE_DATE + " as " + ReturHeader.TABLE + ReturHeader.CREATE_DATE + " ");
        selectQuery.append("FROM " + ReturHeader.TABLE + " ");
        selectQuery.append("WHERE " + ReturHeader.TABLE + "." + ReturHeader.KODE_OUTLET + " = ? ");
        selectQuery.append("ORDER BY " + ReturHeader.TABLE + "." + ReturHeader.ID + " ASC");

        Log.i("getReturHeaderItem : ", selectQuery.toString());

        List<ReturHeader> returHeaders = new ArrayList<ReturHeader>();
        Cursor cursor = db.rawQuery(selectQuery.toString(), new String[]{kodeOutlet});

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ReturHeader returHeader = new ReturHeader();
                returHeader.setId(cursor.getInt(cursor.getColumnIndex(ReturHeader.TABLE + ReturHeader.ID)));
                returHeader.setKodeOutlet(cursor.getString(cursor.getColumnIndex(ReturHeader.TABLE + ReturHeader.KODE_OUTLET)));
                returHeader.setUpdateDate(getDateFromCursor(cursor, ReturHeader.TABLE + ReturHeader.UPDATE_DATE));
                returHeader.setCreateDate(getDateFromCursor(cursor, ReturHeader.TABLE + ReturHeader.CREATE_DATE));

                returHeader.setReturItems(searchReturItemByHeaderID(returHeader.getId()));
                returHeaders.add(returHeader);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return returHeaders;
    }

    public int insert(ReturHeader returHeader) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(returHeader.KODE_OUTLET, returHeader.getKodeOutlet());
        values.put(returHeader.UPDATE_DATE, "");
        values.put(returHeader.CREATE_DATE, sdf.format(new Date()));

        //Inserting row
        long order_header_id = db.insert(ReturHeader.TABLE, null, values);
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
        String selectQuery = "SELECT COUNT (*) FROM " + ReturHeader.TABLE + " WHERE " + ReturHeader.KODE_OUTLET + " = ? ";

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

    private List<ReturItem> searchReturItemByHeaderID(int returHeaderId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder selectItemQuery = new StringBuilder();
        selectItemQuery.append("SELECT ");
        selectItemQuery.append(ReturItem.TABLE + "." + ReturItem.ID + " as " + ReturItem.TABLE + ReturItem.ID + ", ");
        selectItemQuery.append(ReturItem.TABLE + "." + ReturItem.GUID + " as " + ReturItem.TABLE + ReturItem.GUID + ", ");
        selectItemQuery.append(ReturItem.TABLE + "." + ReturItem.RETUR_HEADER_ID + " as " + ReturItem.TABLE + ReturItem.RETUR_HEADER_ID + ", ");
        selectItemQuery.append(ReturItem.TABLE + "." + ReturItem.KODE + " as " + ReturItem.TABLE + ReturItem.KODE + ", ");
        selectItemQuery.append(ReturItem.TABLE + "." + ReturItem.NAMA_BARANG + " as " + ReturItem.TABLE + ReturItem.NAMA_BARANG + ", ");
        selectItemQuery.append(ReturItem.TABLE + "." + ReturItem.HARGA + " as " + ReturItem.TABLE + ReturItem.HARGA + ", ");
        selectItemQuery.append(ReturItem.TABLE + "." + ReturItem.QTY + " as " + ReturItem.TABLE + ReturItem.QTY + ", ");
        selectItemQuery.append(ReturItem.TABLE + "." + ReturItem.UNIT + " as " + ReturItem.TABLE + ReturItem.UNIT + ", ");
        selectItemQuery.append(ReturItem.TABLE + "." + ReturItem.CREATE_DATE + " as " + ReturItem.TABLE + ReturItem.CREATE_DATE + " ");
        selectItemQuery.append("FROM " + ReturItem.TABLE + " ");
        selectItemQuery.append("WHERE " + ReturItem.TABLE + "." + ReturItem.RETUR_HEADER_ID + " = ? ");
        selectItemQuery.append("ORDER BY " + ReturItem.TABLE + "." + ReturItem.ID + " ASC");

        List<ReturItem> returItems = new ArrayList<ReturItem>();
        Cursor cursorItem = db.rawQuery(selectItemQuery.toString(), new String[]{String.valueOf(returHeaderId)});

        if (cursorItem.moveToFirst()) {
            do {
                ReturItem returItem = new ReturItem();
                returItem.setId(cursorItem.getInt(cursorItem.getColumnIndex(ReturItem.TABLE + ReturItem.ID)));
                returItem.setGuid(cursorItem.getString(cursorItem.getColumnIndex(ReturItem.TABLE + ReturItem.GUID)));
                returItem.setKode(cursorItem.getString(cursorItem.getColumnIndex(ReturItem.TABLE + ReturItem.KODE)));
                returItem.setNamaBarang(cursorItem.getString(cursorItem.getColumnIndex(ReturItem.TABLE + ReturItem.NAMA_BARANG)));
                returItem.setHarga(cursorItem.getDouble(cursorItem.getColumnIndex(ReturItem.TABLE + ReturItem.HARGA)));
                returItem.setQty(cursorItem.getInt(cursorItem.getColumnIndex(ReturItem.TABLE + ReturItem.QTY)));
                returItem.setUnit(cursorItem.getString(cursorItem.getColumnIndex(ReturItem.TABLE + ReturItem.UNIT)));
                returItem.setCreateDate(getDateFromCursor(cursorItem, ReturItem.TABLE + ReturItem.CREATE_DATE));

                returItems.add(returItem);
            } while (cursorItem.moveToNext());
        }

        return returItems;
    }
}

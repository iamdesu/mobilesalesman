package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bali.nusadua.productmonitor.model.OrderItem;
import com.bali.nusadua.productmonitor.model.ReturHeader;
import com.bali.nusadua.productmonitor.model.ReturItem;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ReturRepo {
    private DBHelper dbHelper;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    public ReturRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(ReturItem retur) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        retur.setGuid(UUID.randomUUID().toString());
        values.put(ReturItem.GUID, retur.getGuid());
        values.put(ReturItem.RETUR_HEADER_ID, retur.getReturHeaderId());
        values.put(ReturItem.KODE, retur.getKode());
        values.put(ReturItem.NAMA_BARANG, retur.getNamaBarang());
        values.put(ReturItem.HARGA, retur.getHarga());
        values.put(ReturItem.QTY, retur.getQty());
        values.put(ReturItem.UNIT, retur.getUnit());
        values.put(ReturItem.CREATE_DATE, sdf.format(new Date()));

        //Inserting row
        long retur_id = db.insert(ReturItem.TABLE, null, values);
        db.close();
        return (int) retur_id;
    }

    public void insertAll(List<ReturItem> returs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (ReturItem retur : returs) {
            ContentValues contentValues = new ContentValues();

            retur.setGuid(UUID.randomUUID().toString());
            contentValues.put(ReturItem.GUID, retur.getGuid());
            contentValues.put(ReturItem.RETUR_HEADER_ID, retur.getReturHeaderId());
            contentValues.put(ReturItem.KODE, retur.getKode());
            contentValues.put(ReturItem.NAMA_BARANG, retur.getNamaBarang());
            contentValues.put(ReturItem.HARGA, retur.getHarga());
            contentValues.put(ReturItem.QTY, retur.getQty());
            contentValues.put(ReturItem.UNIT, retur.getUnit());
            contentValues.put(ReturItem.CREATE_DATE, sdf.format(new Date()));

            db.insert(ReturItem.TABLE, null, contentValues);
        }
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(ReturItem.TABLE, null, null);
        db.close();
    }

    public void update(ReturItem retur) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ReturItem.KODE, retur.getKode());
        values.put(ReturItem.NAMA_BARANG, retur.getNamaBarang());
        values.put(ReturItem.HARGA, retur.getHarga());
        values.put(ReturItem.QTY, retur.getQty());
        values.put(ReturItem.UNIT, retur.getUnit());

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(ReturItem.TABLE, values, ReturItem.GUID + "= ?", new String[]{retur.getGuid()});
        db.close();
    }

    public ReturItem findByGUID(String guid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                ReturItem.ID + ", " +
                ReturItem.GUID + ", " +
                ReturItem.RETUR_HEADER_ID + ", " +
                ReturItem.KODE + ", " +
                ReturItem.NAMA_BARANG + ", " +
                ReturItem.HARGA + ", " +
                ReturItem.QTY + ", " +
                ReturItem.UNIT + ", " +
                ReturItem.CREATE_DATE + " FROM " +
                ReturItem.TABLE + " WHERE " +
                ReturItem.GUID + " = ?";

        ReturItem retur = new ReturItem();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{guid});

        if (cursor.moveToFirst()) {
            do {
                retur.setId(cursor.getInt(cursor.getColumnIndex(ReturItem.ID)));
                retur.setGuid(cursor.getString(cursor.getColumnIndex(ReturItem.GUID)));
                retur.setReturHeaderId(cursor.getInt(cursor.getColumnIndex(ReturItem.RETUR_HEADER_ID)));
                retur.setKode(cursor.getString(cursor.getColumnIndex(ReturItem.KODE)));
                retur.setNamaBarang(cursor.getString(cursor.getColumnIndex(ReturItem.NAMA_BARANG)));
                retur.setHarga(cursor.getDouble(cursor.getColumnIndex(ReturItem.HARGA)));
                retur.setQty(cursor.getInt(cursor.getColumnIndex(ReturItem.QTY)));
                retur.setUnit(cursor.getString(cursor.getColumnIndex(ReturItem.UNIT)));

                try {
                    Date createDate = sdf.parse(cursor.getString(cursor.getColumnIndex(ReturItem.CREATE_DATE)));
                    retur.setCreateDate(createDate);
                } catch (ParseException e) {
                    retur.setCreateDate(null);
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return retur;
    }

    /**
     * Retrieve all records and populate List<ReturItem>
     */
    public List<ReturItem> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + ReturItem.TABLE;

        List<ReturItem> listRetur = new ArrayList<ReturItem>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list  (cursor.getColumnIndex(Team.ID
        if (cursor.moveToFirst()) {
            do {
                ReturItem retur = new ReturItem();
                retur.setId((int) cursor.getLong(cursor.getColumnIndex(ReturItem.ID)));
                retur.setReturHeaderId(cursor.getInt(cursor.getColumnIndex(ReturItem.RETUR_HEADER_ID)));
                retur.setUnit(cursor.getString(cursor.getColumnIndex(ReturItem.UNIT)));
                retur.setHarga(cursor.getDouble(cursor.getColumnIndex(ReturItem.HARGA)));
                retur.setNamaBarang(cursor.getString(cursor.getColumnIndex(ReturItem.NAMA_BARANG)));
                retur.setGuid(cursor.getString(cursor.getColumnIndex(ReturItem.GUID)));
                retur.setQty(cursor.getInt(cursor.getColumnIndex(ReturItem.QTY)));
                retur.setKode(cursor.getString(cursor.getColumnIndex(ReturItem.KODE)));

                try {
                    Date createDate = sdf.parse(cursor.getString(cursor.getColumnIndex(ReturItem.CREATE_DATE)));
                    retur.setCreateDate(createDate);
                } catch (ParseException e) {
                    retur.setCreateDate(null);
                }

                listRetur.add(retur);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listRetur;
    }

    public Integer getCountByCustomer(String kodeOutlet) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT COUNT (*) " +
                "FROM " + ReturHeader.TABLE + " LEFT JOIN " + ReturItem.TABLE +
                " ON " + ReturHeader.TABLE + "." + ReturHeader.ID + " = " + ReturItem.TABLE + "." + ReturItem.RETUR_HEADER_ID +
                " WHERE " + ReturHeader.KODE_OUTLET + " = ? ";

        Log.i("getCountByCustomer : ", selectQuery);

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
}
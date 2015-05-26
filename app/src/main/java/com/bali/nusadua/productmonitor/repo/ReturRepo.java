package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bali.nusadua.productmonitor.model.Outlet;
import com.bali.nusadua.productmonitor.model.Retur;
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

    public int insert(Retur retur) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        retur.setGuid(UUID.randomUUID().toString());
        values.put(Retur.GUID, retur.getGuid());
        values.put(Retur.KODE, retur.getKode());
        values.put(Retur.NAMA_BARANG, retur.getNamaBarang());
        values.put(Retur.HARGA, retur.getHarga());
        values.put(Retur.QTY, retur.getQty());
        values.put(Retur.UNIT, retur.getUnit());
        values.put(Retur.KODE_OUTLET, retur.getKodeOutlet());
        values.put(Retur.CREATE_DATE, sdf.format(new Date()));

        //Inserting row
        long retur_id = db.insert(Retur.TABLE, null, values);
        db.close();
        return (int) retur_id;
    }

    public void insertAll(List<Retur> returs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for(Retur retur : returs) {
            ContentValues contentValues = new ContentValues();

            retur.setGuid(UUID.randomUUID().toString());
            contentValues.put(Retur.GUID, retur.getGuid());
            contentValues.put(Retur.KODE, retur.getKode());
            contentValues.put(Retur.NAMA_BARANG, retur.getNamaBarang());
            contentValues.put(Retur.HARGA, retur.getHarga());
            contentValues.put(Retur.QTY, retur.getQty());
            contentValues.put(Retur.UNIT, retur.getUnit());
            contentValues.put(Retur.KODE_OUTLET, retur.getKodeOutlet());
            contentValues.put(Retur.CREATE_DATE, sdf.format(new Date()));

            db.insert(Retur.TABLE, null, contentValues);
        }
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Retur.TABLE, null, null);
        db.close();
    }

    public void update(Retur retur) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Retur.KODE, retur.getKode());
        values.put(Retur.NAMA_BARANG, retur.getNamaBarang());
        values.put(Retur.HARGA, retur.getHarga());
        values.put(Retur.QTY, retur.getQty());
        values.put(Retur.UNIT, retur.getUnit());
        values.put(Retur.KODE_OUTLET, retur.getKodeOutlet());

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(Retur.TABLE, values, Retur.GUID + "= ?", new String[]{retur.getGuid()});
        db.close();
    }

    public Retur findByGUID(String guid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                Retur.ID + ", " +
                Retur.GUID + ", " +
                Retur.KODE + ", " +
                Retur.NAMA_BARANG + ", " +
                Retur.HARGA + ", " +
                Retur.QTY + ", " +
                Retur.UNIT + ", " +
                Retur.KODE_OUTLET + ", " +
                Retur.CREATE_DATE + " FROM " +
                Retur.TABLE + " WHERE " +
                Retur.GUID + " = ?";

        Retur retur = new Retur();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{ guid } );

        if(cursor.moveToFirst()) {
            do {
                retur.setId(cursor.getInt(cursor.getColumnIndex(Retur.ID)));
                retur.setGuid(cursor.getString(cursor.getColumnIndex(Retur.GUID)));
                retur.setKode(cursor.getString(cursor.getColumnIndex(Retur.KODE)));
                retur.setNamaBarang(cursor.getString(cursor.getColumnIndex(Retur.NAMA_BARANG)));
                retur.setHarga(cursor.getInt(cursor.getColumnIndex(Retur.HARGA)));
                retur.setQty(cursor.getInt(cursor.getColumnIndex(Retur.QTY)));
                retur.setUnit(cursor.getString(cursor.getColumnIndex(Retur.UNIT)));
                retur.setKodeOutlet(cursor.getString(cursor.getColumnIndex(Retur.KODE_OUTLET)));

                try {
                    Date createDate = sdf.parse(cursor.getString(cursor.getColumnIndex(Retur.CREATE_DATE)));
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
     * Retrieve all records and populate List<Retur>
     */
    public List<Retur> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Retur.TABLE;

        List<Retur> listRetur = new ArrayList<Retur>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list  (cursor.getColumnIndex(Team.ID
        if(cursor.moveToFirst()){
            do {
                Retur retur = new Retur();
                retur.setId((int) cursor.getLong(cursor.getColumnIndex(Retur.ID)));
                retur.setUnit(cursor.getString(cursor.getColumnIndex(Retur.UNIT)));
                retur.setHarga(cursor.getInt(cursor.getColumnIndex(Retur.HARGA)));
                retur.setNamaBarang(cursor.getString(cursor.getColumnIndex(Retur.NAMA_BARANG)));
                retur.setGuid(cursor.getString(cursor.getColumnIndex(Retur.GUID)));
                retur.setQty(cursor.getInt(cursor.getColumnIndex(Retur.QTY)));
                retur.setKode(cursor.getString(cursor.getColumnIndex(Retur.KODE)));
                retur.setKodeOutlet(cursor.getString(cursor.getColumnIndex(Retur.KODE_OUTLET)));

                try {
                    Date createDate = sdf.parse(cursor.getString(cursor.getColumnIndex(Retur.CREATE_DATE)));
                    retur.setCreateDate(createDate);
                } catch (ParseException e) {
                    retur.setCreateDate(null);
                }

                listRetur.add(retur);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listRetur;
    }

    public List<Retur> getAllWithOutlet() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                Retur.TABLE + "." + Retur.ID + ", " +
                Retur.TABLE + "." + Retur.GUID + ", " +
                Retur.TABLE + "." + Retur.KODE + ", " +
                Retur.TABLE + "." + Retur.NAMA_BARANG + ", " +
                Retur.TABLE + "." + Retur.HARGA + ", " +
                Retur.TABLE + "." + Retur.QTY + ", " +
                Retur.TABLE + "." + Retur.UNIT + ", " +
                Retur.TABLE + "." + Retur.KODE_OUTLET + ", " +
                Retur.TABLE + "." + Retur.CREATE_DATE + ", " +
                Outlet.TABLE + "." + Outlet.ID + " as outletID, " +
                Outlet.TABLE + "." + Outlet.GUID + " as outletGUID, " +
                Outlet.TABLE + "." + Outlet.KODE + " as outletKode, " +
                Outlet.TABLE + "." + Outlet.NAME + " as outletName" +
                " FROM " + Retur.TABLE + " LEFT JOIN " + Outlet.TABLE +
                " ON "+ Retur.TABLE + "." + Retur.KODE_OUTLET +" = "+ Outlet.TABLE + "." + Outlet.KODE;

        List<Retur> listRetur = new ArrayList<Retur>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list
        if(cursor.moveToFirst()){
            do {
                Retur retur = new Retur();
                retur.setId((int) cursor.getLong(cursor.getColumnIndex(Retur.ID)));
                retur.setUnit(cursor.getString(cursor.getColumnIndex(Retur.UNIT)));
                retur.setHarga(cursor.getInt(cursor.getColumnIndex(Retur.HARGA)));
                retur.setNamaBarang(cursor.getString(cursor.getColumnIndex(Retur.NAMA_BARANG)));
                retur.setGuid(cursor.getString(cursor.getColumnIndex(Retur.GUID)));
                retur.setQty(cursor.getInt(cursor.getColumnIndex(Retur.QTY)));
                retur.setKode(cursor.getString(cursor.getColumnIndex(Retur.KODE)));
                retur.setKodeOutlet(cursor.getString(cursor.getColumnIndex(Retur.KODE_OUTLET)));

                try {
                    Date createDate = sdf.parse(cursor.getString(cursor.getColumnIndex(Retur.CREATE_DATE)));
                    retur.setCreateDate(createDate);
                } catch (ParseException e) {
                    retur.setCreateDate(null);
                }

                Outlet outlet = new Outlet();
                outlet.setId((int) cursor.getLong(cursor.getColumnIndex("outletID")));
                outlet.setGuid(cursor.getString(cursor.getColumnIndex("outletGUID")));
                outlet.setKode(cursor.getString(cursor.getColumnIndex("outletKode")));
                outlet.setName(cursor.getString(cursor.getColumnIndex("outletName")));

                retur.setOutlet(outlet);

                listRetur.add(retur);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listRetur;
    }

    public List<Retur> getReturByCustomer(String customerId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                Retur.ID + ", " +
                Retur.GUID + ", " +
                Retur.KODE + ", " +
                Retur.NAMA_BARANG + ", " +
                Retur.HARGA + ", " +
                Retur.QTY + ", " +
                Retur.UNIT + ", " +
                Retur.KODE_OUTLET + ", " +
                Retur.CREATE_DATE + " FROM " +
                Retur.TABLE + " WHERE " +
                Retur.KODE_OUTLET + " = ?";

        List<Retur> returs = new ArrayList<Retur>();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{ customerId } );

        //Looping through all rows and adding to list
        if(cursor.moveToFirst()){
            do {
                Retur retur = new Retur();
                retur.setId((int) cursor.getLong(cursor.getColumnIndex(Retur.ID)));
                retur.setUnit(cursor.getString(cursor.getColumnIndex(Retur.UNIT)));
                retur.setHarga(cursor.getInt(cursor.getColumnIndex(Retur.HARGA)));
                retur.setNamaBarang(cursor.getString(cursor.getColumnIndex(Retur.NAMA_BARANG)));
                retur.setGuid(cursor.getString(cursor.getColumnIndex(Retur.GUID)));
                retur.setQty(cursor.getInt(cursor.getColumnIndex(Retur.QTY)));
                retur.setKode(cursor.getString(cursor.getColumnIndex(Retur.KODE)));
                retur.setKodeOutlet(cursor.getString(cursor.getColumnIndex(Retur.KODE_OUTLET)));

                try {
                    Date createDate = sdf.parse(cursor.getString(cursor.getColumnIndex(Retur.CREATE_DATE)));
                    retur.setCreateDate(createDate);
                } catch (ParseException e) {
                    retur.setCreateDate(null);
                }

                returs.add(retur);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return returs;
    }
}
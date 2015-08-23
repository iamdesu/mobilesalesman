package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bali.nusadua.productmonitor.model.Outlet;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by desu sudarsana on 4/21/2015.
 */
public class OutletRepo {
    private DBHelper dbHelper;

    public OutletRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(Outlet outlet) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        outlet.setGuid(UUID.randomUUID().toString());
        values.put(Outlet.GUID, outlet.getGuid());
        values.put(Outlet.KODE, outlet.getKode());
        values.put(Outlet.NAME, outlet.getName());

        //Inserting row
        long outlet_id = db.insert(Outlet.TABLE, null, values);
        db.close();
        return (int) outlet_id;
    }

    public void insertAll(List<Outlet> outlets){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int size = outlets.size();
        for(int i = 0; i < size; i++){
            Outlet outlet = new Outlet();
            ContentValues contentValues = new ContentValues();
            outlet = outlets.get(i);
            outlet.setGuid(UUID.randomUUID().toString());
            contentValues.put(Outlet.GUID, outlet.getGuid());
            contentValues.put(Outlet.KODE, outlet.getKode());
            contentValues.put(Outlet.NAME, outlet.getName());

            db.insert(Outlet.TABLE, null, contentValues);
        }
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Outlet.TABLE, null, null);
        db.close();
    }

    public void update(Outlet outlet) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Outlet.KODE, outlet.getKode());
        values.put(Outlet.NAME, outlet.getName());

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(Outlet.TABLE, values, Outlet.GUID + "= ?", new String[] { outlet.getGuid() });
        db.close();
    }

    public Outlet findByGUID(String guid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                Outlet.ID + ", " +
                Outlet.GUID + ", " +
                Outlet.KODE + ", " +
                Outlet.NAME + " FROM " +
                Outlet.TABLE + " WHERE " +
                Outlet.GUID + " = ?";

        Outlet outlet = new Outlet();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{ guid } );

        if(cursor.moveToFirst()) {
            do {
                outlet.setId(cursor.getInt(cursor.getColumnIndex(Outlet.ID)));
                outlet.setGuid(cursor.getString(cursor.getColumnIndex(Outlet.GUID)));
                outlet.setKode(cursor.getString(cursor.getColumnIndex(Outlet.KODE)));
                outlet.setName(cursor.getString(cursor.getColumnIndex(Outlet.NAME)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return outlet;
    }

    //Retrieve all records and populate List<OrderItem>
    public List<Outlet> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Outlet.TABLE;

        List<Outlet> listOutlet = new ArrayList<Outlet>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list  (cursor.getColumnIndex(Team.ID
        if(cursor.moveToFirst()){
            do {
                Outlet outlet = new Outlet();
                outlet.setId((int) cursor.getLong(cursor.getColumnIndex(Outlet.ID)));
                outlet.setGuid(cursor.getString(cursor.getColumnIndex(Outlet.GUID)));
                outlet.setKode(cursor.getString(cursor.getColumnIndex(Outlet.KODE)));
                outlet.setName(cursor.getString(cursor.getColumnIndex(Outlet.NAME)));

                listOutlet.add(outlet);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listOutlet;
    }
}

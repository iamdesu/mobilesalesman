package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bali.nusadua.productmonitor.model.StaffBilling;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class StaffBillingRepo {
    private DBHelper dbHelper;

    public StaffBillingRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(StaffBilling staffBilling) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(StaffBilling.STAFF, staffBilling.getStaff());
        values.put(StaffBilling.STAFFNAME, staffBilling.getStaffName());
        values.put(StaffBilling.PASSWD, staffBilling.getPassword());
        values.put(StaffBilling.LEVEL, staffBilling.getLevel());
        values.put(StaffBilling.USER_ID, staffBilling.getUserID());
        values.put(StaffBilling.TEAM, staffBilling.getTeam());
        values.put(StaffBilling.EXPIRE, staffBilling.getExpire());

        //Inserting row
        long staffBilling_id = db.insert(StaffBilling.TABLE, null, values);
        db.close();

        return (int) staffBilling_id;
    }

    //Retrieve all records and populate List<OrderItem>
    public List<StaffBilling> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + StaffBilling.TABLE;

        List<StaffBilling> listStaffBilling = new ArrayList<StaffBilling>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list  (cursor.getColumnIndex(Team.ID
        if (cursor.moveToFirst()) {
            do {
                StaffBilling staffBilling = new StaffBilling();
                staffBilling.setId((int) cursor.getLong(cursor.getColumnIndex(StaffBilling.ID)));
                staffBilling.setStaff(cursor.getString(cursor.getColumnIndex(StaffBilling.STAFF)));
                staffBilling.setStaffName(cursor.getString(cursor.getColumnIndex(StaffBilling.STAFFNAME)));
                staffBilling.setPassword(cursor.getString(cursor.getColumnIndex(StaffBilling.PASSWD)));
                staffBilling.setLevel(cursor.getInt(cursor.getColumnIndex(StaffBilling.LEVEL)));
                staffBilling.setUserID(cursor.getString(cursor.getColumnIndex(StaffBilling.USER_ID)));
                staffBilling.setTeam(cursor.getString(cursor.getColumnIndex(StaffBilling.TEAM)));
                staffBilling.setExpire(cursor.getInt(cursor.getColumnIndex(StaffBilling.EXPIRE)));

                listStaffBilling.add(staffBilling);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listStaffBilling;
    }

    public StaffBilling getStaffBilling(String userId, String password) {
        StaffBilling staffBilling = null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                StaffBilling.ID + ", " +
                StaffBilling.STAFF + ", " +
                StaffBilling.STAFFNAME + ", " +
                StaffBilling.PASSWD + ", " +
                StaffBilling.LEVEL + ", " +
                StaffBilling.USER_ID + ", " +
                StaffBilling.TEAM + ", " +
                StaffBilling.EXPIRE + " FROM " +
                StaffBilling.TABLE + " WHERE " +
                StaffBilling.USER_ID + " = ? AND " +
                StaffBilling.PASSWD + " = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{ userId, password } );

        if(cursor.moveToFirst()) {
            do {
                staffBilling = new StaffBilling();
                staffBilling.setId(cursor.getInt(cursor.getColumnIndex(StaffBilling.ID)));
                staffBilling.setStaff(cursor.getString(cursor.getColumnIndex(StaffBilling.STAFF)));
                staffBilling.setStaffName(cursor.getString(cursor.getColumnIndex(StaffBilling.STAFFNAME)));
                staffBilling.setPassword(cursor.getString(cursor.getColumnIndex(StaffBilling.PASSWD)));
                staffBilling.setLevel(cursor.getInt(cursor.getColumnIndex(StaffBilling.LEVEL)));
                staffBilling.setUserID(cursor.getString(cursor.getColumnIndex(StaffBilling.USER_ID)));
                staffBilling.setTeam(cursor.getString(cursor.getColumnIndex(StaffBilling.TEAM)));
                staffBilling.setExpire(cursor.getInt(cursor.getColumnIndex(StaffBilling.EXPIRE)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return staffBilling;
    }

}

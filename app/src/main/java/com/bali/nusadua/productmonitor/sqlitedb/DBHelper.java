package com.bali.nusadua.productmonitor.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bali.nusadua.productmonitor.model.Team;

/**
 * Created by desu sudarsana on 4/12/2015.
 */
public class DBHelper extends SQLiteOpenHelper{
    //Version number to upgrade database version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "mobilesalesman.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_TEAM = "CREATE TABLE " + Team.TABLE + "("
                + Team.GUID + " TEXT PRIMARY KEY, "
                + Team.NAME + " TEXT )";

        db.execSQL(CREATE_TABLE_TEAM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if existed, all data will be gone
        db.execSQL("DROP TABLE IF EXISTS " + Team.TABLE);

        //Create tables again
        onCreate(db);
    }
}

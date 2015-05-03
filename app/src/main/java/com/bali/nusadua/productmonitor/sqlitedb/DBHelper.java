package com.bali.nusadua.productmonitor.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bali.nusadua.productmonitor.model.Order;
import com.bali.nusadua.productmonitor.model.Outlet;
import com.bali.nusadua.productmonitor.model.Retur;
import com.bali.nusadua.productmonitor.model.Settlement;
import com.bali.nusadua.productmonitor.model.Team;

import java.util.UUID;

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
        String CREATE_TABLE_TEAM = "CREATE TABLE " + Team.TABLE + "( "
                + Team.ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Team.GUID + " TEXT, "
                + Team.NAME + " TEXT )";

        db.execSQL(CREATE_TABLE_TEAM);

        String CREATE_TABLE_ORDER = "CREATE TABLE " + Order.TABLE + " ( "
                + Order.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Order.GUID + " TEXT, "
                + Order.KODE + " TEXT, "
                + Order.NAMA_BARANG + " TEXT, "
                + Order.HARGA + " INTEGER, "
                + Order.QTY + " INTEGER, "
                + Order.UNIT + " TEXT, "
                + Order.KODE_OUTLET + " TEXT, "
                + Order.CREATE_DATE + " TEXT ) ";

        db.execSQL(CREATE_TABLE_ORDER);

        String CREATE_TABLE_RETUR = "CREATE TABLE " + Retur.TABLE + " ( "
                + Retur.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Retur.GUID + " TEXT, "
                + Retur.KODE + " TEXT, "
                + Retur.NAMA_BARANG + " TEXT, "
                + Retur.HARGA + " INTEGER, "
                + Retur.QTY + " INTEGER, "
                + Retur.UNIT + " TEXT, "
                + Retur.KODE_OUTLET + " TEXT, "
                + Retur.CREATE_DATE + " TEXT ) ";

        db.execSQL(CREATE_TABLE_RETUR);

        String CREATE_TABLE_SETTLEMENT = "CREATE TABLE " + Settlement.TABLE + " ( "
                + Settlement.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Settlement.GUID + " TEXT, "
                + Settlement.INVOICE_NUMBER + " TEXT, "
                + Settlement.INVOICE_DATE + " TEXT, "
                + Settlement.CREDIT + " INTEGER, "
                + Settlement.PAYMENT_METHOD + " TEXT, "
                + Settlement.NOMINAL_PAYMENT + " INTEGER, "
                + Settlement.KODE_OUTLET + " TEXT, "
                + Settlement.CREATE_DATE + " TEXT ) ";

        db.execSQL(CREATE_TABLE_SETTLEMENT);

        String CREATE_TABLE_OUTLET = "CREATE TABLE " + Outlet.TABLE + " ( "
                + Outlet.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Outlet.GUID + " TEXT, "
                + Outlet.KODE + " TEXT, "
                + Outlet.NAME + " TEXT ) ";

        db.execSQL(CREATE_TABLE_OUTLET);

        //Insert Team
        String INSERT_TEAM_A = "INSERT INTO  " + Team.TABLE + " (" + Team.GUID + ", " + Team.NAME + ") values ( '" + UUID.randomUUID().toString() + "','Team A')";
        db.execSQL(INSERT_TEAM_A);
        String INSERT_TEAM_B = "INSERT INTO  " + Team.TABLE + " (" + Team.GUID + ", " + Team.NAME + ") values ( '" + UUID.randomUUID().toString() + "','Team B')";
        db.execSQL(INSERT_TEAM_B);
        String INSERT_TEAM_C = "INSERT INTO  " + Team.TABLE + " (" + Team.GUID + ", " + Team.NAME + ") values ( '" + UUID.randomUUID().toString() + "','Team C')";
        db.execSQL(INSERT_TEAM_C);
        String INSERT_TEAM_D = "INSERT INTO  " + Team.TABLE + " (" + Team.GUID + ", " + Team.NAME + ") values ( '" + UUID.randomUUID().toString() + "','Team D')";
        db.execSQL(INSERT_TEAM_D);
        String INSERT_TEAM_E = "INSERT INTO  " + Team.TABLE + " (" + Team.GUID + ", " + Team.NAME + ") values ( '" + UUID.randomUUID().toString() + "','Team E')";
        db.execSQL(INSERT_TEAM_E);

        //Insert Outlet
        String INSERT_OUTLET_1 = "INSERT INTO  " + Outlet.TABLE + " (" + Outlet.GUID + ", " + Outlet.KODE + ", " + Outlet.NAME + ") values ( '" + UUID.randomUUID().toString() + "', '111', 'Outlet 1')";
        db.execSQL(INSERT_OUTLET_1);
        String INSERT_OUTLET_2 = "INSERT INTO  " + Outlet.TABLE + " (" + Outlet.GUID + ", " + Outlet.KODE + ", " + Outlet.NAME + ") values ( '" + UUID.randomUUID().toString() + "', '112', 'Outlet 2')";
        db.execSQL(INSERT_OUTLET_2);
        String INSERT_OUTLET_3 = "INSERT INTO  " + Outlet.TABLE + " (" + Outlet.GUID + ", " + Outlet.KODE + ", " + Outlet.NAME + ") values ( '" + UUID.randomUUID().toString() + "', '115', 'Outlet 3')";
        db.execSQL(INSERT_OUTLET_3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if existed, all data will be gone
        db.execSQL("DROP TABLE IF EXISTS " + Team.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Outlet.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Order.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Retur.TABLE);

        //Create tables again
        onCreate(db);
    }
}

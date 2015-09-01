package com.bali.nusadua.productmonitor.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bali.nusadua.productmonitor.model.Billing;
import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.model.OrderHeader;
import com.bali.nusadua.productmonitor.model.OrderItem;
import com.bali.nusadua.productmonitor.model.Outlet;
import com.bali.nusadua.productmonitor.model.ReturHeader;
import com.bali.nusadua.productmonitor.model.ReturItem;
import com.bali.nusadua.productmonitor.model.SettlementHeader;
import com.bali.nusadua.productmonitor.model.SettlementItem;
import com.bali.nusadua.productmonitor.model.StaffBilling;
import com.bali.nusadua.productmonitor.model.StockBilling;
import com.bali.nusadua.productmonitor.model.StockPrice;
import com.bali.nusadua.productmonitor.model.Team;

import java.util.UUID;

/**
 * Created by desu sudarsana on 4/12/2015.
 */
public class DBHelper extends SQLiteOpenHelper{
    //Version number to upgrade database version
    private static final int DATABASE_VERSION = 2;

    //Database Name
    private static final String DATABASE_NAME = "mobilesalesman.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Team.CREATE_TABLE());
        db.execSQL(Outlet.CREATE_TABLE());
        db.execSQL(OrderHeader.CREATE_TABLE());
        db.execSQL(OrderItem.CREATE_TABLE());
        db.execSQL(ReturHeader.CREATE_TABLE());
        db.execSQL(ReturItem.CREATE_TABLE());
        db.execSQL(SettlementHeader.CREATE_TABLE());
        db.execSQL(SettlementItem.CREATE_TABLE());
        db.execSQL(StaffBilling.CREATE_TABLE());
        db.execSQL(StockBilling.CREATE_TABLE());
        db.execSQL(StockPrice.CREATE_TABLE());
        db.execSQL(Customer.CREATE_TABLE());
        db.execSQL(Billing.CREATE_TABLE());

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
        db.execSQL("DROP TABLE IF EXISTS " + OrderItem.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ReturItem.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SettlementItem.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + StaffBilling.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + StockBilling.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + StockPrice.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Customer.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Billing.TABLE);

        //Create tables again
        onCreate(db);
    }
}

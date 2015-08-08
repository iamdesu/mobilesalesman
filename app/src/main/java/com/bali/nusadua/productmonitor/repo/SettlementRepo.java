package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bali.nusadua.productmonitor.model.Outlet;
import com.bali.nusadua.productmonitor.model.Settlement;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SettlementRepo {
    private DBHelper dbHelper;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    public SettlementRepo(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    public int insert(Settlement settlement) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        settlement.setGuid(UUID.randomUUID().toString());
        values.put(Settlement.GUID, settlement.getGuid());
        values.put(Settlement.INVOICE_NUMBER, settlement.getInvoiceNumber());
        values.put(Settlement.INVOICE_DATE, sdf.format(settlement.getInvoiceDate()));
        values.put(Settlement.CREDIT, settlement.getCredit());
        values.put(Settlement.PAYMENT_METHOD, settlement.getPaymentMethod());
        values.put(Settlement.NOMINAL_PAYMENT, settlement.getNominalPayment());
        values.put(Settlement.KODE_OUTLET, settlement.getKodeOutlet());
        values.put(Settlement.CREATE_DATE, sdf.format(new Date()));

        //Inserting row
        long settlement_id = db.insert(Settlement.TABLE, null, values);
        db.close();
        return (int) settlement_id;
    }

    public void insertAll(List<Settlement> settlements) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (Settlement settlement : settlements) {
            ContentValues contentValues = new ContentValues();

            settlement.setGuid(UUID.randomUUID().toString());
            contentValues.put(Settlement.GUID, settlement.getGuid());
            contentValues.put(Settlement.INVOICE_NUMBER, settlement.getInvoiceNumber());
            contentValues.put(Settlement.INVOICE_DATE, sdf.format(settlement.getInvoiceDate()));
            contentValues.put(Settlement.CREDIT, settlement.getCredit());
            contentValues.put(Settlement.PAYMENT_METHOD, settlement.getPaymentMethod());
            contentValues.put(Settlement.NOMINAL_PAYMENT, settlement.getNominalPayment());
            contentValues.put(Settlement.KODE_OUTLET, settlement.getKodeOutlet());
            contentValues.put(Settlement.CREATE_DATE, sdf.format(new Date()));

            db.insert(Settlement.TABLE, null, contentValues);
        }
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Settlement.TABLE, null, null);
        db.close();
    }

    public void update(Settlement settlement) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Settlement.INVOICE_NUMBER, settlement.getInvoiceNumber());
        contentValues.put(Settlement.INVOICE_DATE, sdf.format(settlement.getInvoiceDate()));
        contentValues.put(Settlement.CREDIT, settlement.getCredit());
        contentValues.put(Settlement.PAYMENT_METHOD, settlement.getPaymentMethod());
        contentValues.put(Settlement.NOMINAL_PAYMENT, settlement.getNominalPayment());
        contentValues.put(Settlement.KODE_OUTLET, settlement.getKodeOutlet());

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(Settlement.TABLE, contentValues, Settlement.GUID + "= ?", new String[]{settlement.getGuid()});
        db.close();
    }

    public Settlement findByGUID(String guid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                Settlement.ID + ", " +
                Settlement.GUID + ", " +
                Settlement.INVOICE_NUMBER + ", " +
                Settlement.INVOICE_DATE + ", " +
                Settlement.CREDIT + ", " +
                Settlement.PAYMENT_METHOD + ", " +
                Settlement.NOMINAL_PAYMENT + ", " +
                Settlement.KODE_OUTLET + ", " +
                Settlement.CREATE_DATE + " FROM " +
                Settlement.TABLE + " WHERE " +
                Settlement.GUID + " = ?";

        Settlement settlement = new Settlement();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{guid});

        if (cursor.moveToFirst()) {
            do {
                settlement.setId(cursor.getInt(cursor.getColumnIndex(Settlement.ID)));
                settlement.setGuid(cursor.getString(cursor.getColumnIndex(Settlement.GUID)));
                settlement.setInvoiceNumber(cursor.getString(cursor.getColumnIndex(Settlement.INVOICE_NUMBER)));
                try {
                    Date invoiceDate = sdf.parse(cursor.getString(cursor.getColumnIndex(Settlement.INVOICE_DATE)));
                    settlement.setInvoiceDate(invoiceDate);
                } catch (ParseException e) {
                    settlement.setInvoiceDate(null);
                }
                settlement.setCredit(cursor.getLong(cursor.getColumnIndex(Settlement.CREDIT)));
                settlement.setPaymentMethod(cursor.getString(cursor.getColumnIndex(Settlement.PAYMENT_METHOD)));
                settlement.setNominalPayment(cursor.getLong(cursor.getColumnIndex(Settlement.NOMINAL_PAYMENT)));
                settlement.setKodeOutlet(cursor.getString(cursor.getColumnIndex(Settlement.KODE_OUTLET)));

                try {
                    Date createdDate = sdf.parse(cursor.getString(cursor.getColumnIndex(Settlement.CREATE_DATE)));
                    settlement.setCreatedDate(createdDate);
                } catch (ParseException e) {
                    settlement.setCreatedDate(null);
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return settlement;
    }

    /**
     * Retrieve all records and populate List<Retur>
     */
    public List<Settlement> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Settlement.TABLE;

        List<Settlement> settlements = new ArrayList<Settlement>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list  (cursor.getColumnIndex(Team.ID
        if (cursor.moveToFirst()) {
            do {
                Settlement settlement = new Settlement();
                settlement.setId((int) cursor.getLong(cursor.getColumnIndex(Settlement.ID)));
                settlement.setGuid(cursor.getString(cursor.getColumnIndex(Settlement.GUID)));
                settlement.setInvoiceNumber(cursor.getString(cursor.getColumnIndex(Settlement.INVOICE_NUMBER)));
                try {
                    Date invoiceDate = sdf.parse(cursor.getString(cursor.getColumnIndex(Settlement.INVOICE_DATE)));
                    settlement.setInvoiceDate(invoiceDate);
                } catch (ParseException e) {
                    settlement.setInvoiceDate(null);
                }
                settlement.setCredit(cursor.getLong(cursor.getColumnIndex(Settlement.CREDIT)));
                settlement.setPaymentMethod(cursor.getString(cursor.getColumnIndex(Settlement.PAYMENT_METHOD)));
                settlement.setNominalPayment(cursor.getLong(cursor.getColumnIndex(Settlement.NOMINAL_PAYMENT)));
                settlement.setKodeOutlet(cursor.getString(cursor.getColumnIndex(Settlement.KODE_OUTLET)));

                try {
                    Date createDate = sdf.parse(cursor.getString(cursor.getColumnIndex(Settlement.CREATE_DATE)));
                    settlement.setCreatedDate(createDate);
                } catch (ParseException e) {
                    settlement.setCreatedDate(null);
                }

                settlements.add(settlement);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return settlements;
    }

    public List<Settlement> getAllWithOutlet() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                Settlement.TABLE + "." + Settlement.ID + ", " +
                Settlement.TABLE + "." + Settlement.GUID + ", " +
                Settlement.TABLE + "." + Settlement.INVOICE_NUMBER + ", " +
                Settlement.TABLE + "." + Settlement.INVOICE_DATE + ", " +
                Settlement.TABLE + "." + Settlement.CREDIT + ", " +
                Settlement.TABLE + "." + Settlement.PAYMENT_METHOD + ", " +
                Settlement.TABLE + "." + Settlement.NOMINAL_PAYMENT + ", " +
                Settlement.TABLE + "." + Settlement.KODE_OUTLET + ", " +
                Settlement.TABLE + "." + Settlement.CREATE_DATE + ", " +
                Outlet.TABLE + "." + Outlet.ID + " as outletID, " +
                Outlet.TABLE + "." + Outlet.GUID + " as outletGUID, " +
                Outlet.TABLE + "." + Outlet.KODE + " as outletKode, " +
                Outlet.TABLE + "." + Outlet.NAME + " as outletName" +
                " FROM " + Settlement.TABLE + " LEFT JOIN " + Outlet.TABLE +
                " ON " + Settlement.TABLE + "." + Settlement.KODE_OUTLET + " = " + Outlet.TABLE + "." + Outlet.KODE;

        List<Settlement> settlements = new ArrayList<Settlement>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Settlement settlement = new Settlement();
                settlement.setId((int) cursor.getLong(cursor.getColumnIndex(Settlement.ID)));
                settlement.setGuid(cursor.getString(cursor.getColumnIndex(Settlement.GUID)));
                settlement.setInvoiceNumber(cursor.getString(cursor.getColumnIndex(Settlement.INVOICE_NUMBER)));
                try {
                    Date invoiceDate = sdf.parse(cursor.getString(cursor.getColumnIndex(Settlement.INVOICE_DATE)));
                    settlement.setInvoiceDate(invoiceDate);
                } catch (ParseException e) {
                    settlement.setInvoiceDate(null);
                }
                settlement.setCredit(cursor.getLong(cursor.getColumnIndex(Settlement.CREDIT)));
                settlement.setPaymentMethod(cursor.getString(cursor.getColumnIndex(Settlement.PAYMENT_METHOD)));
                settlement.setNominalPayment(cursor.getLong(cursor.getColumnIndex(Settlement.NOMINAL_PAYMENT)));
                settlement.setKodeOutlet(cursor.getString(cursor.getColumnIndex(Settlement.KODE_OUTLET)));

                try {
                    Date createDate = sdf.parse(cursor.getString(cursor.getColumnIndex(Settlement.CREATE_DATE)));
                    settlement.setCreatedDate(createDate);
                } catch (ParseException e) {
                    settlement.setCreatedDate(null);
                }

                Outlet outlet = new Outlet();
                outlet.setId((int) cursor.getLong(cursor.getColumnIndex("outletID")));
                outlet.setGuid(cursor.getString(cursor.getColumnIndex("outletGUID")));
                outlet.setKode(cursor.getString(cursor.getColumnIndex("outletKode")));
                outlet.setName(cursor.getString(cursor.getColumnIndex("outletName")));

                settlement.setOutlet(outlet);

                settlements.add(settlement);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return settlements;
    }

    public List<Settlement> getSettlementByCustomer(String customerId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                Settlement.ID + ", " +
                Settlement.GUID + ", " +
                Settlement.INVOICE_NUMBER + ", " +
                Settlement.INVOICE_DATE + ", " +
                Settlement.CREDIT + ", " +
                Settlement.PAYMENT_METHOD + ", " +
                Settlement.NOMINAL_PAYMENT + ", " +
                Settlement.KODE_OUTLET + ", " +
                Settlement.CREATE_DATE + " FROM " +
                Settlement.TABLE + " WHERE " +
                Settlement.KODE_OUTLET + " = ?";

        List<Settlement> settlements = new ArrayList<Settlement>();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{customerId});

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Settlement settlement = new Settlement();
                settlement.setId((int) cursor.getLong(cursor.getColumnIndex(Settlement.ID)));
                settlement.setGuid(cursor.getString(cursor.getColumnIndex(Settlement.GUID)));
                settlement.setInvoiceNumber(cursor.getString(cursor.getColumnIndex(Settlement.INVOICE_NUMBER)));
                try {
                    Date invoiceDate = sdf.parse(cursor.getString(cursor.getColumnIndex(Settlement.INVOICE_DATE)));
                    settlement.setInvoiceDate(invoiceDate);
                } catch (ParseException e) {
                    settlement.setInvoiceDate(null);
                }
                settlement.setCredit(cursor.getLong(cursor.getColumnIndex(Settlement.CREDIT)));
                settlement.setPaymentMethod(cursor.getString(cursor.getColumnIndex(Settlement.PAYMENT_METHOD)));
                settlement.setNominalPayment(cursor.getLong(cursor.getColumnIndex(Settlement.NOMINAL_PAYMENT)));
                settlement.setKodeOutlet(cursor.getString(cursor.getColumnIndex(Settlement.KODE_OUTLET)));

                try {
                    Date createDate = sdf.parse(cursor.getString(cursor.getColumnIndex(Settlement.CREATE_DATE)));
                    settlement.setCreatedDate(createDate);
                } catch (ParseException e) {
                    settlement.setCreatedDate(null);
                }

                settlements.add(settlement);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return settlements;
    }

    public List<String> getCustomerOnSettlement() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT DISTINCT " +
                Settlement.KODE_OUTLET + " FROM " +
                Settlement.TABLE;

        List<String> listCustomerID = new ArrayList<String>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String customerID = cursor.getString(cursor.getColumnIndex(Settlement.KODE_OUTLET));
                listCustomerID.add(customerID);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listCustomerID;
    }
}

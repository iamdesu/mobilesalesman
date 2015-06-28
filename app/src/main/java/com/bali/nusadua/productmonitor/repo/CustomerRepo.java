package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.model.Order;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerRepo {
    private DBHelper dbHelper;

    public CustomerRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(Customer customer) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Customer.CUST_ID, customer.getCustomerId());
        values.put(Customer.COMPANY_NAME, customer.getCompanyName());
        values.put(Customer.PERSON_NAME, customer.getPersonName());
        values.put(Customer.ADDRESS, customer.getAddress());
        values.put(Customer.REGION, customer.getRegion());
        values.put(Customer.CITY, customer.getCity());
        values.put(Customer.VISIT, customer.getVisit());
        values.put(Customer.PRICE_LEVEL, customer.getPriceLevel());

        //Inserting row
        long id = db.insert(Customer.TABLE, null, values);
        db.close();
        return (int) id;
    }

    public Customer findByCustomerID(String customerID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                Customer.ID + ", " +
                Customer.CUST_ID + ", " +
                Customer.COMPANY_NAME + ", " +
                Customer.PERSON_NAME + ", " +
                Customer.ADDRESS + ", " +
                Customer.REGION + ", " +
                Customer.CITY + ", " +
                Customer.VISIT + ", " +
                Customer.PRICE_LEVEL + " FROM " +
                Customer.TABLE + " WHERE " +
                Customer.CUST_ID + " = ?";

        Customer customer = new Customer();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{ customerID } );

        if(cursor.moveToFirst()) {
            do {
                customer.setId(cursor.getInt(cursor.getColumnIndex(Customer.ID)));
                customer.setCustomerId(cursor.getString(cursor.getColumnIndex(Customer.CUST_ID)));
                customer.setCompanyName(cursor.getString(cursor.getColumnIndex(Customer.COMPANY_NAME)));
                customer.setPersonName(cursor.getString(cursor.getColumnIndex(Customer.PERSON_NAME)));
                customer.setAddress(cursor.getString(cursor.getColumnIndex(Customer.ADDRESS)));
                customer.setRegion(cursor.getString(cursor.getColumnIndex(Customer.REGION)));
                customer.setCity(cursor.getString(cursor.getColumnIndex(Customer.CITY)));
                customer.setVisit(cursor.getString(cursor.getColumnIndex(Customer.VISIT)));
                customer.setPriceLevel(cursor.getInt(cursor.getColumnIndex(Customer.PRICE_LEVEL)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return customer;
    }

    //Retrieve all records and populate List<Order>
    public List<Customer> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Customer.TABLE + " ORDER BY " + Customer.COMPANY_NAME + " ASC";

        List<Customer> listCustomer = new ArrayList<Customer>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list  (cursor.getColumnIndex(Team.ID
        if(cursor.moveToFirst()){
            do {
                Customer customer = new Customer();
                customer.setId((int) cursor.getLong(cursor.getColumnIndex(Customer.ID)));
                customer.setCustomerId(cursor.getString(cursor.getColumnIndex(Customer.CUST_ID)));
                customer.setCompanyName(cursor.getString(cursor.getColumnIndex(Customer.COMPANY_NAME)));
                customer.setPersonName(cursor.getString(cursor.getColumnIndex(Customer.PERSON_NAME)));
                customer.setAddress(cursor.getString(cursor.getColumnIndex(Customer.ADDRESS)));
                customer.setRegion(cursor.getString(cursor.getColumnIndex(Customer.REGION)));
                customer.setCity(cursor.getString(cursor.getColumnIndex(Customer.CITY)));
                customer.setVisit(cursor.getString(cursor.getColumnIndex(Customer.VISIT)));
                customer.setPriceLevel(cursor.getInt(cursor.getColumnIndex(Customer.PRICE_LEVEL)));

                listCustomer.add(customer);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listCustomer;
    }
}

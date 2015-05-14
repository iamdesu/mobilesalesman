package com.bali.nusadua.productmonitor.dropbox;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.bali.nusadua.productmonitor.model.StaffBilling;
import com.bali.nusadua.productmonitor.model.StockBilling;
import com.bali.nusadua.productmonitor.model.StockPrice;
import com.bali.nusadua.productmonitor.repo.StaffBillingRepo;
import com.bali.nusadua.productmonitor.repo.StockBillingRepo;
import com.bali.nusadua.productmonitor.repo.StockPriceRepo;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class DownloadDataFromDropbox extends AsyncTask<Void, Void, Boolean> {
    private DropboxAPI<?> dropbox;
    private String path;
    private Context context;

    public DownloadDataFromDropbox(Context context, DropboxAPI<?> dropbox, String path) {
        this.context = context.getApplicationContext();
        this.dropbox = dropbox;
        this.path = path;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            readStaffBilling();
            readStockBilling();
            readStockPrice();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DropboxException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Toast.makeText(context, "File Berhasil di unduh!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Gagal mengunduh file!", Toast.LENGTH_LONG).show();
        }
    }

    private void readOutlet() throws IOException, DropboxException {
        final File tempDir = context.getCacheDir();
        File file = File.createTempFile("TeamA-Outlet", ".csv", tempDir);
        FileOutputStream outputStream = new FileOutputStream(file);
        DropboxAPI.DropboxFileInfo info = dropbox.getFile(path + "TeamA-Outlet.csv", null, outputStream, null);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = "";
        while ((line = br.readLine()) != null) {

            // use comma as separator
            String[] country = line.split(",");

            //maps.put(country[4], country[5]);

            Log.i("Data outlet", line);

        }
    }

    private void readStaffBilling() throws IOException, DropboxException {
        final File tempDir = context.getCacheDir();
        File tempfile = File.createTempFile("Staff_B", ".csv", tempDir);
        FileOutputStream outputStream = new FileOutputStream(tempfile);
        DropboxAPI.DropboxFileInfo info = dropbox.getFile(path + "Staff_B.csv", null, outputStream, null);

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + StaffBilling.TABLE);
        db.execSQL("DELETE FROM sqlite_sequence where name='" + StaffBilling.TABLE + "'");
        db.close();

        StaffBillingRepo staffBillingRepo = new StaffBillingRepo(context);
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(tempfile));
            String line = "";
            boolean first = true; //First is column name
            while ((line = br.readLine()) != null && br.readLine().trim() != "") {
                //Log.i("Data outlet", line);

                if (!first) {
                    // use | as separator
                    String[] data = line.split("\\|");

                    StaffBilling staffBilling = new StaffBilling();
                    staffBilling.setStaff(data[0]);
                    staffBilling.setStaffName(data[1]);
                    staffBilling.setPassword(data[2]);
                    staffBilling.setLevel(data[3].trim().equals("") ? null : Integer.valueOf(data[3]));
                    staffBilling.setUserID(data[4]);
                    staffBilling.setTeam(data[5]);
                    staffBilling.setExpire(data[6].trim().equals("") ? null : Integer.valueOf(data[6]));

                    staffBillingRepo.insert(staffBilling);
                } else {
                    first = false;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                tempfile.delete();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void readStockBilling() throws IOException, DropboxException {
        final File tempDir = context.getCacheDir();
        File tempfile = File.createTempFile("Stock_B", ".csv", tempDir);
        FileOutputStream outputStream = new FileOutputStream(tempfile);
        DropboxAPI.DropboxFileInfo info = dropbox.getFile(path + "Stock_B.csv", null, outputStream, null);

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + StockBilling.TABLE);
        db.execSQL("DELETE FROM sqlite_sequence where name='" + StockBilling.TABLE + "'");
        db.close();

        StockBillingRepo stockBillingRepo = new StockBillingRepo(context);
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(tempfile));
            String line = "";
            boolean first = true; //First is column name
            while ((line = br.readLine()) != null && br.readLine().trim() != "") {

                if (!first) {
                    // use | as separator
                    String[] data = line.split("\\|");
                    Log.i("Data outlet", line + " = " + data.length);

                    StockBilling stockBilling = new StockBilling();
                    stockBilling.setStockId(data[0]);
                    stockBilling.setScode(data[1]);
                    stockBilling.setDescription(data[2]);

                    stockBillingRepo.insert(stockBilling);
                } else {
                    first = false;
                }

            }
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                tempfile.delete();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void readStockPrice() throws IOException, DropboxException {
        final File tempDir = context.getCacheDir();
        File tempfile = File.createTempFile("Stockprice_B", ".csv", tempDir);
        FileOutputStream outputStream = new FileOutputStream(tempfile);
        DropboxAPI.DropboxFileInfo info = dropbox.getFile(path + "Stockprice_B.csv", null, outputStream, null);

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + StockPrice.TABLE);
        db.execSQL("DELETE FROM sqlite_sequence where name='" + StockPrice.TABLE + "'");
        db.close();

        StockPriceRepo stockPriceRepo = new StockPriceRepo(context);
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(tempfile));
            String line = "";
            boolean first = true; //First is column name
            while ((line = br.readLine()) != null && br.readLine().trim() != "") {

                if (!first) {
                    // use | as separator
                    String[] data = line.split("\\|");

                    StockPrice stockPrice = new StockPrice();
                    stockPrice.setStockId(data[0]);
                    stockPrice.setPriceLevel(data[1].trim().equals("") ? null : Integer.valueOf(data[1]));
                    stockPrice.setPrice(data[2].trim().equals("") ? null : Double.valueOf(data[2]));

                    stockPriceRepo.insert(stockPrice);
                } else {
                    first = false;
                }

            }
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                tempfile.delete();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
package com.bali.nusadua.productmonitor.dropbox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.bali.nusadua.productmonitor.MSConstantsIntf;
import com.bali.nusadua.productmonitor.R;
import com.bali.nusadua.productmonitor.model.Billing;
import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.model.OrderItem;
import com.bali.nusadua.productmonitor.model.ReturItem;
import com.bali.nusadua.productmonitor.model.SettlementItem;
import com.bali.nusadua.productmonitor.model.StaffBilling;
import com.bali.nusadua.productmonitor.model.StockBilling;
import com.bali.nusadua.productmonitor.model.StockPrice;
import com.bali.nusadua.productmonitor.repo.BillingRepo;
import com.bali.nusadua.productmonitor.repo.CustomerRepo;
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
    private static final String DELIMITED = "\\|";

    private DropboxAPI<?> dropbox;
    private String path;
    private Context context;
    private ProgressDialog progressBar;
    private Boolean isAllDownload = true, isCloseActivity;
    private String team;
    private Activity currentActivity;

    public DownloadDataFromDropbox(Activity currentActivity, DropboxAPI<?> dropbox, String path, Boolean isAllDownload, String team, ProgressDialog progressBar, boolean isCloseActivity) {
        this.currentActivity = currentActivity;
        this.context = currentActivity.getApplicationContext();
        this.dropbox = dropbox;
        this.path = path;
        this.progressBar = progressBar;
        this.isAllDownload = isAllDownload;
        this.isCloseActivity = isCloseActivity;
        this.team = team;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            if (isAllDownload == true) {
                clearOrder();
                clearRetur();
                clearSettlement();

                readStaffBilling();
                progressBar.setProgress(20);
                readStockBilling();
                progressBar.setProgress(40);
                readStockPrice();
                progressBar.setProgress(60);

                SharedPreferences prefs = context.getSharedPreferences(MSConstantsIntf.MOBILESALES_PREFS_NAME, 0);
                String team = prefs.getString(MSConstantsIntf.TEAM, null);

                readCustomer(team);
                progressBar.setProgress(80);
                readBilling(team);
                progressBar.setProgress(90);
            } else {
                readStaffBilling();
                progressBar.setProgress(30);
                readStockBilling();
                progressBar.setProgress(60);
                readStockPrice();
                progressBar.setProgress(90);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DropboxException e) {
            e.printStackTrace();
        } finally {
            progressBar.dismiss();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Toast.makeText(context, context.getResources().getString(R.string.download_success), Toast.LENGTH_LONG).show();
            if (isCloseActivity) {
                currentActivity.finish();
            }
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.download_failed), Toast.LENGTH_LONG).show();
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
            String[] country = line.split(DELIMITED, -1);

            //maps.put(country[4], country[5]);

            Log.i("Data outlet", line);

        }
    }

    private void clearOrder() {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + OrderItem.TABLE);
        db.execSQL("DELETE FROM sqlite_sequence where name='" + OrderItem.TABLE + "'");
        db.close();
    }

    private void clearRetur() {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + ReturItem.TABLE);
        db.execSQL("DELETE FROM sqlite_sequence where name='" + ReturItem.TABLE + "'");
        db.close();
    }

    private void clearSettlement() {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + SettlementItem.TABLE);
        db.execSQL("DELETE FROM sqlite_sequence where name='" + SettlementItem.TABLE + "'");
        db.close();
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
            while ((line = br.readLine()) != null) {
                //Log.i("Data outlet", line);

                if (!first && line.trim() != "" && !line.isEmpty()) {
                    String[] data = line.split(DELIMITED, -1);

                    //Do not accept empty data
                    if (!data[0].trim().isEmpty() && !data[1].trim().isEmpty() && !data[2].isEmpty() && !data[4].trim().isEmpty() && !data[5].trim().isEmpty()) {
                        StaffBilling staffBilling = new StaffBilling();
                        staffBilling.setStaff(data[0].trim());
                        staffBilling.setStaffName(data[1].trim());
                        staffBilling.setPassword(data[2]);
                        staffBilling.setLevel(data[3].trim().equals("") ? null : Integer.valueOf(data[3]));
                        staffBilling.setUserID(data[4].trim());
                        staffBilling.setTeam(data[5].trim());
                        staffBilling.setExpire(data[6].trim().equals("") ? null : Integer.valueOf(data[6]));

                        staffBillingRepo.insert(staffBilling);
                    }
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
            boolean first = true; //First is column name / column header
            while ((line = br.readLine()) != null) {

                if (!first && line.trim() != "" && !line.isEmpty()) {
                    // use | as separator
                    String[] data = line.split(DELIMITED, -1);

                    //Do not accept empty data
                    if (!isEmptyData(data[0]) && !isEmptyData(data[1]) && !isEmptyData(data[2])) {
                        StockBilling stockBilling = new StockBilling();
                        stockBilling.setStockId(data[0].trim());
                        stockBilling.setScode(data[1].trim());
                        stockBilling.setDescription(data[2].trim());

                        stockBillingRepo.insert(stockBilling);
                    }

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
            while ((line = br.readLine()) != null) {

                if (!first && line.trim() != "" && !line.isEmpty()) {
                    // use | as separator
                    String[] data = line.split(DELIMITED, -1);

                    //Do not accept empty data
                    if (!isEmptyData(data[0]) && !isEmptyData(data[1]) && !isEmptyData(data[2])) {
                        StockPrice stockPrice = new StockPrice();
                        stockPrice.setStockId(data[0].trim());
                        stockPrice.setPriceLevel(data[1].trim().equals("") ? null : Integer.valueOf(data[1]));
                        stockPrice.setPrice(data[2].trim().equals("") ? null : Double.valueOf(data[2]));

                        stockPriceRepo.insert(stockPrice);
                    }

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

    private void readCustomer(String team) throws IOException, DropboxException {
        final File tempDir = context.getCacheDir();
        File tempfile = File.createTempFile("Cust_" + team + "_B", ".csv", tempDir);
        FileOutputStream outputStream = new FileOutputStream(tempfile);
        DropboxAPI.DropboxFileInfo info = dropbox.getFile(path + "Cust_" + team + "_B.csv", null, outputStream, null);

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + Customer.TABLE);
        db.execSQL("DELETE FROM sqlite_sequence where name='" + Customer.TABLE + "'");
        db.close();

        CustomerRepo customerRepo = new CustomerRepo(context);
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(tempfile));
            String line = "";
            boolean first = true; //First is column name
            while ((line = br.readLine()) != null) {

                if (!first && line.trim() != "" && !line.isEmpty()) {
                    // use | as separator
                    String[] data = line.split(DELIMITED, -1);

                    //Do not accept empty data
                    if (!isEmptyData(data[0]) && !isEmptyData(data[1])) {
                        Customer customer = new Customer();
                        customer.setCustomerId(data[0].trim());
                        customer.setCompanyName(data[1].trim());
                        customer.setPersonName(data[2].trim());
                        customer.setAddress(data[3].trim());
                        customer.setRegion(data[4].trim());
                        customer.setCity(data[5].trim());
                        customer.setVisit(data[6].trim());
                        customer.setPriceLevel(data[7].trim().equals("") ? null : Integer.valueOf(data[7]));

                        customerRepo.insert(customer);
                    }

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

    private void readBilling(String team) throws IOException, DropboxException {
        final File tempDir = context.getCacheDir();
        File tempfile = File.createTempFile("Billing_" + team + "_B", ".csv", tempDir);
        FileOutputStream outputStream = new FileOutputStream(tempfile);
        DropboxAPI.DropboxFileInfo info = dropbox.getFile(path + "Billing_" + team + "_B.csv", null, outputStream, null);

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + Billing.TABLE);
        db.execSQL("DELETE FROM sqlite_sequence where name='" + Billing.TABLE + "'");
        db.close();

        BillingRepo billingRepo = new BillingRepo(context);
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(tempfile));
            String line = "";
            boolean first = true; //First is column name
            while ((line = br.readLine()) != null) {

                if (!first && line.trim() != "" && !line.isEmpty()) {
                    // use | as separator
                    String[] data = line.split(DELIMITED, -1);

                    //Do not accept empty data
                    if (!isEmptyData(data[0]) && !isEmptyData(data[1]) && !isEmptyData(data[2]) && !isEmptyData(data[3])) {
                        Billing billing = new Billing();
                        billing.setInvoiceNo(data[0].trim());
                        billing.setCustomerId(data[1].trim());
                        billing.setTotalAmount(data[2].trim().equals("") ? null : Double.valueOf(data[2]));
                        billing.setPaidAmount(data[3].trim().equals("") ? null : Double.valueOf(data[3]));

                        billingRepo.insert(billing);
                    }

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

    private boolean isEmptyData(String data) {
        return data.trim().isEmpty() || data.trim().equals("");
    }
}
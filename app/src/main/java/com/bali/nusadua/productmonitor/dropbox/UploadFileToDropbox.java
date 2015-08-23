package com.bali.nusadua.productmonitor.dropbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.bali.nusadua.productmonitor.MSConstantsIntf;
import com.bali.nusadua.productmonitor.model.OrderItem;
import com.bali.nusadua.productmonitor.model.Retur;
import com.bali.nusadua.productmonitor.model.Settlement;
import com.bali.nusadua.productmonitor.modelView.OrderHeaderItemView;
import com.bali.nusadua.productmonitor.repo.OrderHeaderRepo;
import com.bali.nusadua.productmonitor.repo.OrderRepo;
import com.bali.nusadua.productmonitor.repo.ReturRepo;
import com.bali.nusadua.productmonitor.repo.SettlementRepo;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Langit_P on 4/21/2015.
 */
public class UploadFileToDropbox extends AsyncTask<Void, Void, Boolean> {
    private DropboxAPI<?> dropbox;
    private String path;
    private Context context;
    private ProgressDialog progressBar;

    private OrderHeaderRepo orderHeaderRepo;
    private OrderRepo orderRepo;
    private ReturRepo returRepo;
    private SettlementRepo settlementRepo;

    public UploadFileToDropbox(Context context, DropboxAPI<?> dropbox,
                               String path, ProgressDialog progressBar) {
        this.context = context.getApplicationContext();
        this.dropbox = dropbox;
        this.path = path;
        this.progressBar = progressBar;
        this.orderHeaderRepo = new OrderHeaderRepo(this.context);
        this.orderRepo = new OrderRepo(this.context);
        this.returRepo = new ReturRepo(this.context);
        this.settlementRepo = new SettlementRepo(this.context);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            SharedPreferences prefs = this.context.getSharedPreferences(MSConstantsIntf.MOBILESALES_PREFS_NAME, 0);
            String team = prefs.getString(MSConstantsIntf.TEAM, null);

            progressBar.setProgress(30);
            uploadOrderTable(team);
            progressBar.setProgress(60);
            uploadReturTable(team);
            progressBar.setProgress(90);
            uploadSettlementTable(team);
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
            Toast.makeText(context, "File Berhasil di unggah!",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Gagal mengunggah file", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void uploadOrderTable(String team) throws IOException, DropboxException {
        List<String> customerIDs = orderHeaderRepo.getCustomerOnOrderHeader();
        for(String customerID : customerIDs) {
            uploadCustomerOrderTable(team, customerID);
        }

        final File tempDir = context.getCacheDir();
        File tempFile;
        FileWriter fr;

        List<OrderHeaderItemView> orders = orderHeaderRepo.getOrderHeaderItem();
    }

    private void uploadCustomerOrderTable(String team, String customerId) throws IOException, DropboxException {
        final File tempDir = context.getCacheDir();
        File tempFile;
        FileWriter fr;

        tempFile = File.createTempFile("file", ".csv", tempDir);
        fr = new FileWriter(tempFile);
        //OrderRepo orderRepo = new OrderRepo(context);
        List<OrderItem> orders = orderRepo.getOrderByCustomer(customerId);

        fr.append("GUID");
        fr.append(",");
        fr.append("KODE");
        fr.append(",");
        fr.append("Nama Barang");
        fr.append(",");
        fr.append("Harga");
        fr.append(",");
        fr.append("Qty");
        fr.append(",");
        fr.append("Unit");
        fr.append(",");
        fr.append("Kode Outlet");
        fr.append(",");
        fr.append("Create Date");
        fr.append('\n');

        for (OrderItem order : orders) {
            fr.append(order.getGuid());
            fr.append(",");
            fr.append(order.getKode());
            fr.append(",");
            fr.append(order.getNamaBarang());
            fr.append(",");
            fr.append(String.valueOf(order.getHarga()));
            fr.append(",");
            fr.append(String.valueOf(order.getQty()));
            fr.append(",");
            fr.append(order.getUnit());
            fr.append(",");
            fr.append(order.getKodeOutlet());
            fr.append(",");
            fr.append(String.valueOf(order.getCreateDate()));
            fr.append('\n');
        }

        fr.close();

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyMMdd");

        FileInputStream fileInputStream = new FileInputStream(tempFile);
        dropbox.putFileOverwrite(path + "ORDER_" + team + "_" + customerId + "_" + df.format(date) + ".csv", fileInputStream,
                tempFile.length(), null);
        tempFile.delete();
    }

    private void uploadReturTable(String team) throws IOException, DropboxException {
        List<String> customerIDs = returRepo.getCustomerOnRetur();
        for(String customerID : customerIDs) {
            uploadCustomerReturTable(team, customerID);
        }
    }

    private void uploadCustomerReturTable(String team, String customerId) throws IOException, DropboxException {
        final File tempDir = context.getCacheDir();
        File tempFile;
        FileWriter fr;

        tempFile = File.createTempFile("file", ".csv", tempDir);
        fr = new FileWriter(tempFile);
        //ReturRepo returRepo = new ReturRepo(context);
        List<Retur> returs = returRepo.getReturByCustomer(customerId);

        fr.append("GUID");
        fr.append(",");
        fr.append("KODE");
        fr.append(",");
        fr.append("Nama Barang");
        fr.append(",");
        fr.append("Harga");
        fr.append(",");
        fr.append("Qty");
        fr.append(",");
        fr.append("Unit");
        fr.append(",");
        fr.append("Kode Outlet");
        fr.append(",");
        fr.append("Create Date");
        fr.append('\n');

        for (Retur retur : returs) {
            fr.append(retur.getGuid());
            fr.append(",");
            fr.append(retur.getKode());
            fr.append(",");
            fr.append(retur.getNamaBarang());
            fr.append(",");
            fr.append(String.valueOf(retur.getHarga()));
            fr.append(",");
            fr.append(String.valueOf(retur.getQty()));
            fr.append(",");
            fr.append(retur.getUnit());
            fr.append(",");
            fr.append(retur.getKodeOutlet());
            fr.append(",");
            fr.append(String.valueOf(retur.getCreateDate()));
            fr.append('\n');
        }

        fr.close();

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyMMdd");

        FileInputStream fileInputStream = new FileInputStream(tempFile);
        dropbox.putFileOverwrite(path + "RETUR_" + team + "_" + customerId + "_" + df.format(date) + ".csv", fileInputStream,
                tempFile.length(), null);
        tempFile.delete();
    }

    private void uploadSettlementTable(String team) throws IOException, DropboxException {
        List<String> customerIDs = settlementRepo.getCustomerOnSettlement();
        for(String customerID : customerIDs) {
            uploadCustomerSettlementTable(team, customerID);
        }
    }

    private void uploadCustomerSettlementTable(String team, String customerId) throws IOException, DropboxException {
        final File tempDir = context.getCacheDir();
        File tempFile;
        FileWriter fr;

        tempFile = File.createTempFile("file", ".csv", tempDir);
        fr = new FileWriter(tempFile);
        //SettlementRepo settlementRepo = new SettlementRepo(context);
        List<Settlement> settlements = settlementRepo.getAll();
        DateFormat df = new SimpleDateFormat("yyMMdd");

        fr.append("GUID");
        fr.append(",");
        fr.append("Invoice Number");
        fr.append(",");
        fr.append("Invoice Date");
        fr.append(",");
        fr.append("Credit");
        fr.append(",");
        fr.append("Metode Pembayaran");
        fr.append(",");
        fr.append("Nominal Pembayaran");
        fr.append(",");
        fr.append("Kode Outlet");
        fr.append(",");
        fr.append("Create Date");
        fr.append('\n');

        for (Settlement settlement : settlements) {
            fr.append(settlement.getGuid());
            fr.append(",");
            fr.append(settlement.INVOICE_NUMBER);
            fr.append(",");
            fr.append(df.format(settlement.getInvoiceDate()));
            fr.append(",");
            fr.append(String.valueOf(settlement.getCredit()));
            fr.append(",");
            fr.append(settlement.getPaymentMethod());
            fr.append(",");
            fr.append(String.valueOf(settlement.getNominalPayment()));
            fr.append(",");
            fr.append(settlement.getKodeOutlet());
            fr.append(",");
            fr.append(String.valueOf(settlement.getCreatedDate()));
            fr.append('\n');
        }

        fr.close();

        Date date = new Date();

        FileInputStream fileInputStream = new FileInputStream(tempFile);
        dropbox.putFileOverwrite(path + "LUNAS_"+ team + "_" + customerId + "_" + df.format(date)+".csv", fileInputStream,
                tempFile.length(), null);
        tempFile.delete();
    }
}

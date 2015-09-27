package com.bali.nusadua.productmonitor.dropbox;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.bali.nusadua.productmonitor.MSConstantsIntf;
import com.bali.nusadua.productmonitor.R;
import com.bali.nusadua.productmonitor.model.OrderHeader;
import com.bali.nusadua.productmonitor.model.OrderItem;
import com.bali.nusadua.productmonitor.model.ReturHeader;
import com.bali.nusadua.productmonitor.model.ReturItem;
import com.bali.nusadua.productmonitor.model.SettlementHeader;
import com.bali.nusadua.productmonitor.model.SettlementItem;
import com.bali.nusadua.productmonitor.repo.OrderHeaderRepo;
import com.bali.nusadua.productmonitor.repo.OrderRepo;
import com.bali.nusadua.productmonitor.repo.ReturHeaderRepo;
import com.bali.nusadua.productmonitor.repo.ReturRepo;
import com.bali.nusadua.productmonitor.repo.SettlementHeaderRepo;
import com.bali.nusadua.productmonitor.repo.SettlementItemRepo;
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
    private ReturHeaderRepo returHeaderRepo;
    private ReturRepo returRepo;
    private SettlementHeaderRepo settlementHeaderRepo;
    private SettlementItemRepo settlementItemRepo;

    public UploadFileToDropbox(Context context, DropboxAPI<?> dropbox,
                               String path, ProgressDialog progressBar) {
        this.context = context;
        this.dropbox = dropbox;
        this.path = path;
        this.progressBar = progressBar;
        this.orderHeaderRepo = new OrderHeaderRepo(context.getApplicationContext());
        this.orderRepo = new OrderRepo(context.getApplicationContext());
        this.returRepo = new ReturRepo(context.getApplicationContext());
        this.returHeaderRepo = new ReturHeaderRepo(context.getApplicationContext());
        this.settlementItemRepo = new SettlementItemRepo(context.getApplicationContext());
        this.settlementHeaderRepo = new SettlementHeaderRepo(context.getApplicationContext());
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
            Toast.makeText(context, context.getResources().getString(R.string.upload_success),
                    Toast.LENGTH_LONG).show();
        } else {
            /*Toast.makeText(context, "Gagal mengunggah file", Toast.LENGTH_LONG)
                    .show();*/
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Do your Yes progress
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //Do your No progress
                            break;
                    }
                }
            };
            AlertDialog.Builder ab = new AlertDialog.Builder(context);
            ab.setMessage(context.getResources().getString(R.string.upload_failed)).setPositiveButton(context.getResources().getString(R.string.action_ok), dialogClickListener).show();
        }
    }

    private void uploadOrderTable(String team) throws IOException, DropboxException {
        List<String> customerIDs = orderHeaderRepo.getCustomerOnOrderHeader();
        for (String customerID : customerIDs) {
            uploadCustomerOrderTable(team, customerID);
        }
    }

    private void uploadCustomerOrderTable(String team, String customerId) throws IOException, DropboxException {
        final File tempDir = context.getCacheDir();
        File tempFile;
        FileWriter fr;

        tempFile = File.createTempFile("file", ".csv", tempDir);
        fr = new FileWriter(tempFile);
        List<OrderHeader> orderHeaders = orderHeaderRepo.getOrderHeaderItemByCustomer(customerId);

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

        for (OrderHeader orderHeader : orderHeaders) {
            for (OrderItem orderItem : orderHeader.getOrderItems()) {
                fr.append(orderItem.getGuid());
                fr.append(",");
                fr.append(orderItem.getKode());
                fr.append(",");
                fr.append(orderItem.getNamaBarang());
                fr.append(",");
                fr.append(String.valueOf(orderItem.getHarga()));
                fr.append(",");
                fr.append(String.valueOf(orderItem.getQty()));
                fr.append(",");
                fr.append(orderItem.getUnit());
                fr.append(",");
                fr.append(orderHeader.getKodeOutlet());
                fr.append(",");
                fr.append(String.valueOf(orderItem.getCreateDate()));
                fr.append('\n');
            }
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
        List<String> customerIDs = returHeaderRepo.getCustomerOnReturHeader();
        for (String customerID : customerIDs) {
            uploadCustomerReturTable(team, customerID);
        }
    }

    private void uploadCustomerReturTable(String team, String customerId) throws IOException, DropboxException {
        final File tempDir = context.getCacheDir();
        File tempFile;
        FileWriter fr;

        tempFile = File.createTempFile("file", ".csv", tempDir);
        fr = new FileWriter(tempFile);
        List<ReturHeader> returs = returHeaderRepo.getReturHeaderItemByCustomer(customerId);

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

        for (ReturHeader returHeader : returs) {
            for (ReturItem returItem : returHeader.getReturItems()) {
                fr.append(returItem.getGuid());
                fr.append(",");
                fr.append(returItem.getKode());
                fr.append(",");
                fr.append(returItem.getNamaBarang());
                fr.append(",");
                fr.append(String.valueOf(returItem.getHarga()));
                fr.append(",");
                fr.append(String.valueOf(returItem.getQty()));
                fr.append(",");
                fr.append(returItem.getUnit());
                fr.append(",");
                fr.append(returHeader.getKodeOutlet());
                fr.append(",");
                fr.append(String.valueOf(returItem.getCreateDate()));
                fr.append('\n');
            }
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
        List<String> customerIDs = settlementHeaderRepo.getCustomerOnSettlementHeader();
        for (String customerID : customerIDs) {
            uploadCustomerSettlementTable(team, customerID);
        }
    }

    private void uploadCustomerSettlementTable(String team, String customerId) throws IOException, DropboxException {
        final File tempDir = context.getCacheDir();
        File tempFile;
        FileWriter fr;

        tempFile = File.createTempFile("file", ".csv", tempDir);
        fr = new FileWriter(tempFile);
        //SettlementItemRepo settlementItemRepo = new SettlementItemRepo(context);
        List<SettlementHeader> settlementHeaders = settlementHeaderRepo.getSettlementHeaderItemByCustomer(customerId);
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


        for (SettlementHeader settlementHeader : settlementHeaders) {
            for (SettlementItem settlementItem : settlementHeader.getSettlementItems()) {
                fr.append(settlementItem.getGuid());
                fr.append(",");
                fr.append(settlementItem.INVOICE_NUMBER);
                fr.append(",");
                fr.append(df.format(settlementItem.getInvoiceDate()));
                fr.append(",");
                fr.append(String.valueOf(settlementItem.getCredit()));
                fr.append(",");
                fr.append(settlementItem.getPaymentMethod());
                fr.append(",");
                fr.append(String.valueOf(settlementItem.getNominalPayment()));
                fr.append(",");
                fr.append(settlementHeader.getKodeOutlet());
                fr.append(",");
                fr.append(String.valueOf(settlementItem.getCreatedDate()));
                fr.append('\n');
            }
        }

        fr.close();

        Date date = new Date();

        FileInputStream fileInputStream = new FileInputStream(tempFile);
        dropbox.putFileOverwrite(path + "LUNAS_" + team + "_" + customerId + "_" + df.format(date) + ".csv", fileInputStream,
                tempFile.length(), null);
        tempFile.delete();
    }
}

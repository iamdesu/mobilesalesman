package com.bali.nusadua.productmonitor.dropbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.bali.nusadua.productmonitor.MSConstantsIntf;
import com.bali.nusadua.productmonitor.R;
import com.bali.nusadua.productmonitor.model.Customer;
import com.bali.nusadua.productmonitor.model.OrderHeader;
import com.bali.nusadua.productmonitor.model.OrderItem;
import com.bali.nusadua.productmonitor.model.ReturHeader;
import com.bali.nusadua.productmonitor.model.ReturItem;
import com.bali.nusadua.productmonitor.model.SettlementHeader;
import com.bali.nusadua.productmonitor.model.SettlementItem;
import com.bali.nusadua.productmonitor.repo.CustomerRepo;
import com.bali.nusadua.productmonitor.repo.OrderHeaderRepo;
import com.bali.nusadua.productmonitor.repo.ReturHeaderRepo;
import com.bali.nusadua.productmonitor.repo.SettlementHeaderRepo;
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
 * Created by desu on 8/3/15.
 */
public class UploadOutletFileToDropbox extends AsyncTask<Void, Void, Boolean> {
    private DropboxAPI<?> dropbox;
    private String path;
    private Context context;
    private CustomerRepo customerRepo;
    private Customer customer;
    private ProgressDialog progressBar;

    public UploadOutletFileToDropbox(Context context, DropboxAPI<?> dropbox, String path, ProgressDialog progressBar, Customer customer) {
        this.context = context;
        this.dropbox = dropbox;
        this.path = path;
        this.customer = customer;
        this.progressBar = progressBar;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            SharedPreferences prefs = this.context.getSharedPreferences(MSConstantsIntf.MOBILESALES_PREFS_NAME, 0);
            String team = prefs.getString(MSConstantsIntf.TEAM, null);

            progressBar.setProgress(30);
            uploadOrderTable(team, customer.getCustomerId());
            progressBar.setProgress(60);
            uploadReturTable(team, customer.getCustomerId());
            progressBar.setProgress(90);
            uploadSettlementTable(team, customer.getCustomerId());

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
            ((Activity) context).setResult(Activity.RESULT_OK, new Intent());
            ((Activity) context).finish();

        } else {
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

    private void uploadOrderTable(String team, String customerId) throws IOException, DropboxException {
        final File tempDir = context.getCacheDir();
        File tempFile;
        FileWriter fr;

        tempFile = File.createTempFile("file", ".csv", tempDir);
        fr = new FileWriter(tempFile);
        OrderHeaderRepo orderHeaderRepo = new OrderHeaderRepo(context.getApplicationContext());
        List<OrderHeader> orders = orderHeaderRepo.getOrderHeaderItemByCustomer(customerId);

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

        for (OrderHeader orderHeader : orders) {
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

    private void uploadReturTable(String team, String customerId) throws IOException, DropboxException {
        final File tempDir = context.getCacheDir();
        File tempFile;
        FileWriter fr;

        tempFile = File.createTempFile("file", ".csv", tempDir);
        fr = new FileWriter(tempFile);
        ReturHeaderRepo returHeaderRepo = new ReturHeaderRepo(context.getApplicationContext());
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
            for (ReturItem retur : returHeader.getReturItems()) {
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
                fr.append(returHeader.getKodeOutlet());
                fr.append(",");
                fr.append(String.valueOf(retur.getCreateDate()));
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

    private void uploadSettlementTable(String team, String customerId) throws IOException, DropboxException {
        final File tempDir = context.getCacheDir();
        File tempFile;
        FileWriter fr;

        tempFile = File.createTempFile("file", ".csv", tempDir);
        fr = new FileWriter(tempFile);
        SettlementHeaderRepo settlementHeaderRepo = new SettlementHeaderRepo(context.getApplicationContext());
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

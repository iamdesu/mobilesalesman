package com.bali.nusadua.productmonitor.dropbox;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.bali.nusadua.productmonitor.model.Order;
import com.bali.nusadua.productmonitor.repo.OrderRepo;
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
import java.util.Locale;

/**
 * Created by Langit_P on 4/21/2015.
 */
public class UploadFileToDropbox extends AsyncTask<Void, Void, Boolean> {
    private DropboxAPI<?> dropbox;
    private String path;
    private Context context;
    private OrderRepo orderRepo;

    public UploadFileToDropbox(Context context, DropboxAPI<?> dropbox,
                               String path) {
        this.context = context.getApplicationContext();
        this.dropbox = dropbox;
        this.path = path;
        this.orderRepo = new OrderRepo(context);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            uploadOrderTable();
            /*uploadReturTable();
            uploadOrderTable();*/
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
            Toast.makeText(context, "File Uploaded Sucesfully!",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Failed to upload file", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void uploadOrderTable() throws IOException, DropboxException {
        final File tempDir = context.getCacheDir();
        File tempFile;
        FileWriter fr;

        tempFile = File.createTempFile("file", ".csv", tempDir);
        fr = new FileWriter(tempFile);
        List<Order> orders = orderRepo.getAllWithOutlet();

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

        for(Order order : orders) {
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
            fr.append(order.getOutlet().getKode());
            fr.append(",");
            fr.append(String.valueOf(order.getCreateDate()));
            fr.append('\n');
        }

        fr.close();

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        FileInputStream fileInputStream = new FileInputStream(tempFile);
        dropbox.putFile(path + "TeamA-Order-"+df.format(date)+".csv", fileInputStream,
                tempFile.length(), null, null);
        tempFile.delete();
    }
}

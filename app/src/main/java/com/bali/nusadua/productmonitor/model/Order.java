package com.bali.nusadua.productmonitor.model;

import java.util.Date;

/**
 * Created by desu sudarsana on 4/15/2015.
 */
public class Order {
    public static final String TABLE = "OrderPenjualan";
    public static final String ID = "id";
    public static final String GUID = "guid";
    public static final String KODE = "kode";
    public static final String NAMA_BARANG = "nama_barang";
    public static final String HARGA = "harga";
    public static final String QTY = "qty";
    public static final String UNIT = "unit";
    public static final String CREATE_DATE = "create_date";

    private int id;
    private String guid;
    private String kode;
    private String namaBarang;
    private int harga;
    private int qty;
    private String unit;
    private Date createDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}

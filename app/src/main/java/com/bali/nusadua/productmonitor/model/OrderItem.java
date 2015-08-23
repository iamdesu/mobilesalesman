package com.bali.nusadua.productmonitor.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by desu sudarsana on 4/15/2015.
 */
public class OrderItem implements Serializable {
    public static final String TABLE = "OrderItem";
    public static final String ID = "id";
    public static final String GUID = "guid";
    public static final String ORDER_HEADER_GUID = "orderHeaderGuid";
    public static final String KODE = "kode";
    public static final String NAMA_BARANG = "nama_barang";
    public static final String HARGA = "harga";
    public static final String QTY = "qty";
    public static final String UNIT = "unit";
    public static final String CREATE_DATE = "create_date";

    public static final String LOOKUP_DUS = "dus";
    public static final String LOOKUP_PCS = "pcs";
    public static final int LOOKUP_DUS_index = 0;
    public static final int LOOKUP_PCS_index = 1;

    private int id;
    private String guid;
    private String orderHeaderGuid;
    private String kode;
    private String namaBarang;
    private Double harga;
    private int qty;
    private String unit;
    private Date createDate;

    /* Object aggregate */
    private Outlet outlet;

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

    public String getOrderHeaderGuid() {
        return orderHeaderGuid;
    }

    public void setOrderHeaderGuid(String orderHeaderGuid) {
        this.orderHeaderGuid = orderHeaderGuid;
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

    public Double getHarga() {
        return harga;
    }

    public void setHarga(Double harga) {
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

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    /**
     * This method to generate Database
     *
     * @return String
     */
    public static String CREATE_TABLE() {
        String CREATE_TABLE = "CREATE TABLE " + TABLE + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GUID + " TEXT, "
                + ORDER_HEADER_GUID + " TEXT, "
                + KODE + " TEXT, "
                + NAMA_BARANG + " TEXT, "
                + HARGA + " REAL, "
                + QTY + " INTEGER, "
                + UNIT + " TEXT, "
                + CREATE_DATE + " TEXT ) ";

        return CREATE_TABLE;
    }
}

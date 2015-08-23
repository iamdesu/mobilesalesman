package com.bali.nusadua.productmonitor.model;

import java.util.Date;

/**
 * Created by desu on 8/23/15.
 */
public class OrderHeader {
    public static final String TABLE = "OrderHeader";
    public static final String ID = "id";
    public static final String GUID = "guid";
    public static final String KODE_OUTLET = "kode_outlet";
    public static final String CREATE_DATE = "create_date";
    public static final String UPDATE_DATE = "create_date";

    private int id;
    private String guid;
    private String kodeOutlet;
    private Date createDate;
    private Date updateDate;

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

    public String getKodeOutlet() {
        return kodeOutlet;
    }

    public void setKodeOutlet(String kodeOutlet) {
        this.kodeOutlet = kodeOutlet;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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
                + KODE_OUTLET + " TEXT, "
                + UPDATE_DATE + " TEXT, "
                + CREATE_DATE + " TEXT ) ";

        return CREATE_TABLE;
    }
}

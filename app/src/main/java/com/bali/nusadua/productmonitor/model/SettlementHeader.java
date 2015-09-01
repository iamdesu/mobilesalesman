package com.bali.nusadua.productmonitor.model;

import java.util.Date;
import java.util.List;

/**
 * Created by desu on 8/30/15.
 */
public class SettlementHeader {
    public static final String TABLE = "SettlementHeader";
    public static final String ID = "id";
    public static final String KODE_OUTLET = "kode_outlet"; //CustomerID
    public static final String CREATE_DATE = "create_date";
    public static final String UPDATE_DATE = "update_date";

    private int id;
    private String kodeOutlet;
    private Date createDate;
    private Date updateDate;
    private List<SettlementItem> settlementItems;

    /**
     * This method to generate Database
     *
     * @return String
     */
    public static String CREATE_TABLE() {
        String CREATE_TABLE = "CREATE TABLE " + TABLE + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KODE_OUTLET + " TEXT, "
                + UPDATE_DATE + " TEXT, "
                + CREATE_DATE + " TEXT ) ";

        return CREATE_TABLE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<SettlementItem> getSettlementItems() {
        return settlementItems;
    }

    public void setSettlementItems(List<SettlementItem> settlementItems) {
        this.settlementItems = settlementItems;
    }
}

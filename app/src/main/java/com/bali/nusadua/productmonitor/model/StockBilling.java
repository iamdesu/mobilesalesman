package com.bali.nusadua.productmonitor.model;

public class StockBilling {
    public static final String TABLE = "StockBilling";
    public static final String ID = "id";
    public static final String STOCK_ID = "Stock_ID";
    public static final String SCODE = "SCode";
    public static final String DESCRIPTION = "Description";

    private int id;
    private String stockId;
    private String scode;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /* This method to generate Database */
    public static String CREATE_TABLE() {
        String CREATE_TABLE = "CREATE TABLE " + TABLE + "( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STOCK_ID + " TEXT, "
                + SCODE + " TEXT, "
                + DESCRIPTION + " TEXT )";

        return CREATE_TABLE;
    }
}

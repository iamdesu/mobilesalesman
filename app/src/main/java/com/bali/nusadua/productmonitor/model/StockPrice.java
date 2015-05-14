package com.bali.nusadua.productmonitor.model;

public class StockPrice {
    public static final String TABLE = "StockPrice";
    public static final String ID = "id";
    public static final String STOCK_ID = "Stock_ID";
    public static final String PRICE_LEVEL = "price_level";
    public static final String PRICE = "price";

    private int id;
    private String stockId;
    private Integer priceLevel;
    private Double price;

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

    public Integer getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(Integer priceLevel) {
        this.priceLevel = priceLevel;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * This method to generate Database
     *
     * @return String
     */
    public static String CREATE_TABLE() {
        String CREATE_TABLE = "CREATE TABLE " + TABLE + "( "
                + ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STOCK_ID + " TEXT, "
                + PRICE_LEVEL + " INTEGER, "
                + PRICE + " REAL )";

        return CREATE_TABLE;
    }
}

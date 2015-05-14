package com.bali.nusadua.productmonitor.model;

public class Billing {
    public static final String TABLE = "Billing";
    public static final String ID = "id";
    public static final String INVOICE_NO = "invoice_no";
    public static final String CUST_ID = "cust_id";
    public static final String TOTAL_AMOUNT = "total_amount";
    public static final String PAID_AMOUNT = "paid_amount";

    private int id;
    private String invoiceNo;
    private String customerId;
    private Double totalAmount;
    private Double paidAmount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    /**
     * This method to generate Database
     *
     * @return String
     */
    public static final String CREATE_TABLE() {
        String CREATE_TABLE = "CREATE TABLE " + TABLE + "( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + INVOICE_NO + " TEXT, "
                + CUST_ID + " TEXT, "
                + TOTAL_AMOUNT + " REAL, "
                + PAID_AMOUNT + " REAL )";

        return CREATE_TABLE;
    }
}

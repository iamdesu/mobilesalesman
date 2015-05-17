package com.bali.nusadua.productmonitor.model;

import java.util.Date;

public class Settlement {
    public static final String TABLE = "Settlement";
    public static final String ID = "id";
    public static final String GUID = "guid";
    public static final String INVOICE_NUMBER = "invoice_number";
    public static final String INVOICE_DATE = "invoice_date";
    public static final String CREDIT = "credit";
    public static final String PAYMENT_METHOD = "payment_method";
    public static final String NOMINAL_PAYMENT = "nominal_payment";
    public static final String KODE_OUTLET = "kode_outlet";
    public static final String CREATE_DATE = "create_date";

    private int id;
    private String guid;
    private String invoiceNumber;
    private Date invoiceDate;
    private Long credit;
    private String paymentMethod;
    private Long nominalPayment;
    private String kodeOutlet;
    private Date createdDate;

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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Long getCredit() {
        return credit;
    }

    public void setCredit(Long credit) {
        this.credit = credit;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getNominalPayment() {
        return nominalPayment;
    }

    public void setNominalPayment(Long nominalPayment) {
        this.nominalPayment = nominalPayment;
    }

    public String getKodeOutlet() {
        return kodeOutlet;
    }

    public void setKodeOutlet(String kodeOutlet) {
        this.kodeOutlet = kodeOutlet;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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
                + INVOICE_NUMBER + " TEXT, "
                + INVOICE_DATE + " TEXT, "
                + CREDIT + " INTEGER, "
                + PAYMENT_METHOD + " TEXT, "
                + NOMINAL_PAYMENT + " INTEGER, "
                + KODE_OUTLET + " TEXT, "
                + CREATE_DATE + " TEXT ) ";

        return CREATE_TABLE;
    }
}

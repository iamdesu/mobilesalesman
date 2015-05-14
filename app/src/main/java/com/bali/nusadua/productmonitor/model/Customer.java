package com.bali.nusadua.productmonitor.model;

public class Customer {
    public static final String TABLE = "Customer";
    public static final String ID = "id";
    public static final String CUST_ID = "cust_id";
    public static final String COMPANY_NAME = "company_name";
    public static final String PERSON_NAME = "person_name";
    public static final String ADDRESS = "address";
    public static final String REGION = "region";
    public static final String CITY = "city";
    public static final String VISIT = "visit";
    public static final String PRICE_LEVEL = "price_level";

    private int id;
    private String customerId;
    private String companyName;
    private String personName;
    private String address;
    private String region;
    private String city;
    private String visit;
    private Integer priceLevel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVisit() {
        return visit;
    }

    public void setVisit(String visit) {
        this.visit = visit;
    }

    public Integer getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(Integer priceLevel) {
        this.priceLevel = priceLevel;
    }

    /**
     * This method to generate Database
     *
     * @return String
     */
    public static final String CREATE_TABLE() {
        String CREATE_TABLE = "CREATE TABLE " + TABLE + "( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CUST_ID + " TEXT, "
                + COMPANY_NAME + " TEXT, "
                + PERSON_NAME + " TEXT, "
                + ADDRESS + " TEXT, "
                + REGION + " TEXT, "
                + CITY + " TEXT, "
                + VISIT + " TEXT, "
                + PRICE_LEVEL + " INTEGER )";

        return CREATE_TABLE;
    }
}

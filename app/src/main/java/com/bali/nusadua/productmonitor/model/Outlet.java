package com.bali.nusadua.productmonitor.model;

/**
 * Created by desu sudarsana on 4/21/2015.
 */
public class Outlet {
    public static final String TABLE = "Outlet";
    public static final String ID = "id";
    public static final String GUID = "guid";
    public static final String KODE = "kode";
    public static final String NAME = "nama";

    private int id;
    private String guid;
    private String kode;
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method to generate Database
     *
     * @return String
     */
    public static String CREATE_TABLE() {
        String CREATE_TABLE = "CREATE TABLE " + Outlet.TABLE + " ( "
                + Outlet.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Outlet.GUID + " TEXT, "
                + Outlet.KODE + " TEXT, "
                + Outlet.NAME + " TEXT ) ";

        return CREATE_TABLE;
    }

}

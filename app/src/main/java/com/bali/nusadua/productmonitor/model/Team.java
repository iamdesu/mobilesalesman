package com.bali.nusadua.productmonitor.model;

/**
 * Created by desu sudarsana on 4/11/2015.
 */
public class Team {
    //Label table name
    public static final String TABLE = "Team";

    //Labels table column names
    public static final String ID = "id";
    public static final String GUID = "guid";
    public static final String NAME = "name";

    private int id;
    private String guid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.bali.nusadua.productmonitor.model;

public class StaffBilling {
    public static final String TABLE = "StaffBilling";
    public static final String ID = "id";
    public static final String STAFF = "staff";
    public static final String STAFFNAME = "staff_name";
    public static final String PASSWD = "password";
    public static final String LEVEL = "level";
    public static final String USER_ID = "user_id";
    public static final String TEAM = "team";
    public static final String EXPIRE = "expire";

    private int id;
    private String staff;
    private String staffName;
    private String password;
    private Integer level;
    private String userID;
    private String team;
    private Integer expire;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    /**
     * This method to generate Database
     *
     * @return String
     */
    public static String CREATE_TABLE() {
        String CREATE_TABLE = "CREATE TABLE " + TABLE + "( "
                + ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STAFF + " TEXT, "
                + STAFFNAME + " TEXT, "
                + PASSWD + " TEXT, "
                + LEVEL + " INTEGER, "
                + USER_ID + " TEXT, "
                + TEAM + " TEXT, "
                + EXPIRE + " INTEGER )";

        return CREATE_TABLE;
    }

}

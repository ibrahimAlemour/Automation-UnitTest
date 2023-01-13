package com.firstaid.firstaidapp.model;

public class AlarmDataSend {
    private String name;
    private String des;
    private String phone;
    private String location;
    private String level;
    private boolean status;

    public AlarmDataSend(String name, String des, String phone, String location, String level, boolean status) {
        this.name = name;
        this.des = des;
        this.phone = phone;
        this.location = location;
        this.level = level;
        this.status = status;
    }

    public AlarmDataSend() {
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDes() {
        return des;
    }

    public boolean isStatus() {
        return status;
    }

    public String getLevel() {
        return level;
    }
}

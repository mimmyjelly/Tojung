package com.example.tojung;

// 데이터가 담길 변수

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String user_name;
    private String user_phone;
    private String wakeup;
    private String sleep;
    User(String s, String name, String phone, String wakeup, String sleep){

    }
    public User( String user_name, String user_phone, String wakeup, String sleep){
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.wakeup = wakeup;
        this.sleep = sleep;
        }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getWakeup() {
        return wakeup;
    }

    public void setWakeup(String wakeup) {
        this.wakeup = wakeup;
    }

    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }
}

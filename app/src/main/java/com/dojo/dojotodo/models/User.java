package com.dojo.dojotodo.models;
import java.util.HashMap;
import java.util.List;

public class User {
    private String id;
    private String userName;
    private HashMap<String,Day > days;
    private int unfinishedTasks;
    private int superDays;

    public User(String id, String userName, HashMap<String, Day> days, int unfinishedTasks, int superDays) {
        this.id = id;
        this.userName = userName;
        this.days = days;
        this.unfinishedTasks = unfinishedTasks;
        this.superDays = superDays;
    }

    public HashMap<String, Day> getDays() {
        return days;
    }

    public void setDays(HashMap<String, Day> days) {
        this.days = days;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }




    public int getUnfinishedTasks() {
        return unfinishedTasks;
    }

    public void setUnfinishedTasks(int unfinishedTasks) {
        this.unfinishedTasks = unfinishedTasks;
    }

    public int getSuperDays() {
        return superDays;
    }

    public void setSuperDays(int superDays) {
        this.superDays = superDays;
    }
}

package com.dojo.dojotodo.models;

import java.util.HashMap;
import java.util.List;

public class Day {
    public static int maxTasks = 5;
    private int numberOfTasks;
    private int remainingTasks;
    private String customDayId;
    private String date;
    private int day;
    private int month;
    private int year;
    private boolean allTasksCompleted;
    private HashMap<String,UserTask> userTasks;

    public Day() {
    }

    public Day(int numberOfTasks, int remainingTasks, String customDayId, String date, int day, int month, int year, boolean allTasksCompleted, HashMap<String,UserTask> userTasks) {
        this.numberOfTasks = numberOfTasks;
        this.remainingTasks = remainingTasks;
        this.customDayId = customDayId;
        this.date = date;
        this.day = day;
        this.month = month;
        this.year = year;
        this.allTasksCompleted = allTasksCompleted;
        this.userTasks = userTasks;
    }

    public int getNumberOfTasks() {
        return numberOfTasks;
    }

    public void setNumberOfTasks(int numberOfTasks) {
        this.numberOfTasks = numberOfTasks;
    }

    public static int getMaxTasks() {
        return maxTasks;
    }

    public static void setMaxTasks(int maxTasks) {
        Day.maxTasks = maxTasks;
    }

    public int getRemainingTasks() {
        return remainingTasks;
    }

    public void setRemainingTasks(int remainingTasks) {
        this.remainingTasks = remainingTasks;
    }

    public String getCustomDayId() {
        return customDayId;
    }

    public void setCustomDayId(String customDayId) {
        this.customDayId = customDayId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isAllTasksCompleted() {
        return allTasksCompleted;
    }

    public void setAllTasksCompleted(boolean allTasksCompleted) {
        this.allTasksCompleted = allTasksCompleted;
    }

    public HashMap<String,UserTask> getUserTasks() {
        return userTasks;
    }

    public void setUserTasks(HashMap<String,UserTask> userTasks) {
        this.userTasks = userTasks;
    }
}

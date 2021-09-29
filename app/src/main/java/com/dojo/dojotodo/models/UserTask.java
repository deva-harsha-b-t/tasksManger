package com.dojo.dojotodo.models;

public class UserTask {
    public static int maxTasks = 5;
    private String taskId;
    private String date;
    private String taskName;
    private String taskDescription;
    private boolean isCompleted;

    public UserTask() {
    }

    public UserTask(String taskId, String date, String taskName, String taskDescription, boolean isCompleted) {
        this.taskId = taskId;
        this.date = date;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.isCompleted = isCompleted;
    }

    public static int getMaxTasks() {
        return maxTasks;
    }

    public static void setMaxTasks(int maxTasks) {
        UserTask.maxTasks = maxTasks;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}

package com.jnu.student;

import java.io.Serializable;

public class MyTask implements Serializable {
    private String taskTime;
    private String taskTitle;
    private int taskPoint;
    private int taskNum;
    private int taskNumFinish;
    private String taskType;
    private String taskTag;
    private String taskState;



    public MyTask() {
    }

    public MyTask(String taskTime, String taskTitle, int taskPoint, int taskNum, int taskNumFinish, String taskType, String taskTag, String taskState) {
        this.taskTime = taskTime;
        this.taskTitle = taskTitle;
        this.taskPoint = taskPoint;
        this.taskNum = taskNum;
        this.taskNumFinish = taskNumFinish;
        this.taskType = taskType;
        this.taskTag = taskTag;
        this.taskState = taskState;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public int getTaskPoint() {
        return taskPoint;
    }

    public void setTaskPoint(int taskPoint) {
        this.taskPoint = taskPoint;
    }

    public int getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }

    public int getTaskNumFinish() {
        return taskNumFinish;
    }

    public void setTaskNumFinish(int taskNumFinish) {
        this.taskNumFinish = taskNumFinish;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskTag() {
        return taskTag;
    }

    public void setTaskTag(String taskTag) {
        this.taskTag = taskTag;
    }
    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    @Override
    public String toString() {
        return "MyTask{" +
                "taskTime='" + taskTime + '\'' +
                ", taskTitle='" + taskTitle + '\'' +
                ", taskPoint=" + taskPoint +
                ", taskNum=" + taskNum +
                ", taskNumFinish=" + taskNumFinish +
                ", taskType='" + taskType + '\'' +
                ", taskTag='" + taskTag + '\'' +
                ", taskState='" + taskState + '\'' +
                '}';
    }
}
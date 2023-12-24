package com.jnu.student;

import android.content.Context;

import java.util.ArrayList;

public interface TaskRepository {
    ArrayList<MyTask> loadTaskItems(Context context,String fileName);

    void saveTaskItems(Context context,String fileName,ArrayList<MyTask> taskData);
}
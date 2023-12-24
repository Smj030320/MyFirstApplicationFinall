package com.jnu.student;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Task_Repository_Lmpl implements TaskRepository {

    @Override
    public ArrayList<MyTask> loadTaskItems(Context context, String fileName) {
        ArrayList<MyTask> taskData = new ArrayList<>();
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            taskData = (ArrayList<MyTask>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskData;
    }

    @Override
    public void saveTaskItems(Context context, String fileName, ArrayList<MyTask> taskData) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(taskData);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
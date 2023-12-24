package com.jnu.student;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Reward_Repository_Lmpl implements Reward_Repository {

    @Override
    public ArrayList<MyReward> loadRewardItems(Context context, String fileName) {
        ArrayList<MyReward> rewardData = new ArrayList<>();
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            rewardData = (ArrayList<MyReward>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rewardData;
    }

    @Override
    public void saveRewardItems(Context context, String fileName, ArrayList<MyReward> rewardData) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(rewardData);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.jnu.student;

import android.content.Context;

import java.util.ArrayList;

public interface Reward_Repository {
    ArrayList<MyReward> loadRewardItems(Context context, String fileName);
    void saveRewardItems(Context context, String fileName, ArrayList<MyReward> rewardData);
}
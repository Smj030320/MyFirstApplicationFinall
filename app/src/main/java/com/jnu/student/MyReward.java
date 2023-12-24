package com.jnu.student;
import androidx.annotation.NonNull;
import java.io.Serializable;
public class MyReward implements Serializable {
    private final String rewardTime;
    private final String rewardTitle;
    private final int rewardPoint;
    private int rewardFinish;
    private final String rewardType;
    private final String rewardTag;
    public MyReward(String rewardTime, String rewardTitle, int rewardPoint, int rewardFinish, String rewardType, String rewardTag) {
        this.rewardTime = rewardTime;
        this.rewardTitle = rewardTitle;
        this.rewardPoint = rewardPoint;
        this.rewardFinish = rewardFinish;
        this.rewardType = rewardType;
        this.rewardTag = rewardTag;
    }
    public String getRewardTime() {
        return rewardTime;
    }
    public String getRewardTitle() {
        return rewardTitle;
    }
    public int getRewardPoint() {
        return rewardPoint;
    }
    public int getRewardFinish() {
        return rewardFinish;
    }
    public void setRewardFinish(int rewardFinish) {
        this.rewardFinish = rewardFinish;
    }
    public String getRewardType() {
        return rewardType;
    }

    @NonNull
    @Override
    public String toString() {
        return "MyReward{" +
                "rewardTime='" + rewardTime + '\'' +
                ", rewardTitle='" + rewardTitle + '\'' +
                ", rewardPoint=" + rewardPoint +
                ", rewardFinish=" + rewardFinish +
                ", rewardType='" + rewardType + '\'' +
                ", rewardTag='" + rewardTag + '\'' +
                '}';
    }
}
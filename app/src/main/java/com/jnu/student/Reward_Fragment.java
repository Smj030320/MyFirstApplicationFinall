package com.jnu.student;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;


public class Reward_Fragment extends Fragment {
    private final String FILE_TASK_NAME = "taskData.ser";
    private final String FILE_REWARD_NAME = "rewardData.ser";
    private TaskRepository taskRepository;
    private Reward_Repository rewardRepository;
    private ArrayList<MyTask> taskList;
    private ArrayList<MyReward> rewardList;
    private int pointSumText;
    private ActivityResultLauncher<Intent> addTaskLauncher;
    public Reward_Recycler_Adapter rewardRecyclerAdapter;
    private TextView sumPointView;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals("MY_CUSTOM_ACTION")) {
                taskList = taskRepository.loadTaskItems(requireActivity(), FILE_TASK_NAME);
                rewardList = rewardRepository.loadRewardItems(requireActivity(),FILE_REWARD_NAME);
                pointSumText = getPointSum(taskList,rewardList);
                if(pointSumText < 0){
                    sumPointView.setTextColor(Color.RED);
                }
                else{
                    sumPointView.setTextColor(Color.BLACK);
                }
                sumPointView.setText("   "+pointSumText);
            }
        }
    };
    public Reward_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注册广播接收器
        LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(receiver, new IntentFilter("MY_CUSTOM_ACTION"));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reward, container, false);
        // 数据读取
        taskRepository = new Task_Repository_Lmpl();
        rewardRepository = new Reward_Repository_Lmpl();
        taskList = taskRepository.loadTaskItems(requireActivity(), FILE_TASK_NAME);
        rewardList = rewardRepository.loadRewardItems(requireActivity(),FILE_REWARD_NAME);
        // 设置左下角点数
        pointSumText = getPointSum(taskList,rewardList);
        sumPointView = rootView.findViewById(R.id.Point_Sum_Text);
        if(pointSumText < 0){
            sumPointView.setTextColor(Color.RED);
        }
        else{
            sumPointView.setTextColor(Color.BLACK);
        }
        sumPointView.setText("   "+pointSumText);
        // 设置RecyclerView和Adapter
        RecyclerView rewardRecyclerView = rootView.findViewById(R.id.Reward_Recycler_View);
        rewardRecyclerAdapter = new Reward_Recycler_Adapter(requireActivity(),rewardList);
        rewardRecyclerView.setAdapter(rewardRecyclerAdapter);
        rewardRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        // 注册上下文菜单
        registerForContextMenu(rewardRecyclerView);
        // 注册添加任务的Launcher
        addTaskLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // 当数据准备好时
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        assert intent != null;
                        String title = intent.getStringExtra("title");
                        int points = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("points")));
                        String type = intent.getStringExtra("type");
                        String tag = intent.getStringExtra("tag");
                        // 创建 Date 对象表示当前时间
                        Date currentDate = new Date();
                        // 创建 SimpleDateFormat 对象，指定日期格式和时区
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 设置时区为北京时间
                        // 格式化输出
                        String beijingTime = sdf.format(currentDate);
                        MyReward myReward = new MyReward(beijingTime, title,points,0,type,tag);
                        // 存储数据
                        rewardList.add(myReward);
                        rewardRecyclerAdapter.notifyItemInserted(rewardList.size());
                        rewardRepository.saveRewardItems(requireActivity().getApplicationContext(), FILE_REWARD_NAME,rewardList);
                    }
                    // 当数据还没准备好时
                    else {
                        requireActivity();
                    }
                });
        // 添加按钮响应函数
        ImageView myImageView = rootView.findViewById(R.id.Add_Button);
        myImageView.setOnClickListener(v -> {
            // 在这里编写 ImageView 被点击后的逻辑，添加数据
            Intent intent = new Intent(requireActivity(), Reward_Details_Activity.class);
            addTaskLauncher.launch(intent);
        });
        return rootView;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // 删除判断位置
        if(item.getGroupId() != 3){
            return false;
        }
        switch (item.getItemId()) {
            case 0:
                break;
            case 1:
                // 删除数据
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Delete Data");
                builder.setMessage("你确定删除这个奖励吗？");
                // 如果按下确定
                builder.setPositiveButton("YSE", (dialog, which) -> {
                    // 刷新界面
                    rewardList.remove(item.getOrder());
                    rewardRecyclerAdapter.notifyItemRemoved(item.getOrder());
                    // 保存数据
                    rewardRepository.saveRewardItems(requireActivity().getApplicationContext(),FILE_REWARD_NAME,rewardList);
                });
                // 如果按下否定，什么都不做
                builder.setNegativeButton("NO", (dialog, which) -> {
                });
                builder.create().show();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    private int getPointSum(ArrayList<MyTask> taskList,ArrayList<MyReward> rewardList){
        int sum = 0;
        for(MyTask task:taskList){
            sum += task.getTaskPoint() * task.getTaskNumFinish();
        }
        for(MyReward reward:rewardList){
            sum -= reward.getRewardPoint() * reward.getRewardFinish();
        }
        return sum;
    }
    @Override
    public void onDestroy() {
        // 注销广播接收器
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver);
        super.onDestroy();
    }

}

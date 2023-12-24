package com.jnu.student;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class Task_Fragment extends Fragment{
    private final static  String[] taskType = {"每日任务", "每周任务", "普通任务","副本"};
    private final String FILE_TASK_NAME = "taskData.ser";
    private final String FILE_REWARD_NAME = "rewardData.ser";
    private TaskRepository taskRepository;
    private Reward_Repository rewardRepository;
    private ArrayList<MyTask> taskList;
    private ArrayList<MyReward> rewardList;
    private int pointSumText;
    private ActivityResultLauncher<Intent> addTaskLauncher;

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


    public Task_Fragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_task, container, false);
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
        // Fragment + ViewPager2 + TableLayout
        ViewPager2 viewPager = rootView.findViewById(R.id.View_Pager);
        TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);
        // 创建 FragmentPagerAdapter
        PagerAdapter pagerAdapter = new PagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        // 将 TabLayout 和 ViewPager 关联起来
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("Tab " + (position + 1))
        ).attach();
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(taskType[position])).attach();

        // 注册添加任务的Launcher
        addTaskLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // 当数据准备好时
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        assert intent != null;
                        String title = intent.getStringExtra("title");
                        int points = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("points")));
                        int numbers = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("numbers")));
                        String type = intent.getStringExtra("type");
                        String tag = intent.getStringExtra("tag");
                        // 创建 Date 对象表示当前时间
                        Date currentDate = new Date();
                        // 创建 SimpleDateFormat 对象，指定日期格式和时区
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 设置时区为北京时间
                        // 格式化输出
                        String beijingTime = sdf.format(currentDate);
                        MyTask myTask = new MyTask(beijingTime, title,points,numbers,0,type,tag, "正常");
                        // 存储数据
                        taskList.add(myTask);
                        List<Fragment> fragments = getChildFragmentManager().getFragments();
                        for(Fragment fragment:fragments){
                            if(fragment instanceof Phase_Task_Fragment){
                                Phase_Task_Fragment phaseTaskFragment = (Phase_Task_Fragment)fragment;
                                if(phaseTaskFragment.taskType.equals(myTask.getTaskType())) {
                                    phaseTaskFragment.taskListWithType.add(myTask);
                                    phaseTaskFragment.taskRecyclerAdapter.notifyItemInserted(phaseTaskFragment.taskListWithType.size());
                                    break;
                                }
                            }
                        }
                        taskRepository.saveTaskItems(requireActivity().getApplicationContext(), FILE_TASK_NAME,taskList);
                    }
                    // 当数据还没准备好时
                    else {
                        requireActivity();
                    }
                });

        // 添加按钮响应函数
        ImageView myImageView = rootView.findViewById(R.id.AddButton);
        myImageView.setOnClickListener(v -> {
            // 在这里编写 ImageView 被点击后的逻辑，添加数据
            Intent intent = new Intent(requireActivity(), Task_Details_Activity.class);
            addTaskLauncher.launch(intent);
        });


        return rootView;
    }

    // 创建适配器
    private static class PagerAdapter extends FragmentStateAdapter {

        private static final int NUM_TABS = 4;

        public PagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // 根据位置返回不同的子 Fragment
            switch (position) {
                case 0:
                    return new Phase_Task_Fragment(taskType[0],0);
                case 1:
                    return new Phase_Task_Fragment(taskType[1],1);
                case 2:
                    return new Phase_Task_Fragment(taskType[2],2);
                case 3:
                    return new Phase_Task_Fragment(taskType[3],3);
                default:
                    throw new IllegalArgumentException("Invalid position");
            }
        }

        @Override
        public int getItemCount() {
            // 返回子 Fragment 的数量
            return NUM_TABS;
        }
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
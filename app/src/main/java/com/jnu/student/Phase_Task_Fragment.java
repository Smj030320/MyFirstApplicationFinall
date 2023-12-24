package com.jnu.student;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Objects;

public class Phase_Task_Fragment extends Fragment {

    // 菜单Id
    public int menuId;
    // 分辨每日、每周和普通任务，加载不同的数据
    public String taskType;
    private ActivityResultLauncher<Intent> updateTaskLauncher;
    public Task_Recycler_Adapter taskRecyclerAdapter;
    private TaskRepository taskRepository;
    public ArrayList<MyTask> taskListWithType;
    private ArrayList<MyTask> taskListAll;
    private final String FILE_NAME = "taskData.ser";

    public Phase_Task_Fragment() {
        // Required empty public constructor
    }
    public Phase_Task_Fragment(String taskType, int menuId){
        this.menuId = menuId;
        this.taskType = taskType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_phase_task, container, false);
        // 获取数据
        taskRepository = new Task_Repository_Lmpl();
        taskListAll = taskRepository.loadTaskItems(requireActivity().getApplicationContext(),FILE_NAME);
        taskListWithType = filterWithType(taskListAll,taskType);
        // 设置RecyclerView和设置Adapter
        RecyclerView taskRecyclerView = rootView.findViewById(R.id.Task_Recycler_View);
        taskRecyclerAdapter = new Task_Recycler_Adapter(requireActivity(), taskListWithType,this.menuId);
        taskRecyclerView.setAdapter(taskRecyclerAdapter);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        // 注册上下文菜单
        registerForContextMenu(taskRecyclerView);
        // 注册更新的Launcher
        // 更新任务
        updateTaskLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // 当数据准备好时
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        assert intent != null;
                        int position = intent.getIntExtra("position",0);
                        String title = intent.getStringExtra("title");
                        int points = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("points")));
                        int numbers = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("numbers")));
                        String tag = intent.getStringExtra("tag");
                        MyTask task = taskListWithType.get(position);
                        task.setTaskTitle(title);
                        task.setTaskPoint(points);
                        task.setTaskNum(numbers);

                        task.setTaskTag(tag);
                        taskRecyclerAdapter.notifyItemChanged(position);
                        taskRepository.saveTaskItems(requireActivity().getApplicationContext(),FILE_NAME, taskListWithType);
                    }
                    // 当数据还没准备好时
                    else {
                        requireActivity();
                    }
                });
        // 设置加成就
        return rootView;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // 删除判断位置
        if(item.getGroupId() != menuId){
            return false;
        }
        switch (item.getItemId()) {
            case 0:
                Intent intentUpdate = new Intent(requireActivity(), Task_Details_Activity.class);
                MyTask selectTask = taskListWithType.get(item.getOrder());
                intentUpdate.putExtra("title",selectTask.getTaskTitle());
                intentUpdate.putExtra("points",String.valueOf(selectTask.getTaskPoint()));
                intentUpdate.putExtra("numbers",String.valueOf(selectTask.getTaskNum()));
                intentUpdate.putExtra("tag",selectTask.getTaskTag());
                intentUpdate.putExtra("position",item.getOrder());
                updateTaskLauncher.launch(intentUpdate);
                break;
            case 1:
                // 删除数据
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Delete Data");
                builder.setMessage("你确定删除这个任务吗？");
                // 如果按下确定
                builder.setPositiveButton("YSE", (dialog, which) -> {
                    // 更新数据
                    taskListAll = taskRepository.loadTaskItems(requireActivity().getApplicationContext(),FILE_NAME);
                    MyTask myTask = taskListWithType.get(item.getOrder());
                    for(MyTask task:taskListAll){
                        if(task.getTaskTime().equals(myTask.getTaskTime())){
                            task.setTaskState("删除完毕");
                            break;
                        }
                    }
                    taskRepository.saveTaskItems(requireActivity().getApplicationContext(),FILE_NAME,taskListAll);
                    // 刷新界面
                    taskListWithType.remove(item.getOrder());
                    taskRecyclerAdapter.notifyItemRemoved(item.getOrder());
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

    private ArrayList<MyTask> filterWithType(ArrayList<MyTask> taskListAll,String taskType){
        ArrayList<MyTask> taskFilterWithType = new ArrayList<>();
        for(MyTask task:taskListAll){
            if(task.getTaskType().equals(taskType) && task.getTaskState().equals("正常")){
                taskFilterWithType.add(task);
            }
        }
        return taskFilterWithType;
    }

}
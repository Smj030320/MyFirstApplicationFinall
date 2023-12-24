package com.jnu.student;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Task_Recycler_Adapter extends RecyclerView.Adapter<Task_Recycler_Adapter.MyViewHolder>{
    private final String FILE_TASK_NAME = "taskData.ser";
    private TaskRepository taskRepository;
    private final Context context;
    private final ArrayList<MyTask> taskList;
    public int menuId;

    public Task_Recycler_Adapter(Context context, ArrayList<MyTask> taskList, int menuId) {
        this.context = context;
        this.taskList = taskList;
        this.menuId = menuId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.task_item_layout,parent,false);
        return new MyViewHolder(view,menuId);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyTask myTask = taskList.get(position);
        holder.taskTitle.setText(myTask.getTaskTitle());
        holder.taskNumFinish.setText(myTask.getTaskNumFinish()+"/"+ myTask.getTaskNum());
        holder.taskAddPoint.setText("+"+myTask.getTaskPoint());
        holder.taskAddPoint.setOnClickListener(v -> {
            // 改变数据
            if(myTask.getTaskNumFinish() < myTask.getTaskNum()){
                taskRepository = new Task_Repository_Lmpl();
                ArrayList<MyTask> taskAllList = taskRepository.loadTaskItems(context.getApplicationContext(), FILE_TASK_NAME);
                for(MyTask task:taskAllList){
                    if(task.getTaskTime().equals(myTask.getTaskTime())){
                        task.setTaskNumFinish(task.getTaskNumFinish()+1);
                        if(task.getTaskNumFinish() == task.getTaskNumFinish()){
                            task.setTaskState("已完成");
                        }
                        taskRepository.saveTaskItems(context, FILE_TASK_NAME,taskAllList);
                    }
                }
                myTask.setTaskNumFinish(myTask.getTaskNumFinish()+1);
                holder.taskNumFinish.setText(myTask.getTaskNumFinish()+"/"+ myTask.getTaskNum());
                // 发送广播
                Intent intent = new Intent("MY_CUSTOM_ACTION");
                intent.putExtra("data", "invalidate");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        public int menuId;
        ImageView bookImage;
        TextView taskTitle;
        TextView taskNumFinish;
        TextView taskSubPoint;
        TextView taskAddPoint;

        public MyViewHolder(@NonNull View itemView,int menuId) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.Task_Item_Img);
            taskTitle = itemView.findViewById(R.id.Task_Item_Title);
            taskNumFinish = itemView.findViewById(R.id.Task_Num_Finish);
            taskAddPoint = itemView.findViewById(R.id.Task_Num_Add);
            this.menuId = menuId;
            // 设置监听者为自己
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(menuId,0,this.getAdapterPosition(),"修改");
            menu.add(menuId,1,this.getAdapterPosition(),"删除");
        }
    }
}
package com.dojo.dojotodo.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dojo.dojotodo.R;
import com.dojo.dojotodo.TaskActivity;
import com.dojo.dojotodo.TaskDetailsActivity;
import com.dojo.dojotodo.models.UserTask;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class tasksAdapter extends FirebaseRecyclerAdapter<UserTask,tasksAdapter.taskViewHolder> {
    private Activity context;
    private TaskActivity.taskItemHelperClass taskItemHelperClass;

    public tasksAdapter(@NonNull FirebaseRecyclerOptions<UserTask> options, Activity context, TaskActivity.taskItemHelperClass taskItemHelperClass) {
        super(options);
        this.taskItemHelperClass = taskItemHelperClass;
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull taskViewHolder holder, int position, @NonNull UserTask model) {
        if(model.isCompleted()){
            holder.check.setImageResource(R.drawable.complete_task);
            holder.taskName.setPaintFlags(holder.taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.taskName.setText(model.getTaskName());
        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context , TaskDetailsActivity.class);
            intent.putExtra("taskDate",model.getDate());
            intent.putExtra("isComplete", model.isCompleted());
            intent.putExtra("taskID",model.getTaskId());
            intent.putExtra("taskTitle",model.getTaskName());
            intent.putExtra("taskDes",model.getTaskDescription());
            context.startActivity(intent);
        });
        holder.deleteTaskButton.setOnClickListener(v->{
            taskItemHelperClass.deleteItem(model);
        });
    }

    @NonNull
    @Override
    public taskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item,parent,false);
        return new taskViewHolder(v);
    }

    public class taskViewHolder extends RecyclerView.ViewHolder {
        private ImageView check;
        private TextView taskName;
        private ImageButton deleteTaskButton;
        public taskViewHolder(@NonNull View itemView) {
            super(itemView);
            check = itemView.findViewById(R.id.taskItemCompleteIcon);
            taskName = itemView.findViewById(R.id.taskName);
            deleteTaskButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}

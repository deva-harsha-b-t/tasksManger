package com.dojo.dojotodo.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dojo.dojotodo.R;
import com.dojo.dojotodo.TaskActivity;
import com.dojo.dojotodo.models.Day;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class daysadapter extends FirebaseRecyclerAdapter<Day,daysadapter.daysViewHolder> {
    private Activity context;
    private Date todaysDate;
    private String todaysDateShort;


    public daysadapter(@NonNull FirebaseRecyclerOptions<Day> options, Activity context) {
        super(options);
        this.context = context;
        todaysDate = Calendar.getInstance().getTime();
        todaysDateShort = DateFormat.getDateInstance(DateFormat.MEDIUM).format(todaysDate);
    }

    @Override
    protected void onBindViewHolder(@NonNull daysViewHolder holder, int position, @NonNull Day model) {
        String numTask = "" + (model.getNumberOfTasks() - model.getRemainingTasks()) + "/" + model.getNumberOfTasks() + "( max " + Day.maxTasks + ")";
        holder.noOfTaskCompleted.setText(numTask);
        holder.date.setText(model.getDate());
        if(todaysDateShort.equals(model.getDate())){
            holder.today.setVisibility(View.VISIBLE);
        }
        if(model.getRemainingTasks() <= 0){
            holder.completedIcon.setImageResource(R.drawable.complete_icon);
        }
        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, TaskActivity.class);
            intent.putExtra("dayId",model.getDate());
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public daysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_day,parent,false);
        return new daysViewHolder(view);
    }

    public class daysViewHolder extends RecyclerView.ViewHolder{
        private TextView today;
        private TextView date;
        private TextView noOfTaskCompleted;
        private ImageView completedIcon;
        public daysViewHolder(@NonNull View itemView) {
            super(itemView);
            today = itemView.findViewById(R.id.TodayTextView);
            noOfTaskCompleted = itemView.findViewById(R.id.noOfTaskCompleted);
            completedIcon = itemView.findViewById(R.id.completedIcon);
            date = itemView.findViewById(R.id.Date);
        }


    }
}

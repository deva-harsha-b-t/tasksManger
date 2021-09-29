package com.dojo.dojotodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dojo.dojotodo.adapters.tasksAdapter;
import com.dojo.dojotodo.models.Day;
import com.dojo.dojotodo.models.UserTask;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;

import java.util.Objects;
import java.util.UUID;

public class TaskActivity extends AppCompatActivity {
    private TextView time;
    private TextView dateInShort;
    private RecyclerView recyclerView;
    private tasksAdapter tasksadapter;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference mRef;
    private Button createTaskButton;
    private BottomSheetDialog bottomSheetDialog;
    private Context context;
    private String dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        context = this.getApplicationContext();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        time = findViewById(R.id.time);
        dateInShort = findViewById(R.id.shortDate);
        recyclerView = findViewById(R.id.taskRecyclerView);
        createTaskButton = findViewById(R.id.createTask);
        createTaskButton.setOnClickListener(v ->{
            createTask();
        });
        recyclerView.setLayoutManager(new linearLayoutMangerWrapper(this));
        dateString = getIntent().getStringExtra("dayId");

        Query query = mRef.child("users").child(firebaseUser.getUid()).child("days")
                .child(dateString).child("userTasks").orderByChild("taskId").limitToFirst(5);

        FirebaseRecyclerOptions<UserTask> options =
                new FirebaseRecyclerOptions.Builder<UserTask>()
                        .setQuery(query, UserTask.class)
                        .build();
        tasksadapter = new tasksAdapter(options, this, new taskItemHelperClass() {
            @Override
            public void deleteItem(UserTask model) {
                if (!model.isCompleted()){
                        mRef.child("users").child(firebaseUser.getUid()).child("days")
                                .child(model.getDate()).child("userTasks").child(model.getTaskId()).removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "deleted task", Toast.LENGTH_SHORT).show();
                                mRef.child("users").child(firebaseUser.getUid()).child("days")
                                        .child(dateString).child("numberOfTasks").get().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        int noOfTask = task1.getResult().getValue(Integer.class);
                                        if (noOfTask <= 0) {
                                            Toast.makeText(context, "no task to delete", Toast.LENGTH_SHORT).show();
                                        } else {
                                            noOfTask--;
                                            mRef.child("users").child(firebaseUser.getUid()).child("days")
                                                    .child(dateString).child("numberOfTasks").setValue(noOfTask);
                                            mRef.child("users").child(firebaseUser.getUid()).child("days")
                                                    .child(dateString).child("remainingTasks").setValue(ServerValue.increment(-1));
                                            mRef.child("users").child(firebaseUser.getUid()).child("unfinishedTasks").setValue(ServerValue.increment(-1));
                                        }
                                    }
                                });
                            }
                        });
                }
            }
        });
        recyclerView.setAdapter(tasksadapter);

    }

    private void createTask() {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_task);
        EditText taskName = bottomSheetDialog.findViewById(R.id.editTextTaskName);
        EditText taskDetails = bottomSheetDialog.findViewById(R.id.editTextTextTaskDetails);
        Button cancelButton = bottomSheetDialog.findViewById(R.id.cancelTask);
        Button addTaskButton = bottomSheetDialog.findViewById(R.id.BSaddTaskButton);
        cancelButton.setOnClickListener(v -> {
            Toast.makeText(context, "canceling", Toast.LENGTH_SHORT).show();
        });
        addTaskButton.setOnClickListener(v ->{
            if(TextUtils.isEmpty(taskName.getText().toString()) || TextUtils.isEmpty(taskDetails.getText().toString())){
                Toast.makeText(context, "some Fields are empty", Toast.LENGTH_SHORT).show();
            }else{
                String taskNameString = taskName.getText().toString();
                String taskDetailsString = taskDetails.getText().toString();
                String uuid = UUID.randomUUID().toString();
                UserTask task_ = new UserTask(uuid,dateString,taskNameString, taskDetailsString, false);
                mRef.child("users").child(firebaseUser.getUid()).child("days")
                        .child(dateString).child("numberOfTasks").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            int noOfTask = task.getResult().getValue(Integer.class);
                            if(noOfTask >=5){
                                Toast.makeText(context, "task Limit reached", Toast.LENGTH_SHORT).show();
                            }else{
                                noOfTask++;
                                mRef.child("users").child(firebaseUser.getUid()).child("days")
                                        .child(dateString).child("numberOfTasks").setValue(noOfTask);
                                mRef.child("users").child(firebaseUser.getUid()).child("days")
                                        .child(dateString).child("remainingTasks").setValue(ServerValue.increment(1));
                                mRef.child("users").child(firebaseUser.getUid()).child("unfinishedTasks").setValue(ServerValue.increment(1));
                                mRef.child("users").child(firebaseUser.getUid()).child("days")
                                        .child(dateString).child("userTasks").child(uuid).setValue(task_).addOnCompleteListener(taskinner -> {
                                    if(taskinner.isSuccessful()){
                                        Toast.makeText(context, "Task added", Toast.LENGTH_SHORT).show();
                                        bottomSheetDialog.dismiss();
                                    }else {
                                        Toast.makeText(context, Objects.requireNonNull(taskinner.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                });
                            }
                        }
                    }
                });

            }
        });
        bottomSheetDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        tasksadapter.stopListening();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        tasksadapter.stopListening();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        tasksadapter.startListening();
//    }

    @Override
    protected void onStart() {
        super.onStart();
        tasksadapter.startListening();
    }
    public interface taskItemHelperClass {
        void deleteItem(UserTask model);
    }
}

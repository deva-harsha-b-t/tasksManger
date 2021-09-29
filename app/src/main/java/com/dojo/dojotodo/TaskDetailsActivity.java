package com.dojo.dojotodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class TaskDetailsActivity extends AppCompatActivity {
    private TextView taskName;
    private TextView taskDes;
    private Button markDoneButton;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference mRef;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.activity_task_details);
        taskName = findViewById(R.id.taskTitleTaskActivity);
        taskDes = findViewById(R.id.taskDesTaskActivity);
        markDoneButton = findViewById(R.id.doneButton);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        boolean task_isComplete = getIntent().getBooleanExtra("isComplete",false);
        String task_date = getIntent().getStringExtra("taskDate");
        String task_id = getIntent().getStringExtra("taskID");
        String task_name = getIntent().getStringExtra("taskTitle");
        String task_des = getIntent().getStringExtra("taskDes");
        markDoneButton.setOnClickListener(v->{
            if(task_isComplete){
                Toast.makeText(this, "Task Already completed", Toast.LENGTH_SHORT).show();
            }else{
                mRef.child("users").child(firebaseUser.getUid()).child("days")
                        .child(task_date).child("userTasks").child(task_id).child("completed").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mRef.child("users").child(firebaseUser.getUid()).child("days")
                                    .child(task_date).child("remainingTasks").setValue(ServerValue.increment(-1));
                            mRef.child("users").child(firebaseUser.getUid()).child("unfinishedTasks").setValue(ServerValue.increment(-1));
                            Toast.makeText(context, "marked done", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        taskName.setText(task_name);
        taskDes.setText(task_des);
    }
}
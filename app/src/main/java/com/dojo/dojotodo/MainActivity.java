package com.dojo.dojotodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dojo.dojotodo.adapters.daysadapter;
import com.dojo.dojotodo.models.Day;
import com.dojo.dojotodo.models.User;
import com.dojo.dojotodo.models.UserTask;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private Context context;
    private DatabaseReference mRef;
    private User currentUserData;
    private Button addTaskToday;
    private RecyclerView recyclerView;
    private daysadapter mAdapter;
    private TextView onOfTasksRemaining;
    private TextView superDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference();
        context = this.getApplicationContext();
        recyclerView = findViewById(R.id.daysRecyclerView);
        recyclerView.setLayoutManager(new linearLayoutMangerWrapper(this));
        addTaskToday = findViewById(R.id.addNewTask);
        onOfTasksRemaining = findViewById(R.id.UFTsNumber);
        superDays = findViewById(R.id.SuperDaysNumber);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        checkForNewUser();
        mRef.child("users").child(firebaseUser.getUid()).child("unfinishedTasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    int n = snapshot.getValue(Integer.class);
                    onOfTasksRemaining.setText(String.valueOf(n));
                }catch (NullPointerException ignored) { }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mRef.child("users").child(firebaseUser.getUid()).child("superDays").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    int n = snapshot.getValue(Integer.class);
                    superDays.setText(String.valueOf(n));
                }catch (NullPointerException ignored) { }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addTaskToday.setOnClickListener(v -> {
            addTask();
        });
        Query query = mRef.child("users").child(firebaseUser.getUid()).child("days").orderByChild("customDayId").limitToLast(50);

        FirebaseRecyclerOptions<Day> options =
                new FirebaseRecyclerOptions.Builder<Day>()
                        .setQuery(query, Day.class)
                        .build();
        mAdapter = new daysadapter(options,MainActivity.this);
        recyclerView.setAdapter(mAdapter);

        findViewById(R.id.insights).setOnClickListener(v-> {/* pushTask()*/
        });

    }

    private void checkForNewUser() {
        mRef.child("users").child(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.getResult().exists()){
                    createNewUserBoilerPlate();
                }
            }
        });
    }

    private void createNewUserBoilerPlate() {
        Toast.makeText(this, "creating database for new user", Toast.LENGTH_SHORT).show();
        User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(),null,0,3);
        mRef.child("users").child(firebaseUser.getUid()).setValue(user);
    }

    String getTodaysDay(){
        Date todayDate = Calendar.getInstance().getTime();
        String todayDateShort = DateFormat.getDateInstance(DateFormat.MEDIUM).format(todayDate);
        return todayDateShort;

    }
    private void addTask() {
        String todaysDateShort = getTodaysDay();
        mRef.child("users").child(firebaseUser.getUid()).child("days").child(todaysDateShort).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(context, "task already present", Toast.LENGTH_SHORT).show();
                }else{
                    createDayToday(todaysDateShort);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createDayToday(String todayDate) {
        HashMap<String,UserTask> defaultTask = new HashMap<>();
        long time = System.currentTimeMillis();
        String timeID = Long.toString(time);
        defaultTask.put("DEF101",new UserTask("DEF101",todayDate,"stay healthy", "drink water , eat healthy, don't forget to exercise", false));
        Day dayToday = new Day(1,1,timeID,todayDate,31,12,2021,true,defaultTask);
        mRef.child("users").child(firebaseUser.getUid()).child("days").child(todayDate).setValue(dayToday).addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                Toast.makeText(context, "created task for today", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mRef.child("users").child(firebaseUser.getUid()).child("superDays").setValue(ServerValue.increment(1));
        mRef.child("users").child(firebaseUser.getUid()).child("unfinishedTasks").setValue(ServerValue.increment(1));

    }

//    void pushTask(){
//        Toast.makeText(context, "adding", Toast.LENGTH_SHORT).show();
//        String id = firebaseUser.getUid();
//        HashMap<String,UserTask> userTasks = new HashMap<>();
//        HashMap<String,Day> Days = new HashMap<>();
//        userTasks.put("DEF103",new UserTask("DEF103","31122100","do something", "do something in detail", false));
//        userTasks.put("DEF105",new UserTask("DEF105","31122100","do something 2", "do something in detail 2", true));
//        Days.put("21 nov 2001", new Day(1,1,"31122100","21 nov 2001",30,11,2000,false,userTasks));
//        Days.put("22 nov 2001",new Day(1,1,"31132100","22 nov 2001",30,11,2000,true,userTasks));
//
//        User user = new User(id, firebaseUser.getDisplayName(),Days,1,30);
//
//        mRef.child("users").child(id).setValue(user);
//    }

    void getInsights(){
        String builder = "\n" +
                firebaseUser.getUid() +
                "\n" +
                firebaseUser.getEmail() +
                "\n" +
                firebaseUser.getDisplayName() +
                "\n" +
                firebaseUser.getPhoneNumber();
        Log.d("FireBase_User", builder);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        mAdapter.stopListening();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mAdapter.startListening();
//    }

}
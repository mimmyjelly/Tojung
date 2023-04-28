package com.example.tojung;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SubActivity extends AppCompatActivity {
    private UserDB database;
    private List<User> username;
    private Context mcontext;
    EditText EditguardianName;
    EditText EditguardianPhoneNumber;
    EditText EditwakeUpTime;
    EditText EditsleepTime;
    Button savebt;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        EditguardianName = (EditText) findViewById(R.id.guardianName);
        EditguardianPhoneNumber = (EditText)findViewById(R.id.guardianPhoneNumber);
        EditwakeUpTime = (EditText)findViewById(R.id.wakeUpTime);
        EditsleepTime = (EditText)findViewById(R.id.sleepTime);
        savebt = (Button) findViewById(R.id.savebt);
        mcontext = getApplicationContext();

        //DB 생성

        username = (List<User>) database.userDao().getname();


        class InsertRunnable implements Runnable{

            @Override
            public void run(){
                User user = new User();

            }
        }


        savebt.setOnClickListener(v -> {

            InsertRunnable insertRunnable = new InsertRunnable();
            Thread addThread = new Thread(insertRunnable);
            addThread.start();

            Intent intent = new Intent(getApplicationContext(), SubActivity.class);
            startActivity(intent);
            finish();

        });



        //main thread에서 DB 접근 불가 => data 읽고 쓸 때 thread 사용하기

        InsertRunnable insertRunnable = new InsertRunnable();
        Thread t = new Thread(insertRunnable);
        t.start();
    }
}

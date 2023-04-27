package com.example.tojung;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity {
    private UserDB userDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        //DB 생성
        userDB = UserDB.getDBInstance(this);

        //main thread에서 DB 접근 불가 => data 읽고 쓸 때 thread 사용하기
        class InsertRunnable implements Runnable{

            @Override
            public void run(){

            }
        }
        InsertRunnable insertRunnable = new InsertRunnable();
        Thread t = new Thread(insertRunnable);
        t.start();
    }
}

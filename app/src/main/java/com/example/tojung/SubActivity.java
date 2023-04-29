package com.example.tojung;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        EditText name_edit = findViewById(R.id.guardianName);
        EditText phone_edit = findViewById(R.id.guardianPhoneNumber);
        EditText wakeup_edit = findViewById(R.id.wakeUpTime);
        EditText sleep_edit = findViewById(R.id.sleepTime);
        Button savebt = findViewById(R.id.savebt);

        UserDao dao = new UserDao();

        savebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //입력값 변수에 담기
                String name = name_edit.getText().toString();       //이름
                String phone = phone_edit.getText().toString();     //전화번호
                String wakeup = wakeup_edit.getText().toString();   //기상시간
                String sleep = sleep_edit.getText().toString();     //취침시간

                User user = new User("",name, phone,wakeup,sleep);

                //데이터베이스 사용자 등록
                dao.add(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "입력되었습니다.", Toast.LENGTH_SHORT).show();
                        //입력창초기화
                        name_edit.setText("");
                        phone_edit.setText("");
                        wakeup_edit.setText("");
                        sleep_edit.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),  "실패" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //Main으로 넘어가기
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }

        });
    }
}

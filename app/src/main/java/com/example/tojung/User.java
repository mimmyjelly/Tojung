package com.example.tojung;

// 데이터가 담길 변수
import android.widget.TextView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="guardianName")
    public  String guardianName;

    @ColumnInfo(name="wakeUpTime")
    public  String wakeUpTime;

    @ColumnInfo(name="sleepTime")
    public  String sleepTime;

    @ColumnInfo(name="guardianPhoneNumber")
    public String guardianPhoneNumber;
}

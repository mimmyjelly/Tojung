package com.example.tojung;

// 데이터가 담길 변수
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name="userName")
    public  String userName;

    @ColumnInfo(name="userNum")
    public String userNum;
}

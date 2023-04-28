package com.example.tojung;

// 데이터가 담길 변수
import android.widget.TextView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "USER")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="text")
    public  String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

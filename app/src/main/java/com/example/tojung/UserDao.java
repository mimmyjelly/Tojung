package com.example.tojung;
// 데이터베이스의 데이터를 등록, 조회, 수정, 삭제 기능 제공
import android.widget.LinearLayout;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    //조회
    @Query("SELECT * FROM User")
    List<User> getAll();

    @Insert
    //삽입
    void insertUser(User user);

    @Delete
    //삭제
    void userDelete(User user);

    @Update
    //수정
    void userUpdate(User user);
}

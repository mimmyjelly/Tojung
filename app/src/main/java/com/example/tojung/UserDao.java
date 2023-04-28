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
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM User WHERE id = :id")
    User getUser(int id);

    @Query("SELECT * FROM User")
    List<User> getAllUsers();
}
package com.example.tojung;
// 데이터베이스의 데이터를 등록, 조회, 수정, 삭제 기능 제공
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    List<User> getAllUser = null;

    @Insert
    void insertUser(User user);

    @Delete
    void userDelete(User user);

    @Update
    void userUpdate(User user);
}

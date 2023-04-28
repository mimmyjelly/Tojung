package com.example.tojung;
// 데이터베이스의 데이터를 등록, 조회, 수정, 삭제 기능 제공
import static androidx.room.OnConflictStrategy.REPLACE;

import android.widget.LinearLayout;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = REPLACE)
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("UPDATE USER SET text = :sText WHERE ID = :sID")
    void update(int sID, String sText);
    @Query("SELECT * FROM USER")
    List<User> getAll();
    @Query("SELECT * FROM USER WHERE id = 1 LIMIT 1")
    User getname();

    @Query("SELECT * FROM USER WHERE id = 2 LIMIT 1")
    User getnumber();

    @Query("SELECT * FROM USER WHERE id = 2 LIMIT 1")
    User getwakeup();

    @Query("SELECT * FROM USER WHERE id = 2 LIMIT 1")
    User getsleep();
}
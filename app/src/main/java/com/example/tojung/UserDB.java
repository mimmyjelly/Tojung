package com.example.tojung;
import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public abstract class UserDB extends RoomDatabase {
    public  abstract UserDao userDao();
    private static UserDB INSTANCE;

    public static UserDB getDBInstance(Context context){
        if(INSTANCE == null){

            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    UserDB.class, "DB_NAME")
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }

}

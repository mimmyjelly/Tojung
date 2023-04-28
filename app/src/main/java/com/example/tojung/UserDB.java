package com.example.tojung;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class},version=1,exportSchema = false)
public abstract class UserDB extends RoomDatabase {
    private static UserDB database;
    private static String DATABASE_NAME = "database";

    public synchronized static UserDB getInstance(Context context)
    {
        if(database == null)
        {
            database = Room.databaseBuilder(context.getApplicationContext(),
                    UserDB.class,
                    DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;

    }

    public abstract UserDao userDao();
}

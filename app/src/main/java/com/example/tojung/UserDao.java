package com.example.tojung;

import com.example.tojung.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class UserDao {
    private DatabaseReference databaseReference;

    UserDao(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(User.class.getSimpleName());
    }

    //등록
    public Task<Void> add(User user){
        return databaseReference.push().setValue(user);
    }

    public Query get(){
        return databaseReference;
    }
}
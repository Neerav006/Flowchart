package com.demo.flowchart;

import android.app.Application;

import androidx.room.Room;

import com.demo.flowchart.auth.FirebaseRepository;
import com.demo.flowchart.database.AppDatabase;

public class App extends Application {

    private static App instance;
    private AppDatabase database;
    private FirebaseRepository firebase;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        firebase = new FirebaseRepository();
        database = Room.databaseBuilder(this, AppDatabase.class, "flowchart.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public FirebaseRepository getFirebase(){
        return firebase;
    }
}

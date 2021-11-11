package com.demo.flowchart;

import android.app.Application;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.demo.flowchart.database.AppDatabase;
import com.demo.flowchart.database.FlowchartDao;

public class App extends Application {

    private static App instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

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
}
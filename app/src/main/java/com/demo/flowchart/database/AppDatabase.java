package com.demo.flowchart.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FlowchartEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FlowchartDao flowchartDao();

}

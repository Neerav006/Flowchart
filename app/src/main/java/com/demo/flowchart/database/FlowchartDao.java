package com.demo.flowchart.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FlowchartDao {

    @Query("SELECT * FROM flowchartentity")
    List<FlowchartEntity> getAll();

    @Query("SELECT * FROM flowchartentity WHERE uid = :uid")
    FlowchartEntity get(long uid);

    @Insert
    long insert(FlowchartEntity flowchartEntity);

    @Update
    void update(FlowchartEntity flowchartEntity);

    @Delete
    void delete(FlowchartEntity flowchartEntity);
}

package com.barmej.notedeveloper2.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.barmej.notedeveloper2.data.entity.CheckNote;

import java.util.List;

@Dao
public interface CheckNoteDao {

    @Query("SELECT * FROM checkNote_table")
    LiveData<List<CheckNote>> getCheckNote();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCheckNote(CheckNote checkNote);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCheckNote(CheckNote checkNote);

    @Delete
    void deleteCheckNote(CheckNote checkNote);
}

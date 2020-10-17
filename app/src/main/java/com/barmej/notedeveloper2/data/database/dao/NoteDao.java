package com.barmej.notedeveloper2.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.barmej.notedeveloper2.data.entity.TextNote;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note_table")
    LiveData<List<TextNote>> getNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNote(TextNote textNote);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNote(TextNote textNote);

    @Delete
    void deleteNote(TextNote textNote);
}

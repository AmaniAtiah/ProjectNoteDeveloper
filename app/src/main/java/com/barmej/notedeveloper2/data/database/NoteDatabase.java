package com.barmej.notedeveloper2.data.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.barmej.notedeveloper2.NoteConverter;
import com.barmej.notedeveloper2.data.database.dao.CheckNoteDao;
import com.barmej.notedeveloper2.data.database.dao.NoteDao;
import com.barmej.notedeveloper2.data.database.dao.PhotoNoteDao;
import com.barmej.notedeveloper2.data.entity.CheckNote;
import com.barmej.notedeveloper2.data.entity.PhotoNote;
import com.barmej.notedeveloper2.data.entity.TextNote;

@Database(entities = {TextNote.class, PhotoNote.class, CheckNote.class}, version = 1, exportSchema = false)
@TypeConverters({NoteConverter.class})
public abstract class NoteDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static NoteDatabase sInstance;
    private static final String DATABASE_NAME = "note_db";

    public static NoteDatabase getsInstance(Context context) {
        if(sInstance == null) {
            synchronized (LOCK) {
                if(sInstance == null){
                    sInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            NoteDatabase.class,
                            DATABASE_NAME
                    ).allowMainThreadQueries().build();
                }
            }
        }
        return sInstance;
    }

    public abstract NoteDao noteDao();
    public abstract PhotoNoteDao photoNoteDao();
    public abstract CheckNoteDao checkNoteDao();
}

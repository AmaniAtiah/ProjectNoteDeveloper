package com.barmej.notedeveloper2.data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.barmej.notedeveloper2.AppExecutor;
import com.barmej.notedeveloper2.data.database.NoteDatabase;
import com.barmej.notedeveloper2.data.entity.CheckNote;
import com.barmej.notedeveloper2.data.entity.Note;
import com.barmej.notedeveloper2.data.entity.PhotoNote;
import com.barmej.notedeveloper2.data.entity.TextNote;

import java.util.ArrayList;
import java.util.List;

public class NoteRepository {
    private static final Object LOCK = new Object();
    private static NoteRepository sInstance;
    private NoteDatabase noteDatabase;
    private AppExecutor mAppExecutor;

    List<TextNote> textNoteList = new ArrayList<>();
    List<PhotoNote> photoNoteList = new ArrayList<>();
    List<CheckNote> checkNoteList = new ArrayList<>();

    public static NoteRepository getsInstance(Context context) {
        if(sInstance == null) {
            synchronized (LOCK) {
                if(sInstance == null)
                    sInstance = new NoteRepository(context.getApplicationContext());
            }
        }
        return sInstance;
    }

    private NoteRepository(Context context) {
        noteDatabase = NoteDatabase.getsInstance(context);
        mAppExecutor = AppExecutor.getInstance();
    }

    public void insert(final TextNote textNote) {

        if(textNote != null) {
            mAppExecutor.getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    noteDatabase.noteDao().addNote(textNote);
                }
            });
        }

    }
    public void update(TextNote textNote) {
        noteDatabase.noteDao().updateNote(textNote);

    }
    public void delete(TextNote textNote) {
        noteDatabase.noteDao().deleteNote(textNote);
    }

    public void insertPhotoNote(PhotoNote photoNote) {
        noteDatabase.photoNoteDao().addPhotoNote(photoNote);
    }

    public void updatePhotoNote(PhotoNote photoNote) {
        noteDatabase.photoNoteDao().updatePhotoNote(photoNote);
    }

    public void deletePhotoNote(PhotoNote photoNote) {
        noteDatabase.photoNoteDao().deletePhotoNote(photoNote);
    }


    public void insertCheckNote(CheckNote checkNote) {
        noteDatabase.checkNoteDao().addCheckNote(checkNote);
    }

    public void updateCheckNote(CheckNote checkNote) {
        noteDatabase.checkNoteDao().updateCheckNote(checkNote);
    }

    public void deleteCheckNote(CheckNote checkNote) {
        noteDatabase.checkNoteDao().deleteCheckNote(checkNote);
    }

    public LiveData<List<Note>> getAllNotes() {
        final MutableLiveData<List<Note>> notes = new MutableLiveData<>();
        final List<Note> allNote = new ArrayList<>();
        final LiveData<List<TextNote>> noteListLiveData = noteDatabase.noteDao().getNotes();
        final LiveData<List<PhotoNote>> photoNoteListLiveData = noteDatabase.photoNoteDao().getPhotoNotes();
        final LiveData<List<CheckNote>> checkNoteListLiveData = noteDatabase.checkNoteDao().getCheckNote();
        noteListLiveData.observeForever(new Observer<List<TextNote>>() {
            @Override
            public void onChanged(List<TextNote> textNotes) {
                allNote.clear();
                textNoteList = textNotes;
                allNote.addAll(textNotes);
                allNote.addAll(photoNoteList);
                allNote.addAll(checkNoteList);
                notes.setValue(allNote);
            }
        });

        photoNoteListLiveData.observeForever(new Observer<List<PhotoNote>>() {
            @Override
            public void onChanged(List<PhotoNote> photoNotes) {
                allNote.clear();
                photoNoteList = photoNotes;
                allNote.addAll(photoNotes);
                allNote.addAll(textNoteList);
                allNote.addAll(checkNoteList);
                notes.setValue(allNote);
            }
        });

        checkNoteListLiveData.observeForever(new Observer<List<CheckNote>>() {
            @Override
            public void onChanged(List<CheckNote> checkNotes) {
                allNote.clear();
                checkNoteList = checkNotes;
                allNote.addAll(checkNotes);
                allNote.addAll(textNoteList);
                allNote.addAll(photoNoteList);
                notes.setValue(allNote);
            }
        });
        return notes;
    }
}

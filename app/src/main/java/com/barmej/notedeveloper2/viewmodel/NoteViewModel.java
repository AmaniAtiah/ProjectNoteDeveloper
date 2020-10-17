package com.barmej.notedeveloper2.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barmej.notedeveloper2.data.NoteRepository;
import com.barmej.notedeveloper2.data.entity.CheckNote;
import com.barmej.notedeveloper2.data.entity.Note;
import com.barmej.notedeveloper2.data.entity.PhotoNote;
import com.barmej.notedeveloper2.data.entity.TextNote;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository mRepository;

    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        mRepository = NoteRepository.getsInstance(application);
        allNotes = mRepository.getAllNotes();
    }

    public void insertNote(TextNote textNote){
        mRepository.insert(textNote);

    }

    public void updateNote(TextNote textNote){
        mRepository.update(textNote);
    }

    public void deleteNote(TextNote textNote){
        mRepository.delete(textNote);
    }


    public void insertPhotoNote(PhotoNote photoNote){
        mRepository.insertPhotoNote(photoNote);
    }

    public void updatePhotoNote(PhotoNote photoNote){
        mRepository.updatePhotoNote(photoNote);
    }

    public void deletePhotoNote(PhotoNote photoNote){
        mRepository.deletePhotoNote(photoNote);
    }


    public void insertCheckNote(CheckNote checkNote){
        mRepository.insertCheckNote(checkNote);
    }

    public void updateCheckNote(CheckNote checkNote){
        mRepository.updateCheckNote(checkNote);
    }

    public void deleteCheckNote(CheckNote checkNote){
        mRepository.deleteCheckNote(checkNote);
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
}

package com.barmej.notedeveloper2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.barmej.notedeveloper2.adapter.NoteAdapter;
import com.barmej.notedeveloper2.data.entity.CheckNote;
import com.barmej.notedeveloper2.data.entity.Note;
import com.barmej.notedeveloper2.data.entity.PhotoNote;
import com.barmej.notedeveloper2.data.entity.TextNote;
import com.barmej.notedeveloper2.listener.ItemClickListenerCheckNote;
import com.barmej.notedeveloper2.listener.ItemClickListenerNote;
import com.barmej.notedeveloper2.listener.ItemLClickListenerPhotoNote;
import com.barmej.notedeveloper2.listener.ItemLongClickListener;
import com.barmej.notedeveloper2.viewmodel.NoteViewModel;
import java.util.ArrayList;
import java.util.List;

import static com.barmej.notedeveloper2.BR.checkNote;
import static com.barmej.notedeveloper2.BR.note;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_NOTE = 142;
    private static final int EDIT_NOTE = 140;
    private RecyclerView mRecyclerView;
    private ArrayList<Note> mItems;
    private NoteAdapter mAdapter;
    NoteViewModel noteViewModel;
    private int id;
    private Note note;

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view_photos);
        mItems = new ArrayList<>();
        mAdapter = new NoteAdapter(mItems, this, new ItemClickListenerNote() {
            @Override
            public void onClickItemNote(Note note) {
                editNote(note, NoteDetailsActivity.class);
            }
        }, new ItemLClickListenerPhotoNote() {
            @Override
            public void onClickItemPhotoNote(Note note) {
                editNote(note, PhotoNoteDetailsActivity.class);
            }
        }, new ItemClickListenerCheckNote() {
            @Override
            public void onClickItemCheckNote(Note note) {
                editNote(note, CheckNoteDetailsActivity.class);
            }
        }, new ItemLongClickListener() {
            @Override
            public void onLongClickItem(int position) {
                deleteItem(position);
            }
        });

        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.floating_button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddNewNoteActivity();
            }
        });

        getAllNotes();

    }

    private void startAddNewNoteActivity() {
        Intent intent = new Intent(MainActivity.this, AddNewNoteActivity.class);
        startActivityForResult(intent, ADD_NOTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE) {
            if (resultCode == RESULT_OK && data != null) {

                note = data.getParcelableExtra(Constants.NOTE);
                if(note instanceof PhotoNote) {
                    noteViewModel.insertPhotoNote((PhotoNote) note);
                }else if (note instanceof CheckNote) {
                    noteViewModel.insertCheckNote((CheckNote) note);
                }else {
                    noteViewModel.insertNote((TextNote) note);
                }
            }
        } else if (requestCode == EDIT_NOTE) {
            if (resultCode == RESULT_OK && data != null) {
                note = data.getParcelableExtra(Constants.NOTE);
                id = data.getIntExtra(Constants.POSITION, -1);
                note.setId(id);
                if(note instanceof PhotoNote) {
                    noteViewModel.updatePhotoNote((PhotoNote) note);
                }else if(note instanceof CheckNote) {
                    noteViewModel.updateCheckNote((CheckNote) note);
                }else {
                    noteViewModel.updateNote((TextNote) note);
                }
            }
        }
    }

    private void editNote(Note note, Class className) {
        Intent intent = new Intent(MainActivity.this, className);
        intent.putExtra(Constants.NOTE, note);
        intent.putExtra(Constants.POSITION, note.getId());
        startActivityForResult(intent, EDIT_NOTE);
    }

    private void deleteItem(final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.delete_confirmation)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Note note = mAdapter.getNoteAt(position);
                        if(note instanceof PhotoNote) {
                            noteViewModel.deletePhotoNote((PhotoNote) note);
                        }else if(note instanceof CheckNote) {
                            noteViewModel.deleteCheckNote((CheckNote) note);
                        }else {
                            noteViewModel.deleteNote((TextNote) note);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }

                })
                .create();
        alertDialog.show();
    }

    private void getAllNotes() {
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this,new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                mAdapter.updateNote(notes);
            }
        });
    }
}

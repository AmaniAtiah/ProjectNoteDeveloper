package com.barmej.notedeveloper2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.barmej.notedeveloper2.data.entity.Note;

public class NoteDetailsActivity extends AppCompatActivity {

    private EditText mEditTextNote;
    private View backgroundColor;

    private int color;
    private String textNoteEdit;
    private int id;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        mEditTextNote = findViewById(R.id.noteEditText);
        backgroundColor = findViewById(R.id.activity_Note_color);

        id = getIntent().getIntExtra(Constants.POSITION, -1);
        note = getIntent().getParcelableExtra(Constants.NOTE);
        color = note.getColor();

        mEditTextNote.setText(note.getTextNote());
        backgroundColor.setBackgroundColor(color);

        RadioGroup colors = findViewById(R.id.colorRadioGroup);
        final Button editBt = findViewById(R.id.button_edit);

        colors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.select_yellow_button:
                        color = ContextCompat.getColor(NoteDetailsActivity.this, R.color.yellow);
                        backgroundColor.setBackgroundResource(R.color.yellow);
                        break;

                    case R.id.select_red_button:
                        color = ContextCompat.getColor(NoteDetailsActivity.this, R.color.red);
                        backgroundColor.setBackgroundResource(R.color.red);
                        break;

                    case R.id.select_blue_button:
                        color = ContextCompat.getColor(NoteDetailsActivity.this, R.color.blue);
                        backgroundColor.setBackgroundResource(R.color.blue);
                        break;
                }
            }
        });

        editBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNote();
            }
        });
    }

    private void editNote() {
        textNoteEdit = mEditTextNote.getText().toString();

        if (textNoteEdit.trim().isEmpty()) {
            return;
        } else {
            note.setTextNote(textNoteEdit);
            note.setColor(color);
            Intent intent = new Intent();
            intent.putExtra(Constants.NOTE,note);
            intent.putExtra(Constants.POSITION,id);
            setResult(RESULT_OK,intent);
            finish();
        }
    }
}

package com.barmej.notedeveloper2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.barmej.notedeveloper2.data.entity.CheckNote;

public class CheckNoteDetailsActivity extends AppCompatActivity {

    private EditText mEditTextCheckNote;
    private CheckBox mCheckBoxNote;
    private View backgroundColor;
    private int color;
    private int id;
    private CheckNote checkNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_note_details);

        mEditTextCheckNote = findViewById(R.id.checkNoteEditText);
        mCheckBoxNote = findViewById(R.id.checkNoteCheckBox);
        backgroundColor = findViewById(R.id.activity_check_note_color);

        id = getIntent().getIntExtra(Constants.POSITION, -1);
        checkNote = getIntent().getParcelableExtra(Constants.NOTE);
        color = checkNote.getColor();

        mEditTextCheckNote.setText(checkNote.getTextNote());
        mCheckBoxNote.setChecked(checkNote.getCheckNote());
        backgroundColor.setBackgroundColor(color);

        final Button editCheckNoteBt = findViewById(R.id.button_check_edit);

        editCheckNoteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCheckNote();
            }
        });

        RadioGroup colors = findViewById(R.id.colorRadioGroup);
        colors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.select_yellow_button:
                        color = ContextCompat.getColor(CheckNoteDetailsActivity.this, R.color.yellow);
                        backgroundColor.setBackgroundResource(R.color.yellow);
                        break;

                    case R.id.select_red_button:
                        color = ContextCompat.getColor(CheckNoteDetailsActivity.this, R.color.red);
                        backgroundColor.setBackgroundResource(R.color.red);
                        break;

                    case R.id.select_blue_button:
                        color = ContextCompat.getColor(CheckNoteDetailsActivity.this, R.color.blue);
                        backgroundColor.setBackgroundResource(R.color.blue);
                        break;
                }
            }
        });
    }

    private void editCheckNote() {
        String textCheckNoteEdit = mEditTextCheckNote.getText().toString();
        if (textCheckNoteEdit.trim().isEmpty()) {
            return;
        }
        checkNote.setTextNote(textCheckNoteEdit);
        checkNote.setCheckNote(mCheckBoxNote.isChecked());
        checkNote.setColor(color);
        Intent intent = new Intent();
        intent.putExtra(Constants.POSITION, id);
        intent.putExtra(Constants.NOTE, checkNote);
        setResult(RESULT_OK, intent);
        finish();
    }
}

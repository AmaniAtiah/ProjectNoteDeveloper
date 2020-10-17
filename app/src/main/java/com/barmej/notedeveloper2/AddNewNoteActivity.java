package com.barmej.notedeveloper2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.barmej.notedeveloper2.data.entity.CheckNote;
import com.barmej.notedeveloper2.data.entity.Note;
import com.barmej.notedeveloper2.data.entity.PhotoNote;
import com.barmej.notedeveloper2.data.entity.TextNote;


public class AddNewNoteActivity extends AppCompatActivity {

    private static final int READ_PHOTO_FROM_GALLERY_PERMISSION = 130;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE =1001;
    private static final int PICK_IMAGE = 120;
    private int noteType = Constants.IMAGE_DATA;

    CardView cardViewNote;
    CardView cardViewPhoto;
    CardView cardViewCheck;

    EditText editTextNotePhoto;
    EditText editTextNote;
    EditText editTextCheckNote;

    private ImageView mNewPhotoIv;
    Uri mSelectPhotoUri;

    CheckBox checkBoxNote;
    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        RadioGroup checkNoteType = findViewById(R.id.typeRadioGroup);
        RadioGroup colors = findViewById(R.id.colorRadioGroup);

        cardViewNote = findViewById(R.id.cardViewNote);
        cardViewPhoto = findViewById(R.id.cardViewPhoto);
        cardViewCheck = findViewById(R.id.cardViewCheckNote);

        editTextNotePhoto = findViewById(R.id.photoNoteEditText);
        editTextNote = findViewById(R.id.noteEditText);
        editTextCheckNote = findViewById(R.id.checkNoteEditText);

        mNewPhotoIv = findViewById(R.id.photoImageView);
        checkBoxNote = findViewById(R.id.checkNoteCheckBox);

        color = ContextCompat.getColor(this, R.color.transparent);

        mNewPhotoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        final Button submitBt = findViewById(R.id.button_submit);
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        checkNoteType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.select_photo_button:
                        noteType = Constants.IMAGE_DATA;
                        cardViewPhoto.setVisibility(View.VISIBLE);
                        cardViewCheck.setVisibility(View.GONE);
                        cardViewNote.setVisibility(View.GONE);
                        break;

                    case R.id.select_check_button:
                        noteType = Constants.CHECK_DATA;
                        cardViewCheck.setVisibility(View.VISIBLE);
                        cardViewNote.setVisibility(View.GONE);
                        cardViewPhoto.setVisibility(View.GONE);
                        break;

                    case R.id.select_note_button:
                        noteType = Constants.NOTE_DATA;
                        cardViewNote.setVisibility(View.VISIBLE);
                        cardViewCheck.setVisibility(View.GONE);
                        cardViewPhoto.setVisibility(View.GONE);
                        break;
                }
            }
        });

        colors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.select_yellow_button:
                        color = ContextCompat.getColor(AddNewNoteActivity.this, R.color.yellow);
                        cardViewPhoto.setBackgroundResource(R.color.yellow);
                        cardViewNote.setBackgroundResource(R.color.yellow);
                        cardViewCheck.setBackgroundResource(R.color.yellow);
                        break;

                    case R.id.select_red_button:
                        color = ContextCompat.getColor(AddNewNoteActivity.this, R.color.red);
                        cardViewPhoto.setBackgroundResource(R.color.red);
                        cardViewNote.setBackgroundResource(R.color.red);
                        cardViewCheck.setBackgroundResource(R.color.red);
                        break;

                    case R.id.select_blue_button:
                        color = ContextCompat.getColor(AddNewNoteActivity.this, R.color.blue);
                        cardViewPhoto.setBackgroundResource(R.color.blue);
                        cardViewNote.setBackgroundResource(R.color.blue);
                        cardViewCheck.setBackgroundResource(R.color.blue);
                        break;
                }
            }
        });
    }

    private void selectImage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewNoteActivity.this)
                .setTitle(R.string.add_image)
                .setItems(R.array.image,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case 0:
                                takePhoto();
                                break;
                            case 1:
                                selectPhoto();
                                break;
                        }
                    }
                });

        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }else {
                Toast.makeText(this,R.string.camera_access_is_required,Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == READ_PHOTO_FROM_GALLERY_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                firePickPhotoIntent();
            } else {
                Toast.makeText(this,R.string.read_permission_needed_to_access_files,Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == IMAGE_CAPTURE_CODE) {
            if (resultCode == RESULT_OK) {
                mNewPhotoIv.setImageURI(mSelectPhotoUri);
            }else {
                Toast.makeText(this,R.string.failed_to_take_image,Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                setSelectPhoto(data.getData());
                getContentResolver().takePersistableUriPermission(data.getData(),Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                Toast.makeText(this,R.string.failed_to_get_image,Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setSelectPhoto(Uri data) {
        mNewPhotoIv.setImageURI(data);
        mSelectPhotoUri = data;
    }

    private void takePhoto(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String [] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);
            } else {
                openCamera();
            }
        }
    }

    private void selectPhoto() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PHOTO_FROM_GALLERY_PERMISSION);

        } else {
            firePickPhotoIntent();
        }
    }

    protected void firePickPhotoIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE);
    }

    protected void openCamera(){
        ContentValues values = new ContentValues();
        mSelectPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mSelectPhotoUri);
        startActivityForResult(Intent.createChooser(intent,getString(R.string.take_picture)), IMAGE_CAPTURE_CODE);
    }

    private void submit() {
        Note note = null ;
        Intent intent = new Intent();

        if (noteType == Constants.NOTE_DATA) {

            String textNote = editTextNote.getText().toString();
            if (textNote.trim().isEmpty()) {
                Toast.makeText(this,R.string.add_note,Toast.LENGTH_LONG).show();
                return;

            }else {
                note = new TextNote(textNote,color);

            }
        }
        else if (noteType == Constants.IMAGE_DATA) {

            String textNotePhoto = editTextNotePhoto.getText().toString();
            if (textNotePhoto.trim().isEmpty() || mSelectPhotoUri == null) {
                Toast.makeText(this,R.string.add_note,Toast.LENGTH_LONG).show();

                return;
            }else {
                note = new PhotoNote(textNotePhoto, color, mSelectPhotoUri);
            }

        } else if(noteType == Constants.CHECK_DATA) {

            String textNoteCheck = editTextCheckNote.getText().toString();
            boolean checkNote = checkBoxNote.isChecked();
            if (textNoteCheck.trim().isEmpty()) {
                if(checkBoxNote.isChecked()){
                    Toast.makeText(this,R.string.add_note,Toast.LENGTH_LONG).show();
                    return;
                }

            } else {
                note = new CheckNote(textNoteCheck,color,checkNote);
            }
        }
        intent.putExtra(Constants.NOTE, note);
        setResult(RESULT_OK, intent);
        finish();
    }
}

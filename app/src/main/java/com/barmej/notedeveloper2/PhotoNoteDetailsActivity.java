package com.barmej.notedeveloper2;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.barmej.notedeveloper2.data.entity.PhotoNote;

public class PhotoNoteDetailsActivity extends AppCompatActivity {

    private static final int READ_PHOTO_FROM_GALLERY_PERMISSION = 130;
    private static final int PICK_IMAGE = 120;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE =1001;
    private EditText mEditTextPhotoNote;
    private ImageView mEditPhotoNoteIv;
    private View backgroundColor;
    private int color;
    private Uri mSelectPhotoUri;
    private int id;
    private PhotoNote photoNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_note_details);

        id = getIntent().getIntExtra(Constants.POSITION, -1);

        mEditTextPhotoNote = findViewById(R.id.photoNoteEditText);
        mEditPhotoNoteIv = findViewById(R.id.photoImageView);
        backgroundColor = findViewById(R.id.activity_photo_note_color);

        final Button editPhotoNoteBt = findViewById(R.id.edit_photo_note_submit);
        editPhotoNoteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPhotoNote();
            }
        });

        id = getIntent().getIntExtra(Constants.POSITION, -1);
        photoNote = getIntent().getParcelableExtra(Constants.NOTE);

        mSelectPhotoUri = photoNote.getImageNote();
        color = photoNote.getColor();

        mEditTextPhotoNote.setText(photoNote.getTextNote());
        mEditPhotoNoteIv.setImageURI(mSelectPhotoUri);
        backgroundColor.setBackgroundColor(color);

        mEditPhotoNoteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        RadioGroup colors = findViewById(R.id.colorRadioGroup);

        colors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.select_yellow_button:
                        color = ContextCompat.getColor(PhotoNoteDetailsActivity.this, R.color.yellow);
                        backgroundColor.setBackgroundResource(R.color.yellow);
                        break;

                    case R.id.select_red_button:
                        color = ContextCompat.getColor(PhotoNoteDetailsActivity.this, R.color.red);
                        backgroundColor.setBackgroundResource(R.color.red);
                        break;

                    case R.id.select_blue_button:
                        color = ContextCompat.getColor(PhotoNoteDetailsActivity.this, R.color.blue);
                        backgroundColor.setBackgroundResource(R.color.blue);
                        break;
                }
            }
        });
    }

    private void selectImage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoNoteDetailsActivity.this)
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

    protected void openCamera(){
        ContentValues values = new ContentValues();
        mSelectPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mSelectPhotoUri);
        startActivityForResult(Intent.createChooser(intent,getString(R.string.take_picture)), IMAGE_CAPTURE_CODE);
    }

    private void editPhotoNote() {

        String textPhotoNoteEdit = mEditTextPhotoNote.getText().toString();

        if (textPhotoNoteEdit.trim().isEmpty() || mSelectPhotoUri == null) {
            return;
        }

        photoNote.setTextNote(textPhotoNoteEdit);
        photoNote.setImageNote(mSelectPhotoUri);
        photoNote.setColor(color);
        Intent intent = new Intent();
        intent.putExtra(Constants.NOTE, photoNote);
        intent.putExtra(Constants.POSITION, id);
        setResult(RESULT_OK, intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_CAPTURE_CODE) {
            if (resultCode == RESULT_OK) {
                mEditPhotoNoteIv.setImageURI(mSelectPhotoUri);
            }
        }
        else if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                setSelectPhoto(data.getData());

                getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }

    }

    private void setSelectPhoto(Uri data) {
        mEditPhotoNoteIv.setImageURI(data);
        mSelectPhotoUri = data;
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
}

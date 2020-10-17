package com.barmej.notedeveloper2.data.entity;

import android.net.Uri;
import android.os.Parcel;
import androidx.room.Entity;

@Entity(tableName = "photoNote_table")
public class PhotoNote extends Note {
    private Uri imageNote;

    public PhotoNote(String textNote,int color,Uri imageNote) {
        super(textNote, color);
        this.setImageNote(imageNote);
    }

    protected PhotoNote(Parcel in) {
        super(in);
        imageNote = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<PhotoNote> CREATOR = new Creator<PhotoNote>() {
        @Override
        public PhotoNote createFromParcel(Parcel in) {
            return new PhotoNote(in);
        }

        @Override
        public PhotoNote[] newArray(int size) {
            return new PhotoNote[size];
        }
    };

    public Uri getImageNote() {
        return imageNote;
    }

    public void setImageNote(Uri imageNote) {
        this.imageNote = imageNote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(imageNote, flags);
    }
}


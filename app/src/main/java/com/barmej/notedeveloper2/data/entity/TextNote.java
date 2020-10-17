
package com.barmej.notedeveloper2.data.entity;

import android.os.Parcel;

import androidx.room.Entity;

@Entity(tableName = "note_table")
public class TextNote extends Note {

    public TextNote(String textNote, int color) {
        super(textNote, color);
    }

    protected TextNote(Parcel in) {
        super(in);
    }

    public static final Creator<TextNote> CREATOR = new Creator<TextNote>() {
        @Override
        public TextNote createFromParcel(Parcel in) {
            return new TextNote(in);
        }

        @Override
        public TextNote[] newArray(int size) {
            return new TextNote[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

}
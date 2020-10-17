package com.barmej.notedeveloper2.data.entity;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.PrimaryKey;


public abstract class Note implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;
    private String textNote;
    private int color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Note(String textNote, int color) {

        this.setTextNote(textNote);
        this.setColor(color);
    }

    protected Note(Parcel in) {

        textNote = in.readString();
        color = in.readInt();
    }

    public String getTextNote() {
        return textNote;
    }

    public int getColor() {
        return color;
    }

    public void setTextNote(String textNote) {
        this.textNote = textNote;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest,int flags) {

        dest.writeString(textNote);
        dest.writeInt(color);
    }
}
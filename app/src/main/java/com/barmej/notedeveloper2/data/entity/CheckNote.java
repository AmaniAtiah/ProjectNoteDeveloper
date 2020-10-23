package com.barmej.notedeveloper2.data.entity;

import android.os.Parcel;

import androidx.room.Entity;
@Entity(tableName = "checkNote_table")
public class CheckNote extends Note {
    private boolean checkNote;

    public CheckNote(String textNote,int color,boolean checkNote) {
        super(textNote, color);
        this.checkNote = checkNote;
    }


    protected CheckNote(Parcel in) {
        super(in);
        checkNote = in.readByte() != 0;
    }

    public static final Creator<CheckNote> CREATOR = new Creator<CheckNote>() {
        @Override
        public CheckNote createFromParcel(Parcel in) {
            return new CheckNote(in);
        }

        @Override
        public CheckNote[] newArray(int size) {
            return new CheckNote[size];
        }
    };

    public void setCheckNote(boolean checkNote) {
        this.checkNote = checkNote;
    }

    public boolean getCheckNote() {
        return checkNote;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (checkNote ? 1 : 0));
    }
}

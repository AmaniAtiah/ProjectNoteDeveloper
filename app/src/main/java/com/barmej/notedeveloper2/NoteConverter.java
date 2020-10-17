package com.barmej.notedeveloper2;

import android.net.Uri;

import androidx.room.TypeConverter;

public class NoteConverter {

    @TypeConverter
    public static Uri fromString(String value) {
        return Uri.parse(value);
    }

    @TypeConverter
    public static String toString(Uri uri) {
        return uri.toString();
    }
}

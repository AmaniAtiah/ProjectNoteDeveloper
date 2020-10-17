package com.barmej.notedeveloper2.databinding;

import android.net.Uri;
import android.widget.ImageView;
import androidx.databinding.BindingAdapter;

public class ImageLoadingBindingAdapter {

    @BindingAdapter("image")
    public static void loadImage(ImageView imageView, Uri imageUrl){
        imageView.setImageURI(imageUrl);
    }
}

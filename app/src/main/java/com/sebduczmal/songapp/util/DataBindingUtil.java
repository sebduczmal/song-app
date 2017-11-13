package com.sebduczmal.songapp.util;


import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DataBindingUtil {

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, String imageUrl) {
        if (imageUrl == null) {
            return;
        }
        Picasso.with(imageView.getContext()).load(imageUrl).into(imageView);
    }
}

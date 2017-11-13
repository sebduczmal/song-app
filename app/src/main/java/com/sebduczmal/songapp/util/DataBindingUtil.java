package com.sebduczmal.songapp.util;


import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DataBindingUtil {

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, String imageUrl) {
        if (imageUrl != null) {
            Picasso.with(imageView.getContext()).load(imageUrl).into(imageView);
        }
    }

    @BindingAdapter(value = {"imageUrl", "imagePlaceholder"})
    public static void loadImageWithPlaceholder(ImageView imageView, String imageUrl, Drawable
            placeholder) {
        if (imageView != null) {
            Picasso.with(imageView.getContext()).load(imageUrl).placeholder(placeholder).into
                    (imageView);
        }
    }
}

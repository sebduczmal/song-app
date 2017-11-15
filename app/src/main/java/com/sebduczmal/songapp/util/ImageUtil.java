package com.sebduczmal.songapp.util;


import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.graphics.Palette;
import android.view.View;

public class ImageUtil {

    private static final float VIEW_CENTER = 0.5f;

    public static void radialGradientFromBitmap(Bitmap bitmap, View destinationView) {
        Palette.from(bitmap).generate(palette -> {
            final Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
            final Palette.Swatch dominantSwatch = palette.getDominantSwatch();
            final Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();
            if (dominantSwatch != null && vibrantSwatch != null && darkMutedSwatch != null) {
                final GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable
                        .Orientation.BL_TR, new int[]{
                        vibrantSwatch.getRgb(),
                        darkMutedSwatch.getRgb(),
                        dominantSwatch.getRgb()});
                gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
                final float backgroundWidth = destinationView.getWidth();
                gradientDrawable.setGradientRadius(backgroundWidth);
                gradientDrawable.setGradientCenter(VIEW_CENTER, VIEW_CENTER);
                destinationView.setBackground(gradientDrawable);
            }
        });
    }

}

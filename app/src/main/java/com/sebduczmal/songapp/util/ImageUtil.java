package com.sebduczmal.songapp.util;


import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.graphics.Palette;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageUtil {

    private static final float VIEW_CENTER = 0.5f;

    public static void radialGradientFromBitmap(Bitmap bitmap, View destinationView) {
        Palette.from(bitmap).generate(palette -> {
            final GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable
                    .Orientation.BL_TR, getColors(palette));
            gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
            final float backgroundWidth = destinationView.getWidth();
            gradientDrawable.setGradientRadius(backgroundWidth);
            gradientDrawable.setGradientCenter(VIEW_CENTER, VIEW_CENTER);
            destinationView.setBackground(gradientDrawable);
        });
    }

    private static int[] getColors(Palette palette) {
        final List<Palette.Swatch> swatches = new ArrayList<>();
        swatches.add(palette.getVibrantSwatch());
        swatches.add(palette.getDarkMutedSwatch());
        swatches.add(palette.getDominantSwatch());
        swatches.removeAll(Collections.singleton(null));
        final int colors[] = new int[swatches.size()];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = swatches.get(i).getRgb();
        }
        return colors;
    }
}

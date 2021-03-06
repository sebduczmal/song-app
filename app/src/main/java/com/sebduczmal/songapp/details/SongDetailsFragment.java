package com.sebduczmal.songapp.details;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.sebduczmal.songapp.R;
import com.sebduczmal.songapp.data.SongModel;
import com.sebduczmal.songapp.databinding.DialogFragmentSongDetailsBinding;
import com.sebduczmal.songapp.util.ImageUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class SongDetailsFragment extends DialogFragment {

    private static final String KEY_SONG_MODEL = "song_model_key";

    private DialogFragmentSongDetailsBinding binding;
    private SongModel songModel;

    public static SongDetailsFragment newInstance(@NonNull SongModel songModel) {
        SongDetailsFragment songDetailsFragment = new SongDetailsFragment();
        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SONG_MODEL, songModel);
        songDetailsFragment.setArguments(bundle);
        return songDetailsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_song_details,
                container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        songModel = getArguments().getParcelable(KEY_SONG_MODEL);
        if (songModel == null) {
            dismiss();
        }
        binding.setModel(songModel);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onResume() {
        super.onResume();
        setAlbumCoverBackgroundColor();
    }

    private void setAlbumCoverBackgroundColor() {
        Picasso.with(getActivity()).load(songModel.getThumbnailUrl()).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ImageUtil.radialGradientFromBitmap(bitmap, binding.albumCoverBackground);
            }

            @Override public void onBitmapFailed(Drawable errorDrawable) {}

            @Override public void onPrepareLoad(Drawable placeHolderDrawable) {}
        });
    }
}

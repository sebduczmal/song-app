package com.sebduczmal.songapp.list;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sebduczmal.songapp.R;
import com.sebduczmal.songapp.data.SongModel;
import com.sebduczmal.songapp.databinding.ListItemSongBinding;

import java.util.ArrayList;
import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongListViewHolder> {

    private List<SongModel> songsToDisplay;
    private List<SongModel> songsBase;
    private OnSongListClickListener onSongListClickListener;

    public SongListAdapter() {
        songsToDisplay = new ArrayList<>();
    }

    @Override
    public SongListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemSongBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent
                .getContext()), R.layout.list_item_song, parent, false);
        return new SongListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SongListViewHolder holder, int position) {
        final SongModel songModel = songsToDisplay.get(position);
        final ListItemSongBinding binding = holder.viewDataBinding;

        binding.setModel(songModel);
        binding.setListener(onSongListClickListener);
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return songsToDisplay.size();
    }

    public void updateSongsToDisplay(List<SongModel> songs) {
        this.songsToDisplay.clear();
        this.songsToDisplay.addAll(songs);
        notifyDataSetChanged();
    }

    public void updateSongsBase(List<SongModel> songs) {
        songsBase = songs;
        updateSongsToDisplay(songsBase);
    }

    public List<SongModel> getSongsToDisplay() {
        return songsToDisplay;
    }

    public List<SongModel> getSongsBase() {
        return songsBase;
    }

    public void setOnSongListClickListener(OnSongListClickListener listener) {
        onSongListClickListener = listener;
    }

    static class SongListViewHolder extends RecyclerView.ViewHolder {

        private final ListItemSongBinding viewDataBinding;

        public SongListViewHolder(ListItemSongBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            this.viewDataBinding = viewDataBinding;
        }
    }
}

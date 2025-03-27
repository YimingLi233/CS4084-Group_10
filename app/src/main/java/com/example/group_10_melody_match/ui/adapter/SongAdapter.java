package com.example.group_10_melody_match.ui.adapter;

import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group_10_melody_match.R;
import com.example.group_10_melody_match.data.database.entity.Song;
import com.example.group_10_melody_match.ui.activity.SongPlayActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying songs in a RecyclerView
 * 
 * This adapter is enhanced to support song navigation by:
 * - Passing the entire song list to SongPlayActivity
 * - Providing the selected song's position within the list
 * - Using Parcelable to efficiently transfer data between activities
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private List<Song> songs = new ArrayList<>();

    public void setSongs(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songTitle.setText(song.getTitle());
        holder.songArtist.setText("Artist ID: " + song.getArtistName());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), SongPlayActivity.class);

            // Add basic song information
            intent.putExtra("song_title", song.getTitle());
            intent.putExtra("song_artist", song.getArtistName());
            intent.putExtra("song_image", song.getImageUrl());
            intent.putExtra("song_url", song.getResourceUrl());

            // The current position is crucial for navigation functionality
            // It lets SongPlayActivity know which song in the list is currently playing
            intent.putExtra("current_position", position);

            // Pass the entire song list to enable previous/next functionality
            // This allows SongPlayActivity to navigate between songs without
            // returning to the song list activity
            intent.putParcelableArrayListExtra("all_songs", new ArrayList<>(songs));

            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle, songArtist;
        ImageView songImage;

        SongViewHolder(View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.song_title);
            songArtist = itemView.findViewById(R.id.song_artist);
            songImage = itemView.findViewById(R.id.song_image);
        }
    }
}

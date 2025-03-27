package com.example.group_10_melody_match.ui.adapter;

import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
    private static final String TAG = "SongAdapter";
    private List<Song> songs = new ArrayList<>();

    public void setSongs(List<Song> songs) {
        this.songs = songs != null ? songs : new ArrayList<>();
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
        try {
            Song song = songs.get(position);

            // Set text safely
            holder.songTitle.setText(song.getTitle() != null ? song.getTitle() : "Unknown Title");
            holder.songArtist.setText(song.getArtistName() != null ? song.getArtistName() : "Unknown Artist");

            // Set a default or generated image for the song
            holder.songImage.setImageResource(R.drawable.ic_launcher_foreground);

            // Set click listener for the whole item to play the song
            holder.itemView.setOnClickListener(view -> {
                playSong(view, song, position);
            });

            // Set click listener for the play button
            if (holder.btnPlaySong != null) {
                holder.btnPlaySong.setOnClickListener(view -> {
                    playSong(view, song, position);
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "Error binding song at position " + position, e);
        }
    }

    /**
     * Start the song play activity with the selected song
     * 
     * @param view     The view that was clicked
     * @param song     The song to play
     * @param position The position of the song in the list
     */
    private void playSong(View view, Song song, int position) {
        try {
            Intent intent = new Intent(view.getContext(), SongPlayActivity.class);

            // Validate song details before passing
            String title = song.getTitle() != null ? song.getTitle() : "Unknown Title";
            String artist = song.getArtistName() != null ? song.getArtistName() : "Unknown Artist";
            String imageUrl = song.getImageUrl() != null ? song.getImageUrl() : "";
            String resourceUrl = song.getResourceUrl();

            // Check if song has a valid resource URL
            if (resourceUrl == null || resourceUrl.isEmpty()) {
                Toast.makeText(view.getContext(), "Song cannot be played - missing audio file", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            // Add basic song information
            intent.putExtra("song_title", title);
            intent.putExtra("song_artist", artist);
            intent.putExtra("song_image", imageUrl);
            intent.putExtra("song_url", resourceUrl);

            // The current position is crucial for navigation functionality
            intent.putExtra("current_position", position);

            // Pass the entire song list to enable previous/next functionality
            intent.putParcelableArrayListExtra("all_songs", new ArrayList<>(songs));

            view.getContext().startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error playing song", e);
            Toast.makeText(view.getContext(), "Error playing song", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle, songArtist;
        ImageView songImage;
        ImageButton btnPlaySong;

        SongViewHolder(View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.song_title);
            songArtist = itemView.findViewById(R.id.song_artist);
            songImage = itemView.findViewById(R.id.song_image);
            btnPlaySong = itemView.findViewById(R.id.btn_play_song);
        }
    }
}

package com.example.group_10_melody_match.ui.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.group_10_melody_match.R;
import com.example.group_10_melody_match.data.database.entity.Song;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity for playing songs with media controls
 * 
 * Features:
 * - Play/pause functionality
 * - Previous song navigation (newly added)
 * - Handles song playback with MediaPlayer
 */
public class SongPlayActivity extends AppCompatActivity {
    private static final String TAG = "SongPlayActivity";

    private MediaPlayer mediaPlayer;
    private ImageButton playPauseButton;
    private ImageButton prevButton;
    private String songUrl;
    private boolean isPlaying = false;

    // Song navigation properties
    // These fields enable tracking the current song position and available songs
    // for implementing previous/next functionality
    private List<Song> songList;
    private int currentPosition;

    private TextView titleTextView;
    private TextView artistTextView;
    private ImageView albumCover;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_play);

        // Get intent data
        String songTitle = getIntent().getStringExtra("song_title");
        String songArtist = getIntent().getStringExtra("song_artist");
        String songImage = getIntent().getStringExtra("song_image");
        songUrl = getIntent().getStringExtra("song_url");

        // Retrieve position data for navigation between songs
        // Default to 0 if not provided (first song in list)
        currentPosition = getIntent().getIntExtra("current_position", 0);

        // Get list of all songs for the current artist
        // This allows us to navigate between songs without returning to song list
        songList = getIntent().getParcelableArrayListExtra("all_songs");
        if (songList == null) {
            songList = new ArrayList<>();
            Log.e(TAG, "No song list provided");
        }

        // Initialize UI components
        titleTextView = findViewById(R.id.song_title);
        artistTextView = findViewById(R.id.song_artist);
        albumCover = findViewById(R.id.album_cover);
        playPauseButton = findViewById(R.id.btn_play_pause);
        prevButton = findViewById(R.id.btn_prev);

        // Set UI data
        titleTextView.setText(songTitle);
        artistTextView.setText(songArtist);
        albumCover.setImageResource(R.drawable.ic_launcher_foreground);

        // Initialize MediaPlayer
        initializeMediaPlayer();

        // Play/Pause button logic
        playPauseButton.setOnClickListener(view -> {
            if (isPlaying) {
                mediaPlayer.pause();
                playPauseButton.setImageResource(android.R.drawable.ic_media_play);
            } else {
                mediaPlayer.start();
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
            }
            isPlaying = !isPlaying;
        });

        // Previous button logic
        // This handler enables users to navigate to previous songs in the playlist
        prevButton.setOnClickListener(view -> {
            playPreviousSong();
        });
    }

    /**
     * Initialize or reset the MediaPlayer with the current song
     * 
     * This method is extracted to avoid code duplication between initial setup
     * and when changing songs with previous/next buttons
     */
    private void initializeMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, android.net.Uri.parse(songUrl));
            mediaPlayer.prepare();
        } catch (IOException e) {
            Log.e(TAG, "Error initializing media player", e);
            Toast.makeText(this, "Error playing song", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Plays the previous song in the song list if available
     * 
     * Implementation details:
     * 1. Checks if a previous song exists in the list
     * 2. Updates position counter and retrieves previous song
     * 3. Updates UI with new song information
     * 4. Resets media player with new song URL
     * 5. Maintains the current play/pause state during transition
     * 
     * This approach provides a seamless transition between songs while
     * preserving the user's chosen playback state
     */
    private void playPreviousSong() {
        // Check if there's a previous song
        // Boundary check to avoid IndexOutOfBoundsException
        if (songList.isEmpty() || currentPosition <= 0) {
            Toast.makeText(this, "No previous song available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Go to previous song by decrementing position
        currentPosition--;

        // Update the current song reference
        Song previousSong = songList.get(currentPosition);

        // Update UI with new song details
        titleTextView.setText(previousSong.getTitle());
        artistTextView.setText(previousSong.getArtistName());

        // Reset media player for the new song
        if (mediaPlayer != null) {
            // Remember if we were playing to maintain state during transition
            boolean wasPlaying = isPlaying;
            mediaPlayer.release();

            // Set new song
            mediaPlayer = new MediaPlayer();
            try {
                // Update song URL and prepare new media player
                songUrl = previousSong.getResourceUrl();
                mediaPlayer.setDataSource(this, android.net.Uri.parse(songUrl));
                mediaPlayer.prepare();

                // Restore previous playback state
                if (wasPlaying) {
                    // Continue playing if song was playing before
                    mediaPlayer.start();
                    playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
                    isPlaying = true;
                } else {
                    // Maintain paused state if song was paused before
                    playPauseButton.setImageResource(android.R.drawable.ic_media_play);
                    isPlaying = false;
                }
            } catch (IOException e) {
                // Handle errors gracefully with both logs and user feedback
                Log.e(TAG, "Error playing previous song", e);
                Toast.makeText(this, "Error playing previous song", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up resources to prevent memory leaks
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

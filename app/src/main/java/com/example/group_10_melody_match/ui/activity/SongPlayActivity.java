package com.example.group_10_melody_match.ui.activity;

import static org.jetbrains.annotations.Nls.Capitalization.Title;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.group_10_melody_match.R;
import com.example.group_10_melody_match.data.database.entity.Song;
import com.example.group_10_melody_match.data.repository.SongRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity for playing songs with media controls
 * 
 * Features:
 * - Play/pause functionality
 * - Previous song navigation
 * - Next song navigation
 * - Handles song playback with MediaPlayer
 */
public class SongPlayActivity extends AppCompatActivity {
    private static final String TAG = "SongPlayActivity";

    private MediaPlayer mediaPlayer;
    private ImageButton playPauseButton;
    private ImageButton prevButton;
    private ImageButton nextButton;
    private ImageButton likeButton;
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

        // Set up back navigation safely
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Get intent data
        String songTitle = getIntent().getStringExtra("song_title");
        String songArtist = getIntent().getStringExtra("song_artist");
        String songImage = getIntent().getStringExtra("song_cover");
        songUrl = getIntent().getStringExtra("song_url");

        // Set repository and fetch the song
        SongRepository songRepository = new SongRepository(getApplication());
        LiveData<Song> songLiveData = songRepository.getSongByTitle(songTitle);

        likeButton = findViewById(R.id.like_button); // Reference the like button in the layout

        // Observe the LiveData
        songLiveData.observe(this, song -> {
            if (song != null) {
                // Set the like button based on the song's like status
                runOnUiThread(() -> {
                    if (song.isLiked()) {
                        likeButton.setImageResource(R.drawable.ic_like_filled);
                        Log.d(TAG, "Song is liked");
                    } else {
                        likeButton.setImageResource(R.drawable.ic_like_empty);
                        Log.d(TAG, "Song is not liked");
                    }
                });

                // set the click listener for the like button
                likeButton.setOnClickListener(v -> {
                    boolean newLikedStatus = !song.isLiked();
                    song.setLiked(newLikedStatus);
                    songRepository.updateSongLikeStatus(song.getTitle(), newLikedStatus); // update the database

                    // Update the UI to reflect the new like status
                    if (newLikedStatus) {
                        likeButton.setImageResource(R.drawable.ic_like_filled);
                        // Show notification
                        Toast.makeText(this, R.string.song_liked, Toast.LENGTH_SHORT).show();
                    } else {
                        likeButton.setImageResource(R.drawable.ic_like_empty);
                        Toast.makeText(this, R.string.song_unliked, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        // Validate essential data
        if (songUrl == null || songUrl.isEmpty()) {
            Log.e(TAG, "No valid song URL provided");
            Toast.makeText(this, "Unable to play song - missing data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set action bar title safely
        if (actionBar != null) {
            actionBar.setTitle("Now Playing");
            if (songTitle != null) {
                actionBar.setSubtitle(songTitle);
            }
        }

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
        albumCover = findViewById(R.id.song_cover);
        playPauseButton = findViewById(R.id.btn_play_pause);
        prevButton = findViewById(R.id.btn_prev);
        nextButton = findViewById(R.id.btn_next);

        // Set UI data
        if (songTitle != null) {
            titleTextView.setText(songTitle);
        }
        if (songArtist != null) {
            artistTextView.setText(songArtist);
        }



        if (songImage != null && !songImage.isEmpty()) {

            Log.d(TAG, "songImage: " + songImage);  // Log songImage

            String imageName = songImage.replace("android.resource://com.example.group_10_melody_match/", "");
            Log.d(TAG, "imageName extracted: " + imageName);  // Log imageName

            // Get the resource ID of the image
            int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            Log.d(TAG, "imageResId: " + imageResId);  // Log imageResId



            if (imageResId != 0) {
                albumCover.setImageResource(imageResId);
            } else {
                albumCover.setImageResource(R.drawable.ic_launcher_foreground);
                Log.d(TAG, "Image not found, using default image.");

            }
        } else {
            albumCover.setImageResource(R.drawable.ic_launcher_foreground);
            Log.d(TAG, "songImage is null or empty, using default image.");

        }



        // Initialize MediaPlayer
        initializeMediaPlayer();

        // Play/Pause button logic
        if (playPauseButton != null) {
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
        }

        // Previous button logic
        if (prevButton != null) {
            prevButton.setOnClickListener(view -> {
                playPreviousSong();
            });
        }

        // Next button logic
        if (nextButton != null) {
            nextButton.setOnClickListener(view -> {
                playNextSong();
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close this activity and return to previous
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error initializing media player", e);
            Toast.makeText(this, "Unable to play this song", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Plays the previous song in the song list if available
     */
    private void playPreviousSong() {
        // Check if there's a previous song
        if (songList.isEmpty() || currentPosition <= 0) {
            Toast.makeText(this, "No previous song available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Go to previous song by decrementing position
        currentPosition--;

        try {
            // Update the current song reference
            Song previousSong = songList.get(currentPosition);

            // Update UI with new song details
            titleTextView.setText(previousSong.getTitle());
            artistTextView.setText(previousSong.getArtistName());

            // Update action bar subtitle safely
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setSubtitle(previousSong.getTitle());
            }

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
        } catch (Exception e) {
            Log.e(TAG, "Error in playPreviousSong()", e);
            Toast.makeText(this, "Error navigating to previous song", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Plays the next song in the song list if available
     */
    private void playNextSong() {
        // Check if there's a next song
        if (songList.isEmpty() || currentPosition >= songList.size() - 1) {
            Toast.makeText(this, "No next song available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Go to next song by incrementing position
        currentPosition++;

        try {
            // Update the current song reference
            Song nextSong = songList.get(currentPosition);

            // Update UI with new song details
            titleTextView.setText(nextSong.getTitle());
            artistTextView.setText(nextSong.getArtistName());

            // Update action bar subtitle safely
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setSubtitle(nextSong.getTitle());
            }

            // Reset media player for the new song
            if (mediaPlayer != null) {
                // Remember if we were playing to maintain state during transition
                boolean wasPlaying = isPlaying;
                mediaPlayer.release();

                // Set new song
                mediaPlayer = new MediaPlayer();
                try {
                    // Update song URL and prepare new media player
                    songUrl = nextSong.getResourceUrl();
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
                    Log.e(TAG, "Error playing next song", e);
                    Toast.makeText(this, "Error playing next song", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in playNextSong()", e);
            Toast.makeText(this, "Error navigating to next song", Toast.LENGTH_SHORT).show();
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

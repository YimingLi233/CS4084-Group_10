package com.example.group_10_melody_match.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group_10_melody_match.R;
import com.example.group_10_melody_match.data.database.AppDatabase;
import com.example.group_10_melody_match.data.database.entity.Song;
import com.example.group_10_melody_match.ui.adapter.SongAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SongListActivity extends AppCompatActivity {
    private static final String TAG = "SongListActivity";

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private String artistName;

    private TextView titleTextView;
    private TextView songsCountTextView;
    private FloatingActionButton fabShufflePlay;

    private List<Song> currentSongList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        // Set up action bar safely
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Initialize song list views
        titleTextView = findViewById(R.id.song_list_title);
        songsCountTextView = findViewById(R.id.songs_count);

        // Get artist name from intent
        artistName = getIntent().getStringExtra("artist_name");
        if (artistName == null || artistName.isEmpty()) {
            Log.e(TAG, "Error: No valid artist name received!");
            Toast.makeText(this, "Invalid artist name", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d(TAG, "Received artist name: " + artistName);

        // Set title as artist's name
        titleTextView.setText(artistName + "'s Songs");

        // Set action bar title safely
        if (actionBar != null) {
            actionBar.setTitle(artistName + "'s Songs");
        }

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recycler_view_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songAdapter = new SongAdapter();
        recyclerView.setAdapter(songAdapter);

        // Set up shuffle play button
        fabShufflePlay = findViewById(R.id.fab_shuffle_play);
        if (fabShufflePlay != null) {
            fabShufflePlay.setOnClickListener(v -> shufflePlay());
        }

        // Load songs for the artist
        loadSongs();
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
     * Play a random song from the artist's song list
     */
    private void shufflePlay() {
        if (currentSongList.isEmpty()) {
            Toast.makeText(this, "No songs available for this artist", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pick a random song
        Random random = new Random();
        int randomPosition = random.nextInt(currentSongList.size());
        Song randomSong = currentSongList.get(randomPosition);

        // Start the song play activity
        Intent intent = new Intent(this, SongPlayActivity.class);
        intent.putExtra("song_title", randomSong.getTitle());
        intent.putExtra("song_artist", randomSong.getArtistName());
        intent.putExtra("song_image", randomSong.getImageUrl());
        intent.putExtra("song_url", randomSong.getResourceUrl());
        intent.putExtra("current_position", randomPosition);

        // Pass all songs so we can navigate between them
        intent.putParcelableArrayListExtra("all_songs", new ArrayList<>(currentSongList));

        startActivity(intent);
    }

    private void loadSongs() {
        try {
            AppDatabase.getDatabase(this).songDao().getSongsByArtistName(artistName)
                    .observe(this, new Observer<List<Song>>() {
                        @Override
                        public void onChanged(List<Song> songs) {
                            if (songs == null || songs.isEmpty()) {
                                Log.e(TAG, "No songs found for artist: " + artistName);
                                songsCountTextView.setText("No songs available");
                                return;
                            }

                            Log.d(TAG, "Loaded songs: " + songs.size());
                            currentSongList = songs;

                            // Update song count
                            songsCountTextView.setText(songs.size() + " songs");

                            // Update adapter
                            songAdapter.setSongs(songs);
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error loading songs: " + e.getMessage());
            Toast.makeText(this, "Error loading songs", Toast.LENGTH_SHORT).show();
            songsCountTextView.setText("Error loading songs");
        }
    }
}

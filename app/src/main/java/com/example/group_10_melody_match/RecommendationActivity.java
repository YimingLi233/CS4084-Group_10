package com.example.group_10_melody_match;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer; // Import Observer
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import android.content.Intent;
import com.example.group_10_melody_match.ui.activity.MainActivity; // Import MainActivity
import com.example.group_10_melody_match.data.database.entity.Song; // Import your Song entity
import com.example.group_10_melody_match.ui.adapter.SongAdapter; // Import your SongAdapter
import com.example.group_10_melody_match.data.repository.SongRepository; // Import your SongRepository
// Remove unused imports if necessary
// import java.util.ArrayList;
import java.util.List;

public class RecommendationActivity extends AppCompatActivity {

    private static final String TAG = "RecommendationActivity"; // Add a TAG for logging
    private TextView genreTitle;
    private RecyclerView recommendationRecyclerView;
    private BottomNavigationView bottomNavigationView;
    private SongAdapter songAdapter;
    private SongRepository songRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        Log.d(TAG, "onCreate started");

        genreTitle = findViewById(R.id.genre_title);
        recommendationRecyclerView = findViewById(R.id.recommendation_recycler_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Instantiate the repository
        songRepository = new SongRepository(getApplication());
        Log.d(TAG, "SongRepository instantiated");

        // Get the selected genre from the Intent
        String selectedGenre = getIntent().getStringExtra("SELECTED_GENRE");
        if (selectedGenre != null) {
            genreTitle.setText(selectedGenre + " song list");
            Log.d(TAG, "Selected Genre: " + selectedGenre);
        } else {
            Log.w(TAG, "No genre selected, defaulting title");
            genreTitle.setText("Recommendation List"); // Default title if no genre
        }

        // Set up RecyclerView adapter
        setupRecyclerView();
        Log.d(TAG, "RecyclerView setup complete");

        // Load songs for the selected genre if a genre was passed
        if (selectedGenre != null) {
            loadRecommendedSongs(selectedGenre);
        } else {
            Log.w(TAG, "Cannot load songs: selectedGenre is null");
            // Optionally, load default songs or show a message
            // songAdapter.setSongs(new ArrayList<>()); // Clear adapter or show empty state
        }

        // Set up BottomNavigationView listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_recommendation) {
                    // Already on the recommendation screen
                    return true;
                } else if (itemId == R.id.navigation_fav_artist) {
                    // Navigate to MainActivity (Favorite Artists)
                    Intent intent = new Intent(RecommendationActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        // Set the default selected item in the bottom navigation
        bottomNavigationView.setSelectedItemId(R.id.navigation_recommendation);
        Log.d(TAG, "onCreate finished");
    }

    private void setupRecyclerView() {
        // Initialize adapter with the repository
        songAdapter = new SongAdapter(songRepository);
        recommendationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recommendationRecyclerView.setAdapter(songAdapter);
        Log.d(TAG, "SongAdapter set on RecyclerView");
    }

    // Method to load songs using the repository
    private void loadRecommendedSongs(String genreName) {
        Log.d(TAG, "Loading songs for genre: " + genreName);
        if (genreName == null || genreName.isEmpty()) {
            Log.e(TAG, "Genre name is null or empty, cannot load songs.");
            return;
        }

        // Observe the LiveData from the repository
        songRepository.getSongsByGenreName(genreName).observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                // Update the adapter with the fetched songs
                if (songs != null) {
                    Log.d(TAG, "Received " + songs.size() + " songs for genre: " + genreName);
                    songAdapter.setSongs(songs);
                } else {
                    Log.w(TAG, "Received null song list for genre: " + genreName);
                    // Optionally clear the adapter or handle the null case
                    // songAdapter.setSongs(new ArrayList<>());
                }
            }
        });
    }
}
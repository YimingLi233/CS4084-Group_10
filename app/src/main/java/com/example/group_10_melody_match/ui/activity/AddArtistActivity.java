package com.example.group_10_melody_match.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_10_melody_match.R;
import com.example.group_10_melody_match.data.database.entity.Artist;
import com.example.group_10_melody_match.data.database.entity.AvailableArtist;
import com.example.group_10_melody_match.data.repository.ArtistRepository;
import com.example.group_10_melody_match.data.repository.AvailableArtistRepository;
import com.example.group_10_melody_match.ui.adapter.AvailableArtistAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Add Artist Activity
 */
public class AddArtistActivity extends AppCompatActivity implements AvailableArtistAdapter.OnArtistAddListener {
    private AvailableArtistRepository availableArtistRepository;
    private ArtistRepository artistRepository;
    private RecyclerView recyclerView;
    private AvailableArtistAdapter adapter;
    private EditText searchEditText;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_artist);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_available_artists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize repositories
        availableArtistRepository = new AvailableArtistRepository(getApplication());
        artistRepository = new ArtistRepository(getApplication());

        // Initialize adapter with empty list
        adapter = new AvailableArtistAdapter(this, new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // Initialize search field
        searchEditText = findViewById(R.id.edit_search);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Search as user types
                searchArtists(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        // Initialize search button
        btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (!query.isEmpty()) {
                searchArtists(query);
            } else {
                loadAvailableArtists();
            }
        });

        // Load all available artists initially
        loadAvailableArtists();
    }

    /**
     * Load all available artists
     */
    private void loadAvailableArtists() {
        // Observe the LiveData from the repository
        availableArtistRepository.getAllAvailableArtists().observe(this, availableArtists -> {
            // Update the adapter with the new data
            adapter.updateData(availableArtists);
        });
    }

    /**
     * Search artists by name
     */
    private void searchArtists(String query) {
        if (query.isEmpty()) {
            // If search is empty, show all artists
            loadAvailableArtists();
        } else {
            // Observe the search results LiveData
            availableArtistRepository.searchAvailableArtists(query).observe(this, searchResults -> {
                // Update the adapter with the search results
                adapter.updateData(searchResults);
            });
        }
    }

    /**
     * Callback when an artist is added
     */
    @Override
    public void onArtistAdd(AvailableArtist availableArtist) {
        // Check if artist already exists
        if (artistRepository.artistExists(availableArtist.getName())) {
            // Show error message
            Toast.makeText(this, 
                getString(R.string.artist_already_exists, availableArtist.getName()), 
                Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert AvailableArtist to Artist
        Artist artist = new Artist(
                0, // ID will be auto-generated
                availableArtist.getName(),
                availableArtist.getGenre(),
                availableArtist.getImageUrl());

        // Add to favorites
        artistRepository.insert(artist);

        // Show notification
        Toast.makeText(this, getString(R.string.artist_added, artist.getName()), Toast.LENGTH_SHORT).show();

        // Close activity
        finish();
    }
}
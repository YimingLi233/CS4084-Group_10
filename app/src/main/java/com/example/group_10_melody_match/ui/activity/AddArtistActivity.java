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
import com.example.group_10_melody_match.data.repository.ArtistRepository;
import com.example.group_10_melody_match.ui.adapter.AvailableArtistAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Add Artist Activity
 */
public class AddArtistActivity extends AppCompatActivity implements AvailableArtistAdapter.OnArtistAddListener {
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

        // Initialize repository
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
        artistRepository.getAllArtists().observe(this, artists -> {
            // Filter out favorite artists
            List<Artist> availableArtists = new ArrayList<>();
            for (Artist artist : artists) {
                if (!artist.isFavorite()) {
                    availableArtists.add(artist);
                }
            }
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
            // Observe the LiveData from the repository
            artistRepository.getAllArtists().observe(this, artists -> {
                // Filter out favorite artists and search by name
                List<Artist> searchResults = new ArrayList<>();
                for (Artist artist : artists) {
                    if (!artist.isFavorite() && 
                        artist.getName().toLowerCase().contains(query.toLowerCase())) {
                        searchResults.add(artist);
                    }
                }
                // Update the adapter with the search results
                adapter.updateData(searchResults);
            });
        }
    }

    /**
     * Callback when an artist is added
     */
    @Override
    public void onArtistAdd(Artist artist) {
        // Check if artist is already favorite
        if (artistRepository.isArtistFavorite(artist.getName())) {
            // Show error message
            Toast.makeText(this, 
                getString(R.string.artist_already_exists, artist.getName()), 
                Toast.LENGTH_SHORT).show();
            return;
        }

        // Update as favorite
        artistRepository.updateFavoriteStatusByName(artist.getName(), true);

        // Show notification
        Toast.makeText(this, getString(R.string.artist_added, artist.getName()), Toast.LENGTH_SHORT).show();

        // Close activity
        finish();
    }
}
package com.example.group_10_melody_match.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_10_melody_match.R;
import com.example.group_10_melody_match.data.database.entity.AvailableArtist;
import com.example.group_10_melody_match.data.repository.ArtistRepository;
import com.example.group_10_melody_match.data.repository.AvailableArtistRepository;
import com.example.group_10_melody_match.ui.adapter.AvailableArtistAdapter;

import java.util.List;

/**
 * Add Artist Activity
 */
public class AddArtistActivity extends AppCompatActivity implements AvailableArtistAdapter.OnArtistAddListener {
    private AvailableArtistRepository availableArtistRepository;
    private ArtistRepository artistRepository;
    private RecyclerView recyclerView;
    private AvailableArtistAdapter adapter;
    private EditText editSearch;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_artist);

        // Initialize repositories
        availableArtistRepository = new AvailableArtistRepository(getApplication());
        artistRepository = new ArtistRepository(getApplication());

        // Initialize views
        recyclerView = findViewById(R.id.recycler_view_available_artists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        editSearch = findViewById(R.id.edit_search);
        btnSearch = findViewById(R.id.btn_search);

        // Set search button click event
        btnSearch.setOnClickListener(v -> {
            String query = editSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                searchArtists(query);
            } else {
                loadAllAvailableArtists();
            }
        });

        // Load all available artists
        loadAllAvailableArtists();
    }

    /**
     * Load all available artists
     */
    private void loadAllAvailableArtists() {
        List<AvailableArtist> availableArtists = availableArtistRepository.getAllAvailableArtists();
        if (adapter == null) {
            adapter = new AvailableArtistAdapter(this, availableArtists, this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(availableArtists);
        }
    }

    /**
     * Search artists
     */
    private void searchArtists(String query) {
        List<AvailableArtist> searchResults = availableArtistRepository.searchAvailableArtists(query);
        adapter.updateData(searchResults);
    }

    /**
     * Add artist to favorites
     */
    @Override
    public void onArtistAdd(AvailableArtist artist) {
        // Convert available artist to favorite artist and add to database
        artistRepository.insert(artist.toArtist());
        
        // Show notification
        Toast.makeText(this, R.string.artist_added, Toast.LENGTH_SHORT).show();
    }
} 
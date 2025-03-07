package com.example.group_10_melody_match.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_10_melody_match.R;
import com.example.group_10_melody_match.data.database.entity.Artist;
import com.example.group_10_melody_match.data.repository.ArtistRepository;
import com.example.group_10_melody_match.ui.adapter.ArtistAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ArtistAdapter.OnArtistRemoveListener {
    private ArtistRepository artistRepository;
    private RecyclerView recyclerView;
    private ArtistAdapter adapter;
    private FloatingActionButton fabAddArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Set window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_artists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize data repository
        artistRepository = new ArtistRepository(getApplication());

        // Initialize the adapter with an empty list
        adapter = new ArtistAdapter(this, new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // Set reset button click event
        findViewById(R.id.btn_reset).setOnClickListener(v -> resetDatabase());

        // Observe LiveData from repository
        artistRepository.getAllArtists().observe(this, artists -> {
            // Update the RecyclerView when data changes
            adapter.updateData(artists);
        });

        // Set add artist button
        fabAddArtist = findViewById(R.id.fab_add_artist);
        fabAddArtist.setOnClickListener(v -> {
            // Open add artist activity
            Intent intent = new Intent(MainActivity.this, AddArtistActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Reset database to initial state
     */
    private void resetDatabase() {
        // Delete all favorite artists
        artistRepository.deleteAllArtists();

        // Re-add initial data
        addInitialArtists();

        // Show notification
        Toast.makeText(this, R.string.data_reset, Toast.LENGTH_SHORT).show();
    }

    /**
     * Add initial artist data
     */
    private void addInitialArtists() {
        // Create initial artist list
        List<Artist> artists = new ArrayList<>();
        artists.add(new Artist(0, "Taylor Swift", "Pop", "taylor_swift"));
        artists.add(new Artist(0, "Ed Sheeran", "Pop", "ed_sheeran"));
        artists.add(new Artist(0, "Billie Eilish", "Pop/Alternative", "billie_eilish"));

        // Batch insert artist data
        artistRepository.insertAll(artists);
    }

    /**
     * Callback when an artist is removed
     */
    @Override
    public void onArtistRemove(Artist artist) {
        // Delete artist from database
        artistRepository.delete(artist);

        // Show notification
        Toast.makeText(this, R.string.artist_removed, Toast.LENGTH_SHORT).show();
    }
}
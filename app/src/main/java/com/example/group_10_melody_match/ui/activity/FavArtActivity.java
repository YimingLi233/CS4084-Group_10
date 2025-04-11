package com.example.group_10_melody_match.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_10_melody_match.R;
import com.example.group_10_melody_match.RecommendationActivity;
import com.example.group_10_melody_match.data.database.entity.Artist;
import com.example.group_10_melody_match.data.repository.ArtistRepository;
import com.example.group_10_melody_match.ui.adapter.ArtistAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FavArtActivity extends AppCompatActivity implements ArtistAdapter.OnArtistRemoveListener {
    private ArtistRepository artistRepository;
    private RecyclerView recyclerView;
    private ArtistAdapter adapter;
    private FloatingActionButton fabAddArtist;

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fav_artist);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Synchronize selected item when activity starts
        bottomNavigationView.setSelectedItemId(R.id.navigation_fav_artist);

        Log.d("FavArtActivity", "BottomNavigationView selected item: " + bottomNavigationView.getSelectedItemId());

        // Set listener for BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_recommendation) {
                // If current activity is RecommendationActivity, don't navigate
                if (!(getClass().getSimpleName().equals(RecommendationActivity.class.getSimpleName()))) {
                    // If it's not the same, go to RecommendationActivity
                    Intent intent = new Intent(FavArtActivity.this, RecommendationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);  // Bring to front
                    startActivity(intent);
                    item.setChecked(true); // Ensure the item is selected when navigating to RecommendationActivity
                }
                return true;
            } else if (item.getItemId() == R.id.navigation_fav_artist) {
                // If already on FavArtActivity, just return
                return true;
            } else {
                return false;
            }
        });


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
        artistRepository.getFavoriteArtists().observe(this, artists -> {
            // Update the RecyclerView when data changes
            adapter.updateData(artists);
        });

        // Set add artist button
        fabAddArtist = findViewById(R.id.fab_add_artist);
        fabAddArtist.setOnClickListener(v -> {
            // Open add artist activity
            Intent intent = new Intent(FavArtActivity.this, AddArtistActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure the correct item is selected when returning to FavArtActivity
        bottomNavigationView.setSelectedItemId(R.id.navigation_fav_artist);
    }

    /**
     * Reset database to initial state
     */
    private void resetDatabase() {
        // Reset database using repository
        artistRepository.resetDatabase();

        // Show notification
        Toast.makeText(this, R.string.data_reset, Toast.LENGTH_SHORT).show();
        
        // Force UI refresh after a delay to ensure database operations are complete
        recyclerView.postDelayed(() -> {
            // This will force the adapter to refresh its data
            adapter.notifyDataSetChanged();
            
            // Additionally, we can force the RecyclerView to redraw
            recyclerView.invalidate();
        }, 500); // 500ms delay
    }

    /**
     * Callback when an artist is removed
     */
    @Override
    public void onArtistRemove(Artist artist) {
        // Update as not favorite instead of deleting
        artistRepository.updateFavoriteStatusByName(artist.getName(), false);

        // Show notification
        Toast.makeText(this, R.string.artist_removed, Toast.LENGTH_SHORT).show();
    }
}
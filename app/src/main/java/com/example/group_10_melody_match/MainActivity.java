package com.example.group_10_melody_match;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_10_melody_match.data.database.AppDatabase;
import com.example.group_10_melody_match.data.database.dao.ArtistDao;
import com.example.group_10_melody_match.data.database.entity.Artist;
import com.example.group_10_melody_match.ui.adapter.ArtistAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArtistDao artistDao;
    private RecyclerView recyclerView;
    private ArtistAdapter artistAdapter; // Assume you have an adapter for your RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recycler_view_artists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        artistAdapter = new ArtistAdapter(); // Initialize your adapter
        recyclerView.setAdapter(artistAdapter);

        // Get the DAO
        artistDao = AppDatabase.getDatabase(this).artistDao();

        // Observe the LiveData from the DAO
        artistDao.getAllArtists().observe(this, new Observer<List<Artist>>() {
            @Override
            public void onChanged(List<Artist> artists) {
                // Update the RecyclerView when data changes
//                artistAdapter.setArtists(artists); // Assume you have a method to update the adapter
            }
        });
    }
}
package com.example.group_10_melody_match.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group_10_melody_match.R;
import com.example.group_10_melody_match.data.database.AppDatabase;
import com.example.group_10_melody_match.data.database.entity.Song;
import com.example.group_10_melody_match.ui.adapter.SongAdapter;
import java.util.List;

public class SongListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private String artistName;

    private TextView titleTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        // Initialize song list textview
        titleTextView = findViewById(R.id.song_list_title);

        // Get artist name from intent
        artistName = getIntent().getStringExtra("artist_name");
        if (artistName == null || artistName.isEmpty()) {
            Log.e("SongListActivity", "Error: No valid artist name received!");
            finish();
            return;
        }

        Log.d("SongListActivity", "Received artist name: " + artistName);

        // Set title as artist's name
        titleTextView.setText(artistName + "'s Songs");

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recycler_view_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songAdapter = new SongAdapter();
        recyclerView.setAdapter(songAdapter);

        // Load songs for the artist
        loadSongs();
    }


    private void loadSongs() {
        AppDatabase.getDatabase(this).songDao().getSongsByArtistName(artistName)
                .observe(this, new Observer<List<Song>>() {
                    @Override
                    public void onChanged(List<Song> songs) {
                        if (songs == null || songs.isEmpty()) {
                            Log.e("SongListActivity", "No songs found for artist: " + artistName);
                            return;
                        }
                        Log.d("SongListActivity", "Loaded songs: " + songs.size());

                        songAdapter.setSongs(songs);
                    }
                });
    }
}

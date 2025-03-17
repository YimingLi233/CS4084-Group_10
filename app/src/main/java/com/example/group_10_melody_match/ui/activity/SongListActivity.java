package com.example.group_10_melody_match.ui.activity;

import android.os.Bundle;
import android.util.Log;

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
    private String artistName; // ✅ 改成类的成员变量

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        recyclerView = findViewById(R.id.recycler_view_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songAdapter = new SongAdapter();
        recyclerView.setAdapter(songAdapter);

        // ✅ 现在 artistName 是类的成员变量
        artistName = getIntent().getStringExtra("artist_name");
        Log.d("SongListActivity", "Received artist name: " + artistName);

        if (artistName == null || artistName.isEmpty()) {
            Log.e("SongListActivity", "Error: No valid artist name received!");
            finish();
        }

        // ✅ 现在 loadSongs() 可以访问 artistName
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

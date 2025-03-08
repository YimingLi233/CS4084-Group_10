package com.example.group_10_melody_match.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_10_melody_match.R;
import com.example.group_10_melody_match.data.database.entity.Song;
import com.example.group_10_melody_match.data.repository.SongRepository;
import com.example.group_10_melody_match.ui.adapter.SongAdapter;

import java.util.List;

public class SongListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private SongRepository songRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        // ✅ 获取 Intent 传递的 artist_id
        Intent intent = getIntent();
        int artistId = intent.getIntExtra("artist_id", -1);
        String artistName = intent.getStringExtra("artist_name");

        if (artistId == -1) {
            finish(); // 没有传递有效 ID，关闭 Activity
        }

        // ✅ 初始化 RecyclerView
        recyclerView = findViewById(R.id.recycler_view_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ✅ 获取该 Artist 的所有歌曲
        songRepository = new SongRepository(getApplication());
        List<Song> songs = songRepository.getSongsByArtistId(artistId);

        // ✅ 绑定适配器
        songAdapter = new SongAdapter(this, songs);
        recyclerView.setAdapter(songAdapter);
    }
}

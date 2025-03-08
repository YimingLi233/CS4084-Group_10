package com.example.group_10_melody_match.ui.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_10_melody_match.R;
import com.example.group_10_melody_match.data.database.entity.Song;
import com.example.group_10_melody_match.data.repository.SongRepository;
import com.example.group_10_melody_match.ui.adapter.SongAdapter;

import java.util.List;
// Yiming Li
// This activity controls the song play page, shows song info and play/pause/next song behavior.
public class SongPlayActivity extends AppCompatActivity {

    private TextView artistNameView, artistGenreView;
    private RecyclerView songsRecyclerView;
    private SongAdapter songAdapter;
    private List<Song> songList;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private ImageButton btnPlayPause;
    private SongRepository songRepository;

    // This
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_play);

        // Click and jump
        Intent intent = getIntent();
        String artistName = intent.getStringExtra("artist_name");
        String artistGenre = intent.getStringExtra("artist_genre");
        // Display song name and genre
        TextView nameView = findViewById(R.id.song_title);
        TextView genreView = findViewById(R.id.song_artist);
        btnPlayPause = findViewById(R.id.btn_play_pause);

        nameView.setText(artistName);
        genreView.setText(artistGenre);

        mediaPlayer = MediaPlayer.create(this, R.raw.ding);


        // Song play logic
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    mediaPlayer.start();
                    btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                }
                isPlaying = !isPlaying;
            }
        });
    }

    // Free the resource once user is not on song play activity anymore
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

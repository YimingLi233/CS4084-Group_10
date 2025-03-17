package com.example.group_10_melody_match.ui.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.group_10_melody_match.R;
import java.io.IOException;

public class SongPlayActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ImageButton playPauseButton;
    private String songUrl;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_play);

        // 获取 Intent 传递的参数
        String songTitle = getIntent().getStringExtra("song_title");
        String songArtist = getIntent().getStringExtra("song_artist");
        String songImage = getIntent().getStringExtra("song_image");
        songUrl = getIntent().getStringExtra("song_url");

        // 绑定 UI 组件
        TextView titleTextView = findViewById(R.id.song_title);
        TextView artistTextView = findViewById(R.id.song_artist);
        ImageView albumCover = findViewById(R.id.album_cover);
        playPauseButton = findViewById(R.id.btn_play_pause);

        // 设置 UI 数据
        titleTextView.setText(songTitle);
        artistTextView.setText(songArtist);
        albumCover.setImageResource(R.drawable.ic_launcher_foreground);  // 🔥 这里可以改为实际图片加载逻辑

        // 初始化 MediaPlayer
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, android.net.Uri.parse(songUrl));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 播放 / 暂停 按钮逻辑
        playPauseButton.setOnClickListener(view -> {
            if (isPlaying) {
                mediaPlayer.pause();
                playPauseButton.setImageResource(android.R.drawable.ic_media_play);
            } else {
                mediaPlayer.start();
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
            }
            isPlaying = !isPlaying;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

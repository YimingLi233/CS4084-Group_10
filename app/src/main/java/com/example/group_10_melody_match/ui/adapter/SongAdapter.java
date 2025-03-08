package com.example.group_10_melody_match.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_10_melody_match.R;
import com.example.group_10_melody_match.data.database.entity.Song;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private Context context;
    private List<Song> songList;

    public SongAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ✅ 修正错误的 XML 文件，使用 item_song.xml 作为歌曲列表项
        View view = LayoutInflater.from(context).inflate(R.layout.activity_song_list, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.songName.setText(song.getSName());  // ✅ 确保正确显示数据库中的 sName
        holder.songArtist.setText(song.getSongArtist());  // ✅ 确保正确显示数据库中的 songArtist

        holder.itemView.setOnClickListener(v -> {
            // 这里可以写播放歌曲的逻辑
            Toast.makeText(context, "Playing: " + song.getSName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    // ✅ 新增方法：用于刷新数据
    public void updateData(List<Song> newSongs) {
        this.songList = newSongs;
        notifyDataSetChanged();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songName;
        TextView songArtist;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.song_title);  // ✅ 确保 XML 里有这个 ID
            songArtist = itemView.findViewById(R.id.song_artist);  // ✅ 确保 XML 里有这个 ID
        }
    }
}

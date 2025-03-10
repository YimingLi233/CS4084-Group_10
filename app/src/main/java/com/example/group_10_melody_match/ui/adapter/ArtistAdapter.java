package com.example.group_10_melody_match.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_10_melody_match.R;
import com.example.group_10_melody_match.data.database.AppDatabase;
import com.example.group_10_melody_match.data.database.dao.SongDao;
import com.example.group_10_melody_match.data.database.entity.Artist;
import com.example.group_10_melody_match.data.database.entity.Song;
import com.example.group_10_melody_match.ui.activity.SongListActivity;
import com.example.group_10_melody_match.ui.activity.SongPlayActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Artist list adapter
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {
    private Context context;
    private List<Artist> artistList;
    private OnArtistRemoveListener onArtistRemoveListener;
    private OnArtistClickListener onArtistClickListener; // 点击事件接口

    public interface OnArtistClickListener {
        void onArtistClick(Artist artist);
    }

    public interface OnArtistRemoveListener {
        void onArtistRemove(Artist artist);
    }

    public ArtistAdapter(Context context, List<Artist> artistList) {
        this.context = context;
        this.artistList = artistList;
    }
    
    public ArtistAdapter(Context context, List<Artist> artistList, OnArtistRemoveListener listener) {
        this.context = context;
        this.artistList = artistList;
        this.onArtistRemoveListener = listener;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_artist, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = artistList.get(position);

        holder.artistName.setText(artist.getName());
        holder.artistGenre.setText(artist.getGenre());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SongListActivity.class);
            intent.putExtra("artist_id", artist.getId());  // 传递 Artist ID
            intent.putExtra("artist_name", artist.getName());
            context.startActivity(intent);
        });

//        // 监听点击事件，触发 MainActivity 里的方法
//        holder.itemView.setOnClickListener(v -> {
//            if (onArtistClickListener != null) {
//                onArtistClickListener.onArtistClick(artist);
//            }
//        });
//
//        // 加载艺术家图片
//        int resourceId = context.getResources().getIdentifier(
//                artist.getImageUrl(), "drawable", context.getPackageName());
//        if (resourceId != 0) {
//            holder.artistImage.setImageResource(resourceId);
//        } else {
//            holder.artistImage.setImageResource(R.drawable.default_artist);
//        }
//
//        // 点击跳转，并获取数据库中的歌曲
//        holder.itemView.setOnClickListener(v -> {
//            new Thread(() -> {
//                AppDatabase db = AppDatabase.getDatabase(context);
//                SongDao songDao = db.songDao();
//
//                // 获取该艺术家的所有歌曲
//                List<Song> songs = songDao.getAllSongs(); // 获取所有歌曲
//                List<Song> artistSongs = new ArrayList<>();
//
//                for (Song song : songs) {
//                    if (song.getSongArtist().equals(artist.getName())) {
//                        artistSongs.add(song);
//                    }
//                }
//
//                // 传递数据给下一个 Activity
//                Intent intent = new Intent(context, SongPlayActivity.class);
//                intent.putExtra("artist_name", artist.getName());
//                intent.putExtra("artist_genre", artist.getGenre());
//
//                // 传递歌曲列表
//                intent.putParcelableArrayListExtra("songs", new ArrayList<>(artistSongs));
//
//                context.startActivity(intent);
//            }).start(); // 在子线程中查询数据库
//        });

        // 删除按钮监听
        if (holder.btnRemove != null) {
            holder.btnRemove.setOnClickListener(v -> {
                if (onArtistRemoveListener != null) {
                    onArtistRemoveListener.onArtistRemove(artist);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }
    
    /**
     * Update data
     */
    public void updateData(List<Artist> newList) {
        this.artistList = newList;
        notifyDataSetChanged();
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        ImageView artistImage;
        TextView artistName;
        TextView artistGenre;
        ImageButton btnRemove;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image);
            artistName = itemView.findViewById(R.id.artist_name);
            artistGenre = itemView.findViewById(R.id.artist_genre);
            btnRemove = itemView.findViewById(R.id.btn_remove_artist);
        }
    }
} 
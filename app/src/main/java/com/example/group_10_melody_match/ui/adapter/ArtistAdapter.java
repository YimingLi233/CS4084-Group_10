package com.example.group_10_melody_match.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_10_melody_match.R;
import com.example.group_10_melody_match.data.database.entity.Artist;
import com.example.group_10_melody_match.ui.activity.SongListActivity;

import java.util.List;

/**
 * Artist list adapter
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {
    private Context context;
    private List<Artist> artistList;
    private OnArtistRemoveListener onArtistRemoveListener;

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

// Load artist image (temporary static assignment for Taylor Swift)
        holder.artistImage.setImageResource(R.drawable.taylor_swift);

// Click event to SongListActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SongListActivity.class);
            intent.putExtra("artist_id", artist.getId());
            intent.putExtra("artist_name", artist.getName());
            context.startActivity(intent);
            Log.d("ArtistAdapter", "Artist: " + artist.getName());
        });

// --- Keeping their commented version below for reference ---

//        // 点击跳转，并获取数据库中的歌曲
//        holder.itemView.setOnClickListener(v -> {
//            new Thread(() -> {
//                AppDatabase db = AppDatabase.getDatabase(context);
//                SongDao songDao = db.songDao();
//                List<Song> songs = songDao.getAllSongs();
//                List<Song> artistSongs = new ArrayList<>();
//                for (Song song : songs) {
//                    if (song.getSongArtist().equals(artist.getName())) {
//                        artistSongs.add(song);
//                    }
//                }
//                Intent intent = new Intent(context, SongPlayActivity.class);
//                intent.putExtra("artist_name", artist.getName());
//                intent.putExtra("artist_genre", artist.getGenre());
//                intent.putParcelableArrayListExtra("songs", new ArrayList<>(artistSongs));
//                context.startActivity(intent);
//            }).start();
//        });

        if (holder.btnRemove != null) {
            holder.btnRemove.setOnClickListener(v -> {
                if (onArtistRemoveListener != null) {
                    onArtistRemoveListener.onArtistRemove(artist);
                }
            });
        }

        // Click artist jump into their song list
        holder.itemView.setOnClickListener(view -> {
            Log.d("ArtistAdapter", "Clicked Artist: " + artist.getName());

            if (artist.getName() == null || artist.getName().isEmpty()) {
                Log.e("ArtistAdapter", "Error: artist name is NULL or EMPTY");
                return; // 避免崩溃
            }
            Intent intent = new Intent(view.getContext(), SongListActivity.class);
            intent.putExtra("artist_name", artist.getName());  // pass name
            view.getContext().startActivity(intent);
        });


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
package com.example.group_10_melody_match.ui.adapter;

import android.content.Context;
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
        
        // Set artist image
        int resourceId = context.getResources().getIdentifier(
                artist.getImageUrl(), "drawable", context.getPackageName());
        if (resourceId != 0) {
            holder.artistImage.setImageResource(resourceId);
        } else {
            // If image not found, use default image
            holder.artistImage.setImageResource(R.drawable.default_artist);
        }
        
        // Set remove button click event
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
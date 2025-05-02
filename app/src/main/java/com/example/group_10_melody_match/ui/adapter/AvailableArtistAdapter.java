package com.example.group_10_melody_match.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_10_melody_match.R;
import com.example.group_10_melody_match.data.database.entity.Artist;

import java.util.List;

/**
 * Available artist list adapter
 */
public class AvailableArtistAdapter extends RecyclerView.Adapter<AvailableArtistAdapter.AvailableArtistViewHolder> {
    private Context context;
    private List<Artist> availableArtistList;
    private OnArtistAddListener onArtistAddListener;

    public interface OnArtistAddListener {
        void onArtistAdd(Artist artist);
    }

    public AvailableArtistAdapter(Context context, List<Artist> availableArtistList, OnArtistAddListener listener) {
        this.context = context;
        this.availableArtistList = availableArtistList;
        this.onArtistAddListener = listener;
    }

    @NonNull
    @Override
    public AvailableArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_available_artist, parent, false);
        return new AvailableArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableArtistViewHolder holder, int position) {
        Artist artist = availableArtistList.get(position);
        
        holder.artistName.setText(artist.getName());
        holder.artistGenre.setText(artist.getGenre());
        
        String artistName = artist.getName();
        if (artistName != null && !artistName.isEmpty()) {
            String resourceName = "artist_" + artistName.toLowerCase().replace(" ", "_");
            Log.d("AvailableArtistAdapter", "Trying to load image: " + resourceName);

            int resourceId = context.getResources().getIdentifier(
                    resourceName, "drawable", context.getPackageName());
                    
            if (resourceId != 0) {
                holder.artistImage.setImageResource(resourceId);
            } else {
                Log.w("AvailableArtistAdapter", "Image not found for: " + resourceName + ", using default.");
                holder.artistImage.setImageResource(R.drawable.default_artist);
            }
        } else {
            Log.w("AvailableArtistAdapter", "Artist name is null or empty, using default image.");
            holder.artistImage.setImageResource(R.drawable.default_artist);
        }
        
        // Set add button click event
        holder.btnAdd.setOnClickListener(v -> {
            if (onArtistAddListener != null) {
                onArtistAddListener.onArtistAdd(artist);
            }
        });
    }

    @Override
    public int getItemCount() {
        return availableArtistList.size();
    }
    
    /**
     * Update data
     */
    public void updateData(List<Artist> newList) {
        this.availableArtistList = newList;
        notifyDataSetChanged();
    }

    public static class AvailableArtistViewHolder extends RecyclerView.ViewHolder {
        ImageView artistImage;
        TextView artistName;
        TextView artistGenre;
        Button btnAdd;

        public AvailableArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.available_artist_image);
            artistName = itemView.findViewById(R.id.available_artist_name);
            artistGenre = itemView.findViewById(R.id.available_artist_genre);
            btnAdd = itemView.findViewById(R.id.btn_add_artist);
        }
    }
} 
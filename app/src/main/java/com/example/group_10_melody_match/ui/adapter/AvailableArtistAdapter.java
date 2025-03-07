package com.example.group_10_melody_match.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_10_melody_match.R;
import com.example.group_10_melody_match.data.database.entity.AvailableArtist;

import java.util.List;

/**
 * Available artist list adapter
 */
public class AvailableArtistAdapter extends RecyclerView.Adapter<AvailableArtistAdapter.AvailableArtistViewHolder> {
    private Context context;
    private List<AvailableArtist> availableArtistList;
    private OnArtistAddListener onArtistAddListener;

    public interface OnArtistAddListener {
        void onArtistAdd(AvailableArtist artist);
    }

    public AvailableArtistAdapter(Context context, List<AvailableArtist> availableArtistList, OnArtistAddListener listener) {
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
        AvailableArtist artist = availableArtistList.get(position);
        
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
    public void updateData(List<AvailableArtist> newList) {
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
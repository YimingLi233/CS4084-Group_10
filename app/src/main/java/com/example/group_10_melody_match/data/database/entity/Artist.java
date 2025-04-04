package com.example.group_10_melody_match.data.database.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Data model class representing an artist
 */
@Entity(tableName = "artists")
public class Artist {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String imageUrl;
    private boolean isFavorite;
    
    @Ignore
    private String genre; // Temporary field for backward compatibility

    public Artist(int id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.isFavorite = false;  // Default to false
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
    
    // Temporary methods for backward compatibility
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
} 
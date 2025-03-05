package com.example.group_10_melody_match.data.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Data model class representing an artist
 */
@Entity(tableName = "artists")
public class Artist {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String genre;
    private String imageUrl;

    public Artist(int id, String name, String genre, String imageUrl) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.imageUrl = imageUrl;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
} 
package com.example.group_10_melody_match.data.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;

/**
 * Junction table for Artist and Genre many-to-many relationship
 */
@Entity(tableName = "artist_genre",
        primaryKeys = {"artistId", "genreId"},
        foreignKeys = {
            @ForeignKey(entity = Artist.class,
                        parentColumns = "id",
                        childColumns = "artistId",
                        onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Genre.class,
                        parentColumns = "id",
                        childColumns = "genreId",
                        onDelete = ForeignKey.CASCADE)
        })
public class ArtistGenre {
    private int artistId;
    private int genreId;
    
    public ArtistGenre(int artistId, int genreId) {
        this.artistId = artistId;
        this.genreId = genreId;
    }
    
    public int getArtistId() {
        return artistId;
    }
    
    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }
    
    public int getGenreId() {
        return genreId;
    }
    
    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }
} 
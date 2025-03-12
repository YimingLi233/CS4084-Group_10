package com.example.group_10_melody_match.data.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.group_10_melody_match.data.database.entity.Artist;
import com.example.group_10_melody_match.data.database.entity.ArtistGenre;
import com.example.group_10_melody_match.data.database.entity.Genre;

import java.util.List;

/**
 * Data class that represents an Artist with its associated Genres
 */
public class ArtistWithGenres {
    @Embedded
    public Artist artist;
    
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = @Junction(
            value = ArtistGenre.class,
            parentColumn = "artistId",
            entityColumn = "genreId"
        )
    )
    public List<Genre> genres;
} 
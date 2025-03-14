package com.example.group_10_melody_match.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.group_10_melody_match.data.database.entity.ArtistGenre;
import com.example.group_10_melody_match.data.database.entity.Genre;

import java.util.List;

/**
 * ArtistGenre Data Access Object interface
 */
@Dao
public interface ArtistGenreDao {
    /**
     * Insert a single artist-genre relation
     */
    @Insert
    void insert(ArtistGenre artistGenre);
    
    /**
     * Insert multiple artist-genre relations
     */
    @Insert
    void insertAll(List<ArtistGenre> artistGenres);
    
    /**
     * Get all artist-genre relations for a specific artist
     */
    @Query("SELECT * FROM artist_genre WHERE artistId = :artistId")
    List<ArtistGenre> getGenreRelationsForArtist(int artistId);
    
    /**
     * Get all genres for a specific artist
     */
    @Query("SELECT g.* FROM genres g INNER JOIN artist_genre ag ON g.id = ag.genreId WHERE ag.artistId = :artistId")
    LiveData<List<Genre>> getGenresByArtistId(int artistId);
    
    /**
     * Get all genres for a specific artist (non-LiveData version)
     */
    @Query("SELECT g.* FROM genres g INNER JOIN artist_genre ag ON g.id = ag.genreId WHERE ag.artistId = :artistId")
    List<Genre> getGenresByArtistIdSync(int artistId);
    
    /**
     * Get genre names as comma-separated string for an artist
     */
    @Query("SELECT IFNULL(GROUP_CONCAT(g.name, ', '), '') FROM genres g INNER JOIN artist_genre ag ON g.id = ag.genreId WHERE ag.artistId = :artistId")
    String getGenreNamesForArtist(int artistId);
    
    /**
     * Count genres for an artist (for debugging)
     */
    @Query("SELECT COUNT(*) FROM artist_genre WHERE artistId = :artistId")
    int countGenresForArtist(int artistId);
    
    /**
     * Delete all artist-genre relations
     */
    @Query("DELETE FROM artist_genre")
    void deleteAll();
} 
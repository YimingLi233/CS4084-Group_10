package com.example.group_10_melody_match.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.group_10_melody_match.data.database.entity.Artist;

import java.util.List;

/**
 * Artist Data Access Object interface
 */
@Dao
public interface ArtistDao {

    /**
     * Insert a single artist
     */
    @Insert
    void insert(Artist artist);

    /**
     * Insert multiple artists
     */
    @Insert
    void insertAll(List<Artist> artists);

    /**
     * Get all artists, sorted by name
     */
    @Query("SELECT * FROM artists ORDER BY name ASC")
    LiveData<List<Artist>> getAllArtists();

    /**
     * Delete a single artist
     */
    @Delete
    void delete(Artist artist);

    /**
     * Delete all artists
     */
    @Query("DELETE FROM artists")
    void deleteAll();

    /**
     * Check if artist exists by name
     */
    @Query("SELECT COUNT(*) FROM artists WHERE name = :name")
    int getArtistCountByName(String name);

    /**
     * Check if artist is favorite by name
     */
    @Query("SELECT COUNT(*) FROM artists WHERE name = :name AND isFavorite = 1")
    int getFavoriteCountByName(String name);

    /**
     * Update favorite status by artist name
     */
    @Query("UPDATE artists SET isFavorite = :isFavorite WHERE name = :name")
    void updateFavoriteStatusByName(String name, boolean isFavorite);

    /**
     * Get favorite artists
     */
    @Query("SELECT * FROM artists WHERE isFavorite = 1 ORDER BY name ASC")
    LiveData<List<Artist>> getFavoriteArtists();
}
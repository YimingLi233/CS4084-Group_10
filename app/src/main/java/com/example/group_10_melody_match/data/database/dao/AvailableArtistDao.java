package com.example.group_10_melody_match.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.group_10_melody_match.data.database.entity.AvailableArtist;

import java.util.List;

/**
 * Available Artist Data Access Object interface
 */
@Dao
public interface AvailableArtistDao {

    /**
     * Insert a single available artist
     */
    @Insert
    void insert(AvailableArtist artist);

    /**
     * Insert multiple available artists
     */
    @Insert
    void insertAll(List<AvailableArtist> artists);

    /**
     * Get all available artists, sorted by name
     */
    @Query("SELECT * FROM available_artists ORDER BY name ASC")
    LiveData<List<AvailableArtist>> getAllAvailableArtists();

    /**
     * Search available artists by name
     */
    @Query("SELECT * FROM available_artists WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    LiveData<List<AvailableArtist>> searchAvailableArtists(String query);

    /**
     * Delete all available artists
     */
    @Query("DELETE FROM available_artists")
    void deleteAll();
}
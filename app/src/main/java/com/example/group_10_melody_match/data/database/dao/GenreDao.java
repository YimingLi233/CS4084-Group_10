package com.example.group_10_melody_match.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.group_10_melody_match.data.database.entity.Genre;

import java.util.List;

/**
 * Genre Data Access Object interface
 */
@Dao
public interface GenreDao {
    /**
     * Insert a single genre
     */
    @Insert
    long insert(Genre genre);
    
    /**
     * Insert multiple genres
     */
    @Insert
    List<Long> insertAll(List<Genre> genres);
    
    /**
     * Get all genres, sorted by name
     */
    @Query("SELECT * FROM genres ORDER BY name ASC")
    LiveData<List<Genre>> getAllGenres();
    
    /**
     * Get genre by ID
     */
    @Query("SELECT * FROM genres WHERE id = :genreId")
    Genre getGenreById(int genreId);
    
    /**
     * Get genre by name
     */
    @Query("SELECT * FROM genres WHERE name = :name LIMIT 1")
    Genre getGenreByName(String name);
    
    /**
     * Delete all genres
     */
    @Query("DELETE FROM genres")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM genres")
    int getCountSync();

} 
package com.example.group_10_melody_match.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import com.example.group_10_melody_match.data.database.entity.Song;

@Dao
public interface SongDao {
    @Insert
    void insertAll(List<Song> songs);

    @Query("SELECT * FROM songs WHERE artistName = :artistName")
    LiveData<List<Song>> getSongsByArtistName(String artistName);

    @Query("SELECT * FROM songs WHERE title = :title LIMIT 1")
    LiveData<Song> getSongByTitle(String title); // Query by title to identify the song

    // New Query: Get songs based on genre name by joining tables
    @Query("SELECT s.* FROM songs s " +
            "INNER JOIN artists a ON s.artistName = a.name " +
            "INNER JOIN artist_genre ag ON a.id = ag.artistId " +
            "INNER JOIN genres g ON ag.genreId = g.id " +
            "WHERE g.name = :genreName")
    LiveData<List<Song>> getSongsByGenreName(String genreName);

    @Query("UPDATE songs SET isLiked = :isLiked WHERE title = :title")
    void updateSongLikeStatus(String title, boolean isLiked); // Update the like status of the song

    @Query("DELETE FROM songs")
    void deleteAll();
}

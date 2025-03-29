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

    @Query("UPDATE songs SET isLiked = :isLiked WHERE title = :title")
    void updateSongLikeStatus(String title, boolean isLiked); // Update the like status of the song

    @Query("DELETE FROM songs")
    void deleteAll();
}

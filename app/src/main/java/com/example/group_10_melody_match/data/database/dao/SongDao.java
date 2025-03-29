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

    @Query("DELETE FROM songs")
    void deleteAll();
}

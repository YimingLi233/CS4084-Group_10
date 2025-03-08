package com.example.group_10_melody_match.data.database.dao;
import static android.icu.text.MessagePattern.ArgType.SELECT;

import  androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.group_10_melody_match.data.database.entity.Artist;
import com.example.group_10_melody_match.data.database.entity.Song;

import java.util.List;

// Yiming Li
@Dao
public interface SongDao {

    @Insert
    void insert(Song song);

    @Insert
    void insertAll(List<Song> songs);

    @Query("SELECT * FROM songs ORDER BY sName ASC")
    List<Song> getAllSongs();

    @Query("SELECT * FROM songs WHERE sId = :artistId")
    List<Song> getSongsByArtistId(int artistId);

    @Delete
    void delete(Song song);

    @Query("DELETE FROM songs")
    void deleteAll();


}

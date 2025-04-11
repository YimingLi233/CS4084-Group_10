package com.example.group_10_melody_match.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.group_10_melody_match.data.database.entity.Song;
import com.example.group_10_melody_match.data.database.dao.SongDao;
import com.example.group_10_melody_match.data.database.AppDatabase;

import java.util.List;

public class SongRepository {
    private SongDao songDao;
    private LiveData<List<Song>> allSongs;

    public SongRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        songDao = db.songDao();
        allSongs = songDao.getSongsByArtistName("default"); // Fetch default artist or any other query
    }

    // Method to get a song by title
    public LiveData<Song> getSongByTitle(String title) {
        return songDao.getSongByTitle(title); // This will automatically run in a background thread if you use LiveData
    }

    // Method to get songs by genre name
    public LiveData<List<Song>> getSongsByGenreName(String genreName) {
        return songDao.getSongsByGenreName(genreName);
    }

    // Method to update the like status of a song
    public void updateSongLikeStatus(String title, boolean isLiked) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            songDao.updateSongLikeStatus(title, isLiked); // Update the "isLiked" field in the database
        });
    }

    // Method to get all songs (can be customized as needed)
    public LiveData<List<Song>> getAllSongs() {
        return allSongs;
    }
}

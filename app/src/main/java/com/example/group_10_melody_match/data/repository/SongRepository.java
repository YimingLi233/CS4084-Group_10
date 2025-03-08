package com.example.group_10_melody_match.data.repository;

import android.app.Application;

import com.example.group_10_melody_match.data.database.AppDatabase;
import com.example.group_10_melody_match.data.database.dao.SongDao;
import com.example.group_10_melody_match.data.database.entity.Song;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Song data repository class
 */
public class SongRepository {
    private SongDao songDao;
    private List<Song> allSongs;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Constructor
     */
    public SongRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        songDao = db.songDao();
        // Fetch all songs initially (not recommended on the main thread)
        allSongs = songDao.getAllSongs();
    }

    /**
     * Get all songs
     */
    public List<Song> getAllSongs() {
        return songDao.getAllSongs();
    }

    public void insert(Song song) {
        executorService.execute(() -> {
            songDao.insert(song);
            allSongs = songDao.getAllSongs();
        });
    }

    public void insertAll(List<Song> songs) {
        executorService.execute(() -> {
            songDao.insertAll(songs);
            allSongs = songDao.getAllSongs();
        });
    }

    public void delete(Song song) {
        executorService.execute(() -> {
            songDao.delete(song);
            allSongs = songDao.getAllSongs();
        });
    }

    public void deleteAllSongs() {
        executorService.execute(() -> {
            songDao.deleteAll();
            allSongs = songDao.getAllSongs();
        });
    }

    public List<Song> getSongsByArtistId(int artistId) {
        return songDao.getSongsByArtistId(artistId);
    }
}
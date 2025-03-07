package com.example.group_10_melody_match.data.repository;

import android.app.Application;

import com.example.group_10_melody_match.data.database.AppDatabase;
import com.example.group_10_melody_match.data.database.dao.ArtistDao;
import com.example.group_10_melody_match.data.database.entity.Artist;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Artist data repository class
 */
public class ArtistRepository {
    private ArtistDao artistDao;
    private List<Artist> allArtists;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Constructor
     */
    public ArtistRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        artistDao = db.artistDao();
        // Executing queries on the main thread may cause ANR, but for simplicity, we'll do it this way for now
        allArtists = artistDao.getAllArtists();
    }

    /**
     * Get all artists
     */
    public List<Artist> getAllArtists() {
        // Always get the latest data from the database
        return artistDao.getAllArtists();
    }


    public void insert(Artist artist) {
        executorService.execute(() -> {
            artistDao.insert(artist);
            allArtists = artistDao.getAllArtists();
        });
    }
    

    public void insertAll(List<Artist> artists) {
        executorService.execute(() -> {
            artistDao.insertAll(artists);
            allArtists = artistDao.getAllArtists();
        });
    }
    

    public void delete(Artist artist) {
        executorService.execute(() -> {
            artistDao.delete(artist);
            allArtists = artistDao.getAllArtists();
        });
    }
    

    public void deleteAllArtists() {
        executorService.execute(() -> {
            artistDao.deleteAll();
            // Update cache
            allArtists = artistDao.getAllArtists();
        });
    }
} 
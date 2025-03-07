package com.example.group_10_melody_match.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

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
    private LiveData<List<Artist>> allArtists;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Constructor
     */
    public ArtistRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        artistDao = db.artistDao();
        // Get LiveData from DAO
        allArtists = artistDao.getAllArtists();
    }

    /**
     * Get all artists as LiveData
     */
    public LiveData<List<Artist>> getAllArtists() {
        return allArtists;
    }

    /**
     * Insert a single artist
     */
    public void insert(Artist artist) {
        executorService.execute(() -> {
            artistDao.insert(artist);
        });
    }

    /**
     * Insert multiple artists
     */
    public void insertAll(List<Artist> artists) {
        executorService.execute(() -> {
            artistDao.insertAll(artists);
        });
    }

    /**
     * Delete a single artist
     */
    public void delete(Artist artist) {
        executorService.execute(() -> {
            artistDao.delete(artist);
        });
    }

    /**
     * Delete all artists
     */
    public void deleteAllArtists() {
        executorService.execute(() -> {
            artistDao.deleteAll();
        });
    }
}
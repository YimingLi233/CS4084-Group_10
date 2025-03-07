package com.example.group_10_melody_match.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.group_10_melody_match.data.database.AppDatabase;
import com.example.group_10_melody_match.data.database.dao.AvailableArtistDao;
import com.example.group_10_melody_match.data.database.entity.AvailableArtist;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Available Artist data repository class
 */
public class AvailableArtistRepository {
    private AvailableArtistDao availableArtistDao;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Constructor
     */
    public AvailableArtistRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        availableArtistDao = db.availableArtistDao();
    }

    /**
     * Get all available artists as LiveData
     */
    public LiveData<List<AvailableArtist>> getAllAvailableArtists() {
        return availableArtistDao.getAllAvailableArtists();
    }

    /**
     * Search available artists as LiveData
     */
    public LiveData<List<AvailableArtist>> searchAvailableArtists(String query) {
        return availableArtistDao.searchAvailableArtists(query);
    }
}
package com.example.group_10_melody_match.data.repository;

import android.app.Application;

import com.example.group_10_melody_match.data.database.AppDatabase;
import com.example.group_10_melody_match.data.database.dao.AvailableArtistDao;
import com.example.group_10_melody_match.data.database.entity.AvailableArtist;

import java.util.List;

/**
 * Available Artist data repository class
 */
public class AvailableArtistRepository {
    private AvailableArtistDao availableArtistDao;

    /**
     * Constructor
     */
    public AvailableArtistRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        availableArtistDao = db.availableArtistDao();
    }

    /**
     * Get all available artists
     */
    public List<AvailableArtist> getAllAvailableArtists() {
        return availableArtistDao.getAllAvailableArtists();
    }

    /**
     * Search available artists
     */
    public List<AvailableArtist> searchAvailableArtists(String query) {
        return availableArtistDao.searchAvailableArtists(query);
    }
} 
package com.example.group_10_melody_match.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.group_10_melody_match.data.database.AppDatabase;
import com.example.group_10_melody_match.data.database.dao.ArtistDao;
import com.example.group_10_melody_match.data.database.entity.Artist;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Artist data repository class
 */
public class ArtistRepository {
    private ArtistDao artistDao;
    private LiveData<List<Artist>> allArtists;
    private LiveData<List<Artist>> favoriteArtists;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Application application;

    /**
     * Constructor
     */
    public ArtistRepository(Application application) {
        this.application = application;
        AppDatabase db = AppDatabase.getDatabase(application);
        artistDao = db.artistDao();
        // Get LiveData from DAO
        allArtists = artistDao.getAllArtists();
        favoriteArtists = artistDao.getFavoriteArtists();
    }

    /**
     * Get all artists as LiveData
     */
    public LiveData<List<Artist>> getAllArtists() {
        return allArtists;
    }

    /**
     * Get favorite artists as LiveData
     */
    public LiveData<List<Artist>> getFavoriteArtists() {
        return favoriteArtists;
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

    /**
     * Check if artist exists
     */
    public boolean artistExists(String name) {
        // Using a simple way to get the result synchronously
        try {
            return executorService.submit(() -> 
                artistDao.getArtistCountByName(name) > 0
            ).get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if artist is favorite
     */
    public boolean isArtistFavorite(String name) {
        // Using a simple way to get the result synchronously
        try {
            return executorService.submit(() -> 
                artistDao.getFavoriteCountByName(name) > 0
            ).get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update favorite status by artist name
     */
    public void updateFavoriteStatusByName(String name, boolean isFavorite) {
        executorService.execute(() -> {
            artistDao.updateFavoriteStatusByName(name, isFavorite);
        });
    }

    /**
     * Reset database to initial state
     */
    public void resetDatabase() {
        executorService.execute(() -> {
            // Clear existing data
            artistDao.deleteAll();

            // Create initial artist list
            List<Artist> artists = new ArrayList<>();
            artists.add(new Artist(0, "Taylor Swift", "Pop", "taylor_swift"));
            artists.add(new Artist(0, "Ed Sheeran", "Pop", "ed_sheeran"));
            artists.add(new Artist(0, "Billie Eilish", "Pop/Alternative", "billie_eilish"));
            artists.add(new Artist(0, "The Weeknd", "R&B/Pop", "the_weeknd"));
            artists.add(new Artist(0, "BTS", "K-Pop", "bts"));
            artists.add(new Artist(0, "Ariana Grande", "Pop", "ariana_grande"));
            artists.add(new Artist(0, "Drake", "Hip-Hop/Rap", "drake"));
            artists.add(new Artist(0, "Dua Lipa", "Pop", "dua_lipa"));
            artists.add(new Artist(0, "Justin Bieber", "Pop", "justin_bieber"));
            artists.add(new Artist(0, "Lady Gaga", "Pop", "lady_gaga"));
            artists.add(new Artist(0, "Beyoncé", "R&B/Pop", "beyonce"));
            artists.add(new Artist(0, "Coldplay", "Alternative/Rock", "coldplay"));

            // Insert initial data
            artistDao.insertAll(artists);

            // Set initial favorites
            artistDao.updateFavoriteStatusByName("Taylor Swift", true);
            artistDao.updateFavoriteStatusByName("Ed Sheeran", true);
            artistDao.updateFavoriteStatusByName("Billie Eilish", true);
        });
    }
}
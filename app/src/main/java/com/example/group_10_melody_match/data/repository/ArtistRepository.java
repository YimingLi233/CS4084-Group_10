package com.example.group_10_melody_match.data.repository;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.group_10_melody_match.data.database.AppDatabase;
import com.example.group_10_melody_match.data.database.dao.ArtistDao;
import com.example.group_10_melody_match.data.database.dao.ArtistGenreDao;
import com.example.group_10_melody_match.data.database.entity.Artist;
import com.example.group_10_melody_match.data.database.entity.ArtistGenre;
import com.example.group_10_melody_match.data.database.entity.Genre;
import com.example.group_10_melody_match.data.model.ArtistWithGenres;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository for Artist-related operations
 */
public class ArtistRepository {
    private ArtistDao artistDao;
    private ArtistGenreDao artistGenreDao;
    private AppDatabase database;

    public ArtistRepository(Application application) {
        database = AppDatabase.getDatabase(application);
        artistDao = database.artistDao();
        artistGenreDao = database.artistGenreDao();
    }

    /**
     * Get all artists with genre information
     */
    public LiveData<List<Artist>> getAllArtists() {
        // Get all artists
        LiveData<List<Artist>> allArtists = artistDao.getAllArtists();
        
        // Pre-load genre information for all artists in a background thread
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            List<Artist> currentArtists = artistDao.getAllArtistsSync();
            if (currentArtists != null) {
                for (Artist artist : currentArtists) {
                    try {
                        // First check if artist has any genres
                        int genreCount = artistGenreDao.countGenresForArtist(artist.getId());
                        if (genreCount > 0) {
                            String genreString = artistGenreDao.getGenreNamesForArtist(artist.getId());
                            Log.d("ArtistRepository", "Preloaded genre for " + artist.getName() + ": " + genreString);
                        }
                    } catch (Exception e) {
                        Log.e("ArtistRepository", "Error preloading genres for artist: " + artist.getName(), e);
                    }
                }
            }
        });
        
        // Transform to add genre information
        return Transformations.map(allArtists, artists -> {
            if (artists != null) {
                List<Artist> result = new ArrayList<>();
                
                for (Artist artist : artists) {
                    // Create a copy of the artist to avoid modifying the original
                    Artist artistCopy = new Artist(artist.getId(), artist.getName(), artist.getImageUrl());
                    artistCopy.setFavorite(artist.isFavorite());
                    
                    // Get genre information synchronously but with a timeout
                    String genreString = "";
                    try {
                        // We need to run this in a separate thread with a timeout
                        Thread genreThread = new Thread(() -> {
                            try {
                                int genreCount = artistGenreDao.countGenresForArtist(artist.getId());
                                if (genreCount > 0) {
                                    String genres = artistGenreDao.getGenreNamesForArtist(artist.getId());
                                    if (genres != null && !genres.isEmpty()) {
                                        artistCopy.setGenre(genres);
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("ArtistRepository", "Error getting genres in thread: " + e.getMessage());
                            }
                        });
                        
                        genreThread.start();
                        genreThread.join(500); // Wait up to 500ms for the thread to complete
                        
                        // If the thread is still running after timeout, we'll use empty genre
                        if (genreThread.isAlive()) {
                            Log.w("ArtistRepository", "Genre loading timed out for: " + artist.getName());
                            genreThread.interrupt();
                        }
                    } catch (Exception e) {
                        Log.e("ArtistRepository", "Error getting genres for artist: " + artist.getName(), e);
                    }
                    
                    result.add(artistCopy);
                }
                
                return result;
            }
            return artists;
        });
    }
    
    /**
     * Get favorite artists with genre information
     */
    public LiveData<List<Artist>> getFavoriteArtists() {
        // Get favorite artists
        LiveData<List<Artist>> favoriteArtists = artistDao.getFavoriteArtists();
        
        // Pre-load genre information for favorite artists in a background thread
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            List<Artist> currentFavorites = artistDao.getFavoriteArtistsSync();
            if (currentFavorites != null) {
                for (Artist artist : currentFavorites) {
                    try {
                        // First check if artist has any genres
                        int genreCount = artistGenreDao.countGenresForArtist(artist.getId());
                        if (genreCount > 0) {
                            String genreString = artistGenreDao.getGenreNamesForArtist(artist.getId());
                            Log.d("ArtistRepository", "Preloaded genre for favorite " + artist.getName() + ": " + genreString);
                        }
                    } catch (Exception e) {
                        Log.e("ArtistRepository", "Error preloading genres for favorite artist: " + artist.getName(), e);
                    }
                }
            }
        });
        
        // Transform to add genre information
        return Transformations.map(favoriteArtists, artists -> {
            if (artists != null) {
                List<Artist> result = new ArrayList<>();
                
                for (Artist artist : artists) {
                    // Create a copy of the artist to avoid modifying the original
                    Artist artistCopy = new Artist(artist.getId(), artist.getName(), artist.getImageUrl());
                    artistCopy.setFavorite(artist.isFavorite());
                    
                    // Get genre information synchronously but with a timeout
                    String genreString = "";
                    try {
                        // We need to run this in a separate thread with a timeout
                        Thread genreThread = new Thread(() -> {
                            try {
                                int genreCount = artistGenreDao.countGenresForArtist(artist.getId());
                                if (genreCount > 0) {
                                    String genres = artistGenreDao.getGenreNamesForArtist(artist.getId());
                                    if (genres != null && !genres.isEmpty()) {
                                        artistCopy.setGenre(genres);
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("ArtistRepository", "Error getting genres in thread: " + e.getMessage());
                            }
                        });
                        
                        genreThread.start();
                        genreThread.join(500); // Wait up to 500ms for the thread to complete
                        
                        // If the thread is still running after timeout, we'll use empty genre
                        if (genreThread.isAlive()) {
                            Log.w("ArtistRepository", "Genre loading timed out for: " + artist.getName());
                            genreThread.interrupt();
                        }
                    } catch (Exception e) {
                        Log.e("ArtistRepository", "Error getting genres for artist: " + artist.getName(), e);
                    }
                    
                    result.add(artistCopy);
                }
                
                return result;
            }
            return artists;
        });
    }
    
    /**
     * Update artist favorite status
     */
    public void updateFavoriteStatus(String artistName, boolean isFavorite) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            artistDao.updateFavoriteStatusByName(artistName, isFavorite);
        });
    }
    
    /**
     * Get artist by ID with genre information
     */
    public Artist getArtistById(int id) {
        Artist artist = artistDao.getArtistById(id);
        if (artist != null) {
            // We need to set a default empty genre first
            artist.setGenre("");
            
            // Then update it in the background
            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                try {
                    // First check if artist has any genres
                    int genreCount = artistGenreDao.countGenresForArtist(artist.getId());
                    Log.d("ArtistRepository", "Artist ID: " + artist.getId() + ", Name: " + artist.getName() + ", Genre Count: " + genreCount);
                    
                    if (genreCount > 0) {
                        String genreString = artistGenreDao.getGenreNamesForArtist(artist.getId());
                        Log.d("ArtistRepository", "Genre String: " + (genreString != null ? genreString : "null"));
                        artist.setGenre(genreString != null ? genreString : "");
                    } else {
                        Log.d("ArtistRepository", "No genres found for artist: " + artist.getName());
                    }
                } catch (Exception e) {
                    Log.e("ArtistRepository", "Error getting genres for artist ID: " + artist.getId(), e);
                }
            });
        }
        return artist;
    }
    
    /**
     * Get artist by name with genre information
     */
    public Artist getArtistByName(String name) {
        Artist artist = artistDao.getArtistByName(name);
        if (artist != null) {
            // We need to set a default empty genre first
            artist.setGenre("");
            
            // Then update it in the background
            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                try {
                    // First check if artist has any genres
                    int genreCount = artistGenreDao.countGenresForArtist(artist.getId());
                    Log.d("ArtistRepository", "Artist ID: " + artist.getId() + ", Name: " + artist.getName() + ", Genre Count: " + genreCount);
                    
                    if (genreCount > 0) {
                        String genreString = artistGenreDao.getGenreNamesForArtist(artist.getId());
                        Log.d("ArtistRepository", "Genre String: " + (genreString != null ? genreString : "null"));
                        artist.setGenre(genreString != null ? genreString : "");
                    } else {
                        Log.d("ArtistRepository", "No genres found for artist: " + artist.getName());
                    }
                } catch (Exception e) {
                    Log.e("ArtistRepository", "Error getting genres for artist name: " + name, e);
                }
            });
        }
        return artist;
    }
    
    /**
     * Get all genres for a specific artist
     */
    public LiveData<List<Genre>> getGenresByArtistId(int artistId) {
        return artistGenreDao.getGenresByArtistId(artistId);
    }
    
    /**
     * Add a genre to an artist
     */
    public void addGenreToArtist(int artistId, int genreId) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            ArtistGenre relation = new ArtistGenre(artistId, genreId);
            artistGenreDao.insert(relation);
        });
    }
    
    /**
     * Add multiple genres to an artist
     */
    public void addGenresToArtist(int artistId, List<Integer> genreIds) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            for (Integer genreId : genreIds) {
                ArtistGenre relation = new ArtistGenre(artistId, genreId);
                artistGenreDao.insert(relation);
            }
        });
    }
    
    /**
     * Reset database to initial state
     */
    public void resetDatabase() {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            try {
                // Initialize the database
                AppDatabase.initializeDatabase(database);
                
                // After reset, we need to ensure the UI gets refreshed with genre information
                // Wait a moment for the database operations to complete
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                // Force a refresh of the favorite artists by making a dummy update
                // This will trigger the LiveData observers to refresh
                Artist dummyArtist = artistDao.getArtistByName("Taylor Swift");
                if (dummyArtist != null) {
                    // Toggle favorite status and then toggle it back
                    boolean currentStatus = dummyArtist.isFavorite();
                    artistDao.updateFavoriteStatusByName("Taylor Swift", !currentStatus);
                    
                    // Wait a moment
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    // Set it back to the original status
                    artistDao.updateFavoriteStatusByName("Taylor Swift", currentStatus);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // If initialization fails, at least try to initialize artists and genres
                try {
                    AppDatabase.initializeGenres(database);
                    // Wait a moment to ensure genres are initialized
                    Thread.sleep(100);
                    database.artistDao().deleteAll();
                    AppDatabase.initializeArtists(database);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Check if artist exists
     */
    public boolean artistExists(String name) {
        try {
            return artistDao.getArtistCountByName(name) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if artist is favorite
     */
    public boolean isArtistFavorite(String name) {
        try {
            return artistDao.getFavoriteCountByName(name) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update favorite status by artist name
     */
    public void updateFavoriteStatusByName(String name, boolean isFavorite) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            artistDao.updateFavoriteStatusByName(name, isFavorite);
        });
    }
}
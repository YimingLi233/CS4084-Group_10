package com.example.group_10_melody_match.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.group_10_melody_match.data.database.AppDatabase;
import com.example.group_10_melody_match.data.database.dao.GenreDao;
import com.example.group_10_melody_match.data.database.entity.Genre;

import java.util.List;

/**
 * Repository for Genre-related operations
 */
public class GenreRepository {
    private GenreDao genreDao;
    private AppDatabase database;
    private LiveData<List<Genre>> allGenres;

    public GenreRepository(Application application) {
        database = AppDatabase.getDatabase(application);
        genreDao = database.genreDao();
        allGenres = genreDao.getAllGenres();
    }

    /**
     * Get all genres
     */
    public LiveData<List<Genre>> getAllGenres() {
        return allGenres;
    }

    /**
     * Get genre by ID
     */
    public Genre getGenreById(int id) {
        return genreDao.getGenreById(id);
    }

    /**
     * Get genre by name
     */
    public Genre getGenreByName(String name) {
        return genreDao.getGenreByName(name);
    }

    /**
     * Insert a single genre
     */
    public void insert(Genre genre) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            genreDao.insert(genre);
        });
    }

    /**
     * Insert multiple genres
     */
    public void insertAll(List<Genre> genres) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            genreDao.insertAll(genres);
        });
    }

    /**
     * Delete all genres
     */
    public void deleteAll() {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            genreDao.deleteAll();
        });
    }
    
    /**
     * Initialize genres with default data
     */
    public void initializeGenres() {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            AppDatabase.initializeGenres(database);
        });
    }
} 
package com.example.group_10_melody_match.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.lifecycle.LiveData;

import com.example.group_10_melody_match.data.database.dao.ArtistDao;
import com.example.group_10_melody_match.data.database.dao.AvailableArtistDao;
import com.example.group_10_melody_match.data.database.entity.Artist;
import com.example.group_10_melody_match.data.database.entity.AvailableArtist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Room Database class
 */
@Database(entities = { Artist.class, AvailableArtist.class }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Singleton pattern
    private static volatile AppDatabase INSTANCE;

    // Database name
    private static final String DATABASE_NAME = "group_10_melody_match_db";

    // Thread pool for background operations
    private static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    // Get DAOs
    public abstract ArtistDao artistDao();

    public abstract AvailableArtistDao availableArtistDao();

    /**
     * Get database instance (Singleton pattern)
     */
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()// Don't delete this. It has to do with database.
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Get database write executor
     */
    public static ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }

    /**
     * Database creation callback, used to add initial data
     */
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Add initial data in a new thread
            databaseWriteExecutor.execute(() -> {
                // Initialize all data
                initializeDatabase(INSTANCE);
            });
        }
    };

    /**
     * Initialize the entire database
     */
    public static void initializeDatabase(AppDatabase db) {
        initializeArtists(db);
        initializeAvailableArtists(db);
        // Add initialization methods for new entities here
    }

    /**
     * Initialize artist data
     */
    private static void initializeArtists(AppDatabase db) {
        ArtistDao artistDao = db.artistDao();

        // Clear existing data
        artistDao.deleteAll();

        // Create initial artist list
        List<Artist> artists = new ArrayList<>();
        artists.add(new Artist(0, "Taylor Swift", "Pop", "taylor_swift"));
        artists.add(new Artist(0, "Ed Sheeran", "Pop", "ed_sheeran"));
        artists.add(new Artist(0, "Billie Eilish", "Pop/Alternative", "billie_eilish"));

        // Insert initial data
        artistDao.insertAll(artists);
    }

    /**
     * Initialize available artist data
     */
    private static void initializeAvailableArtists(AppDatabase db) {
        AvailableArtistDao availableArtistDao = db.availableArtistDao();

        // Clear existing data
        availableArtistDao.deleteAll();

        // Create initial available artist list
        List<AvailableArtist> availableArtists = new ArrayList<>();
        availableArtists.add(new AvailableArtist(0, "Taylor Swift", "Pop", "taylor_swift"));
        availableArtists.add(new AvailableArtist(0, "Ed Sheeran", "Pop", "ed_sheeran"));
        availableArtists.add(new AvailableArtist(0, "Billie Eilish", "Pop/Alternative", "billie_eilish"));
        availableArtists.add(new AvailableArtist(0, "The Weeknd", "R&B/Pop", "the_weeknd"));
        availableArtists.add(new AvailableArtist(0, "BTS", "K-Pop", "bts"));
        availableArtists.add(new AvailableArtist(0, "Ariana Grande", "Pop", "ariana_grande"));
        availableArtists.add(new AvailableArtist(0, "Drake", "Hip-Hop/Rap", "drake"));
        availableArtists.add(new AvailableArtist(0, "Dua Lipa", "Pop", "dua_lipa"));
        availableArtists.add(new AvailableArtist(0, "Justin Bieber", "Pop", "justin_bieber"));
        availableArtists.add(new AvailableArtist(0, "Lady Gaga", "Pop", "lady_gaga"));
        availableArtists.add(new AvailableArtist(0, "Beyoncé", "R&B/Pop", "beyonce"));
        availableArtists.add(new AvailableArtist(0, "Coldplay", "Alternative/Rock", "coldplay"));

        // Insert initial data
        availableArtistDao.insertAll(availableArtists);
    }
}
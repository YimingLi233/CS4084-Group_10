package com.example.group_10_melody_match.data.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.lifecycle.LiveData;

import com.example.group_10_melody_match.data.database.dao.ArtistDao;
import com.example.group_10_melody_match.data.database.dao.GenreDao;
import com.example.group_10_melody_match.data.database.dao.ArtistGenreDao;
import com.example.group_10_melody_match.data.database.dao.SongDao;
import com.example.group_10_melody_match.data.database.entity.Artist;
import com.example.group_10_melody_match.data.database.entity.Genre;
import com.example.group_10_melody_match.data.database.entity.ArtistGenre;
import com.example.group_10_melody_match.data.database.entity.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Room Database class
 */
@Database(entities = { Artist.class, Genre.class, ArtistGenre.class, Song.class }, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Singleton pattern
    private static volatile AppDatabase INSTANCE;

    // Database name
    private static final String DATABASE_NAME = "group_10_melody_match_db";

    // Thread pool for background operations
    private static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    // Get DAOs
    public abstract ArtistDao artistDao();
    public abstract GenreDao genreDao();
    public abstract ArtistGenreDao artistGenreDao();
    public abstract SongDao songDao();

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
            Log.d("Database", "✅ Room database created!");


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
        try {

            Log.d("DatabaseInit", "✅ Initializing Database...");

            // Initialize genres first, as artist-genre relationships depend on genres
            initializeGenres(db);
            
            // Wait a moment to ensure genres are initialized
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // Then initialize artists
            initializeArtists(db);
            
            // Wait a moment to ensure artists are initialized
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Then initialize songs
            initializeSongs(db);

            // Wait a moment to ensure songs are initialized
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // Finally initialize artist-genre relationships
            initializeArtistGenreRelations(db);
            
            // Verify the initialization
            verifyArtistGenreRelations(db);

            Log.d("DatabaseInit", "✅ Database Initialized Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DatabaseInit", "❌ Database Initialization Failed", e);

        }
    }

    /**
     * Initialize genre data
     */
    public static void initializeGenres(AppDatabase db) {
        GenreDao genreDao = db.genreDao();

        // Clear existing data
        genreDao.deleteAll();

        // Create initial genre list
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(0, "Pop"));
        genres.add(new Genre(0, "Rock"));
        genres.add(new Genre(0, "Hip-Hop"));
        genres.add(new Genre(0, "R&B"));
        genres.add(new Genre(0, "K-Pop"));
        genres.add(new Genre(0, "Alternative"));
        genres.add(new Genre(0, "Country"));
        genres.add(new Genre(0, "Electronic"));

        // Insert initial data
        genreDao.insertAll(genres);
    }

    /**
     * Initialize artist data
     */
    public static void initializeArtists(AppDatabase db) {
        ArtistDao artistDao = db.artistDao();

        // Clear existing data
        artistDao.deleteAll();

        // Create initial artist list
        List<Artist> artists = new ArrayList<>();
        artists.add(new Artist(0, "Taylor Swift", "taylor_swift"));
        artists.add(new Artist(0, "Ed Sheeran", "ed_sheeran"));
        artists.add(new Artist(0, "Billie Eilish", "billie_eilish"));
        artists.add(new Artist(0, "The Weeknd", "the_weeknd"));
        artists.add(new Artist(0, "BTS", "bts"));
        artists.add(new Artist(0, "Ariana Grande", "ariana_grande"));
        artists.add(new Artist(0, "Drake", "drake"));
        artists.add(new Artist(0, "Dua Lipa", "dua_lipa"));
        artists.add(new Artist(0, "Justin Bieber", "justin_bieber"));
        artists.add(new Artist(0, "Lady Gaga", "lady_gaga"));

        // Insert initial data
        artistDao.insertAll(artists);

        // Set initial favorites
        artistDao.updateFavoriteStatusByName("Taylor Swift", true);
        artistDao.updateFavoriteStatusByName("Ed Sheeran", true);
        artistDao.updateFavoriteStatusByName("Billie Eilish", true);
    }

    /**
     * Initialize artist-genre relations
     */
    public static void initializeArtistGenreRelations(AppDatabase db) {
        try {
            ArtistGenreDao artistGenreDao = db.artistGenreDao();
            
            // Clear existing relations
            artistGenreDao.deleteAll();
            Log.d("AppDatabase", "Cleared existing artist-genre relations");
            
            // Get artists by name
            Artist taylorSwift = db.artistDao().getArtistByName("Taylor Swift");
            Artist edSheeran = db.artistDao().getArtistByName("Ed Sheeran");
            Artist billieEilish = db.artistDao().getArtistByName("Billie Eilish");
            Artist theWeeknd = db.artistDao().getArtistByName("The Weeknd");
            Artist bts = db.artistDao().getArtistByName("BTS");
            Artist arianaGrande = db.artistDao().getArtistByName("Ariana Grande");
            Artist drake = db.artistDao().getArtistByName("Drake");
            Artist duaLipa = db.artistDao().getArtistByName("Dua Lipa");
            Artist justinBieber = db.artistDao().getArtistByName("Justin Bieber");
            Artist ladyGaga = db.artistDao().getArtistByName("Lady Gaga");
            
            // Get genres by name
            Genre pop = db.genreDao().getGenreByName("Pop");
            Genre rock = db.genreDao().getGenreByName("Rock");
            Genre hipHop = db.genreDao().getGenreByName("Hip-Hop");
            Genre rnb = db.genreDao().getGenreByName("R&B");
            Genre kpop = db.genreDao().getGenreByName("K-Pop");
            Genre alternative = db.genreDao().getGenreByName("Alternative");
            Genre country = db.genreDao().getGenreByName("Country");
            
            // Create relations
            List<ArtistGenre> relations = new ArrayList<>();
            
            // Check if artist and genre exist before adding relationship
            if (taylorSwift != null && pop != null) {
                relations.add(new ArtistGenre(taylorSwift.getId(), pop.getId()));
            }
            if (taylorSwift != null && country != null) {
                relations.add(new ArtistGenre(taylorSwift.getId(), country.getId()));
            }
            
            if (edSheeran != null && pop != null) {
                relations.add(new ArtistGenre(edSheeran.getId(), pop.getId()));
            }
            
            if (billieEilish != null && pop != null) {
                relations.add(new ArtistGenre(billieEilish.getId(), pop.getId()));
            }
            if (billieEilish != null && alternative != null) {
                relations.add(new ArtistGenre(billieEilish.getId(), alternative.getId()));
            }
            
            if (theWeeknd != null && rnb != null) {
                relations.add(new ArtistGenre(theWeeknd.getId(), rnb.getId()));
            }
            if (theWeeknd != null && pop != null) {
                relations.add(new ArtistGenre(theWeeknd.getId(), pop.getId()));
            }
            
            if (bts != null && kpop != null) {
                relations.add(new ArtistGenre(bts.getId(), kpop.getId()));
            }
            
            if (arianaGrande != null && pop != null) {
                relations.add(new ArtistGenre(arianaGrande.getId(), pop.getId()));
            }
            
            if (drake != null && hipHop != null) {
                relations.add(new ArtistGenre(drake.getId(), hipHop.getId()));
            }
            
            if (duaLipa != null && pop != null) {
                relations.add(new ArtistGenre(duaLipa.getId(), pop.getId()));
            }
            
            if (justinBieber != null && pop != null) {
                relations.add(new ArtistGenre(justinBieber.getId(), pop.getId()));
            }
            
            if (ladyGaga != null && pop != null) {
                relations.add(new ArtistGenre(ladyGaga.getId(), pop.getId()));
            }
            
            // Only insert if relations list is not empty
            if (!relations.isEmpty()) {
                artistGenreDao.insertAll(relations);
                Log.d("AppDatabase", "Inserted " + relations.size() + " artist-genre relations");
            } else {
                Log.e("AppDatabase", "No artist-genre relations to insert!");
            }
        } catch (Exception e) {
            Log.e("AppDatabase", "Error initializing artist-genre relations", e);
            e.printStackTrace();
        }
    }

    public static void initializeSongs(AppDatabase db) {
        Log.d("DatabaseInit", "Initializing Songs Table...");

        SongDao songDao = db.songDao();
        songDao.deleteAll();

        List<Song> songs = new ArrayList<>();
        songs.add(new Song(0, "Love Story", "Taylor Swift", "love_story_image"));
        songs.add(new Song(0, "You Belong With Me", "Taylor Swift", "you_belong_with_me_image"));

        songDao.insertAll(songs);

        Log.d("DatabaseInit", "Inserted Songs: " + songs.size() + " songs into database");
    }



    /**
     * Verify artist-genre relations after initialization
     */
    private static void verifyArtistGenreRelations(AppDatabase db) {
        // This method is called from initializeDatabase which is already running in a background thread
        // So we don't need to create another thread here
        try {
            // Check a few artists to make sure they have genres
            Artist taylorSwift = db.artistDao().getArtistByName("Taylor Swift");
            if (taylorSwift != null) {
                int count = db.artistGenreDao().countGenresForArtist(taylorSwift.getId());
                Log.d("AppDatabase", "Taylor Swift has " + count + " genres");
            }
            
            Artist edSheeran = db.artistDao().getArtistByName("Ed Sheeran");
            if (edSheeran != null) {
                int count = db.artistGenreDao().countGenresForArtist(edSheeran.getId());
                Log.d("AppDatabase", "Ed Sheeran has " + count + " genres");
            }
            
            // Count total relations
            int totalRelations = 0;
            List<Artist> allArtists = db.artistDao().getAllArtistsSync();
            if (allArtists != null) {
                for (Artist artist : allArtists) {
                    totalRelations += db.artistGenreDao().countGenresForArtist(artist.getId());
                }
            }
            Log.d("AppDatabase", "Total artist-genre relations: " + totalRelations);
            
        } catch (Exception e) {
            Log.e("AppDatabase", "Error verifying artist-genre relations", e);
        }
    }
}
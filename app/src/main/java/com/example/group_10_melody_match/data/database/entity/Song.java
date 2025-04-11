package com.example.group_10_melody_match.data.database.entity;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a song in the database
 * 
 * This class implements Parcelable to enable:
 * 1. Passing lists of Song objects between activities using Intent
 * 2. Supporting song navigation features (previous/next song)
 * 3. Maintaining song context when switching between songs
 * 
 * Parcelable is preferred over Serializable for Android because:
 * - It's significantly faster (up to 10x)
 * - It generates less garbage collection overhead
 * - It's specifically designed for Android's IPC mechanisms
 */
@Entity(tableName = "songs")
public class Song implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String artistName;
    private String imageUrl;
    private String resourceUrl;

    private boolean isLiked;  // New field for like status

    public Song(int id, String title, String artistName, String imageUrl, String resourceUrl, boolean isLiked) {
        this.id = id;
        this.title = title;
        this.artistName = artistName;
        this.imageUrl = imageUrl;
        this.resourceUrl = resourceUrl;
        this.isLiked = isLiked;
    }

    /**
     * Constructor for recreating object from a Parcel
     * 
     * Called by the Parcelable.Creator to recreate the object
     * Order of reading must match order of writing in writeToParcel
     */
    protected Song(Parcel in) {
        id = in.readInt();
        title = in.readString();
        artistName = in.readString();
        imageUrl = in.readString();
        resourceUrl = in.readString();
        isLiked = in.readByte() != 0;  // Convert byte to boolean
    }

    /**
     * Creator object that creates instances of Song from a Parcel
     * 
     * This is required for Parcelable implementation and used by Android's
     * framework when unparceling Song objects from an Intent
     */
    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    // Getter and Setter for 'isLiked'
    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    /**
     * Special flags for Parcelable, generally 0 unless file descriptors are used
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write object fields to the Parcel
     * 
     * This method serializes the Song object for transport between activities
     * Order of writing must match order of reading in the constructor
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(artistName);
        dest.writeString(imageUrl);
        dest.writeString(resourceUrl);
        dest.writeByte((byte) (isLiked ? 1 : 0));  // Save 'isLiked' as byte (1 or 0)
    }
}
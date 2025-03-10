package com.example.group_10_melody_match.data.database.entity;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Songs class by Yiming Li
@Entity(tableName = "songs")
public class Song implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int sId;
    private String sName;
    private int artistId;
    private int resourceID;

    public Song(int sId, String sName, int artistId, int resourceID) {
        this.sId = sId;
        this.sName = sName;
        this.artistId = artistId;
        this.resourceID = resourceID;
    }

    protected Song(Parcel in) {
        sId = in.readInt();
        sName = in.readString();
        artistId = in.readInt();
        resourceID = in.readInt();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sId);
        dest.writeString(sName);
        dest.writeInt(artistId);
        dest.writeInt(resourceID);
    }
    public int getSId() {
        return sId;
    }

    public void setSId(int sId) {
        this.sId = sId;
    }

    public String getSName() {
        return sName;
    }

    public void setSName(String sName) {
        this.sName = sName;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public int getResourceID() {
        return resourceID;
    }


    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    @Override
    public String toString() {
        return "Songs{" +
                "sId=" + sId +
                ", sName='" + sName + '\'' +
                ", artistId=" + artistId +
                ", resourceID=" + resourceID +
                '}';
    }
}

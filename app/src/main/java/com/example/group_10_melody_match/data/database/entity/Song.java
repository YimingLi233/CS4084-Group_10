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
    private String songArtist;
    private int resourceID;

    public Song(int sId, String sName, String songArtist, int resourceID) {
        this.sId = sId;
        this.sName = sName;
        this.songArtist = songArtist;
        this.resourceID = resourceID;
    }

    protected Song(Parcel in) {
        sId = in.readInt();
        sName = in.readString();
        songArtist = in.readString();
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
        dest.writeString(songArtist);
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

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
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
                ", songArtist=" + songArtist +
                ", resourceID=" + resourceID +
                '}';
    }
}

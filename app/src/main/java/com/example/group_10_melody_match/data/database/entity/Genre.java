package com.example.group_10_melody_match.data.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Data model class representing a music genre
 */
@Entity(tableName = "genres")
public class Genre {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    
    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
} 
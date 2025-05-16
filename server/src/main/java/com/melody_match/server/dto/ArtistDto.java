package com.melody_match.server.dto;

public class ArtistDto {
    private int id;
    private String name;
    private String imageUrl;
    private String genre;

    // Default constructor
    public ArtistDto() {
    }

    // Constructor with all fields
    public ArtistDto(int id, String name, String imageUrl, String genre) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.genre = genre;
    }

    // Getters and Setters
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
} 
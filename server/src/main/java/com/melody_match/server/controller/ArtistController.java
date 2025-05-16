package com.melody_match.server.controller;

import com.melody_match.server.dto.ArtistDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @GetMapping
    public List<ArtistDto> getAllArtists() {
        List<ArtistDto> artists = new ArrayList<>();

        // Hardcoded data, ensure imageUrl matches potential Android drawable names
        artists.add(new ArtistDto(1, "Taylor Swift", "artist_taylor_swift", "Pop"));
        artists.add(new ArtistDto(2, "Ed Sheeran", "artist_ed_sheeran", "Pop"));
        artists.add(new ArtistDto(3, "Billie Eilish", "artist_billie_eilish", "Alternative"));
        artists.add(new ArtistDto(4, "The Weeknd", "artist_the_weeknd", "R&B"));
        artists.add(new ArtistDto(5, "BTS", "artist_bts", "K-Pop"));
        artists.add(new ArtistDto(6, "Ariana Grande", "artist_ariana_grande", "Pop"));
        artists.add(new ArtistDto(7, "Drake", "artist_drake", "Hip Hop"));
        artists.add(new ArtistDto(8, "Dua Lipa", "artist_dua_lipa", "Pop"));
        artists.add(new ArtistDto(9, "Justin Bieber", "artist_justin_bieber", "Pop"));
        artists.add(new ArtistDto(10, "Lady Gaga", "artist_lady_gaga", "Pop"));

        return artists;
    }
} 
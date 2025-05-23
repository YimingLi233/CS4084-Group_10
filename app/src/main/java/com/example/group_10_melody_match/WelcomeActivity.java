package com.example.group_10_melody_match;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.group_10_melody_match.data.database.AppDatabase;
import com.example.group_10_melody_match.data.database.entity.Genre;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private Spinner genreSpinner;
    private Button startJourneyButton;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        genreSpinner = findViewById(R.id.genre_spinner);
        startJourneyButton = findViewById(R.id.start_journey_button);

        // Get database instance
        db = AppDatabase.getDatabase(getApplicationContext());

        // Load genres from database
        loadGenresFromDatabase();

        // Set OnClickListener for the button
        startJourneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object selectedItem = genreSpinner.getSelectedItem();
                if (selectedItem == null) {
                    // This should never happen, but just in case
                    android.util.Log.e("WelcomeActivity", "❌ genreSpinner.getSelectedItem() is null");
                    return;
                }
                String selectedGenre = selectedItem.toString();

                Intent intent = new Intent(WelcomeActivity.this, RecommendationActivity.class);
                intent.putExtra("SELECTED_GENRE", selectedGenre);
                startActivity(intent);
            }
        });
    }

    private void loadGenresFromDatabase() {
        // Observe genre data from the database
        db.genreDao().getAllGenres().observe(this, new Observer<List<Genre>>() {
            @Override
            public void onChanged(List<Genre> genres) {
                if (genres != null && !genres.isEmpty()) {
                    // Convert Genre objects to genre names for the spinner
                    List<String> genreNames = new ArrayList<>();
                    for (Genre genre : genres) {
                        genreNames.add(genre.getName());
                    }

                    // Create and set adapter for spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            WelcomeActivity.this,
                            android.R.layout.simple_spinner_item,
                            genreNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    genreSpinner.setAdapter(adapter);
                    Log.d("WelcomeActivity", "✅ Spinner loaded with genres, first item: " + genreSpinner.getSelectedItem());

                    // Set "Pop" as the default selection if it exists
                    int popIndex = genreNames.indexOf("Pop");
                    if (popIndex >= 0) {
                        genreSpinner.setSelection(popIndex);
                    }
                }

            }
        });
    }
}
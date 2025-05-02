# 🎵 Melody Match

**Melody Match** is an elegant and interactive Android music recommendation app that helps users explore and manage their favorite music genres and artists. Built with **Java**, **Room Database**, and **Material Components**, the app allows for seamless user experience across multiple screens with persistent data storage.


## ✨ Features

- 🎨 **Feature 1: Favorite Artist List Display**  
  Displays a list of favorite artists with visual cards.

- 🔍 **Feature 2: Search Artist When Adding to Favorite List**  
  Search artists by name and add them to the favorites list.

- 🧾 **Feature 3: Available Artists Display**  
  Shows a full list of available artists to choose from.

- ▶️ **Feature 4: Song Play**  
  Play selected songs from the list.

- 🗑 **Feature 5: Delete Favorite Artist from List**  
  Remove an artist from favorites with one tap.

- ⏮⏯⏭ **Feature 6: Prev/Next Button in Song Play Page**  
  Song player page includes previous, play/pause, and next buttons.

- 🌊 **Feature 7: App Entrance Page**  
  Welcome page with genre selection before entering the app.

- 🧭 **Feature 8: Bottom Route Page**  
  Bottom navigation bar for seamless screen switching.

- 🎵 **Feature 9: Recommendation Page**  
  Displays recommended songs based on selected genre.

- 💜 **Feature 10: Mark LIKE on Song Item**  
  Tap the heart icon to like or unlike a song.

- 🔀 **Feature 11: Shuffle Play on Song List Page**  
  Shuffle play button enables random playback from the song list.

- ➕ **Feature 12: Add Favorite Artist to List**  
  Add an artist to your favorite list using the "Add" button.

## 🏗 Architecture

- **MVVM Pattern**  
  Clean separation of concerns using View (Activity), ViewModel (Repository), and Model (Room Entities & DAOs).

- **Room Database**  
  Handles storage of songs, artists, genres, and relationships between them.

- **LiveData Observers**  
  UI automatically reacts to database updates without manual refreshes.

## 📁 Project Structure

```
group_10_melody_match/
├── ui/
│   ├── activity/                # Activities like RecommendationActivity, FavArtActivity
│   └── adapter/                # RecyclerView adapters for songs and artists
├── data/
│   ├── database/
│   │   ├── dao/                # Room DAO interfaces
│   │   ├── entity/             # Room entities: Song, Artist, Genre, etc.
│   │   └── AppDatabase.java
│   └── repository/             # Repository classes for abstracted data operations
├── res/
│   ├── layout/                 # All XML layouts including activity and item views
│   ├── drawable/               # Backgrounds, icons, and shapes
│   ├── menu/                   # Bottom navigation menu
│   └── values/                 # Colors, strings, themes
```

## 🚀 Getting Started

### Prerequisites

- Android Studio Bumblebee or newer
- Android device or emulator (API level 24+)
- Java 8 or above


### Installation

1. Clone this repo:
   ```bash
   git clone https://github.com/YimingLi233/CS4084-Group_10.git
   ```

2. Open in Android Studio and let Gradle sync.

3. Run the app on emulator or device.

## 📸 Screenshots

| Welcome Page | Recommendation List | Favorite Artists |
|--------------|---------------------|------------------|
| ![Welcome](https://github.com/user-attachments/assets/0f8788ee-c2eb-4b14-90d4-1339be8a3265) | ![Songs](https://github.com/user-attachments/assets/669dd4f0-1cd2-48fe-bde3-b6fc4eca6b51) | ![Fav](https://github.com/user-attachments/assets/36bbe082-7c0b-4f32-8e5d-78cc2c59610b) |



## 📝 License

This project is licensed under the MIT License.


## 👨‍💻 Authors

This project was developed as part of a group assignment for CS4084.

- **Yiming Li** — 23039248
- **Ping Lin** — 23086505
- **Yuexi Chen** — 23005505
- **Stephen O'Brien** — 0444464

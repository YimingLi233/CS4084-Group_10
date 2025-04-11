# 🎵 Melody Match

**Melody Match** is an elegant and interactive Android music recommendation app that helps users explore and manage their favorite music genres and artists. Built with **Java**, **Room Database**, and **Material Components**, the app allows for seamless user experience across multiple screens with persistent data storage.


## ✨ Features

- 🎧 **Genre-Based Music Recommendation**  
  Users can choose a genre (e.g., Pop, Classical) and view a curated list of songs from that genre.

- 💜 **Favorite Artists Management**  
  Easily mark artists as favorites and manage them through a visually appealing library interface.

- 🌈 **Interactive Like Button**  
  Songs can be liked or unliked, and the like status is synced with the database in real-time.

- 🌊 **Beautiful Welcome Page**  
  A themed welcome screen with background image and centered layout elements, including spinner selection and styled buttons.

- 🔄 **Bottom Navigation Bar**  
  Persistent bottom navigation across activities, with proper state synchronization and animated selection indicator.

- ➕ **Floating Action Button (FAB)**  
  Add new favorite artists quickly via a "+" button, positioned conveniently on the top-right corner for easy access.

- 🧹 **Database Reset (dev only)**  
  Reset the database to its initial state with a single tap (currently commented out for production).

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
   git clone https://github.com/yourusername/melody-match.git
   ```

2. Open in Android Studio and let Gradle sync.

3. Run the app on emulator or device.

## 📸 Screenshots

| Welcome Page | Recommendation List | Favorite Artists |
|--------------|---------------------|------------------|
| ![Welcome](https://github.com/user-attachments/assets/ed37ee63-6fea-4513-ad01-e59143ceae80) | ![Songs](https://github.com/user-attachments/assets/75ac296c-cea6-4511-a4c9-b0b14bc044ec) | ![Fav](https://github.com/user-attachments/assets/cbd7a08d-b09b-485c-9a67-885f50f2e4a1) |



## 📝 License

This project is licensed under the MIT License.

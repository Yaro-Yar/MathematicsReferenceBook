package com.example.mathematics_reference_book.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface TopicDao {

    @Query("UPDATE topics SET is_favorite = :isFavorite WHERE id = :id")
    void updateFavoriteStatus(int id, boolean isFavorite);

    @Query("SELECT * FROM topics")
    LiveData<List<TopicEntity>> getAllTopics();

    @Query("UPDATE topics SET user_notes = :notes WHERE id = :id")
    void updateUserNotes(int id, String notes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTopics(List<TopicEntity> topics);


    @Query("SELECT * FROM topics WHERE is_favorite = 1") // Исправлено на is_favorite
    LiveData<List<TopicEntity>> getFavoriteTopics();

    @Query("SELECT * FROM topics WHERE title LIKE :query OR description LIKE :query OR category LIKE :query COLLATE NOCASE")
    LiveData<List<TopicEntity>> searchTopics(String query);
}
package com.example.mathematics_reference_book.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.Executor;

public class TopicRepository {
    private final TopicDao topicDao;
    private final Executor executor;

    public TopicRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        this.topicDao = database.topicDao();
        this.executor = AppDatabase.databaseWriteExecutor;
    }

    // Метод для обновления заметок
    public void updateUserNotes(int topicId, String notes) {
        executor.execute(() -> {
            topicDao.updateUserNotes(topicId, notes);
        });
    }

    // Метод для получения всех тем
    public LiveData<List<TopicEntity>> getAllTopics() {
        return topicDao.getAllTopics();
    }

    // Метод для поиска тем
    public LiveData<List<TopicEntity>> searchTopics(String query) {
        return topicDao.searchTopics("%" + query + "%");
    }

    // Метод для обновления статуса избранного
    public void updateFavoriteStatus(int id, boolean isFavorite) {
        executor.execute(() -> {
            topicDao.updateFavoriteStatus(id, isFavorite);
        });
    }

    // Метод для вставки новых тем
    public void insertTopics(List<TopicEntity> topics) {
        executor.execute(() -> {
            topicDao.insertTopics(topics);
        });
    }
}

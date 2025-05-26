package com.example.mathematics_reference_book.data;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.List;

public class TopicViewModel extends AndroidViewModel {
    private static final String TAG = "TopicViewModel";
    private final TopicRepository repository;
    private LiveData<List<TopicEntity>> allTopics;
    private final MutableLiveData<List<TopicEntity>> searchResults = new MutableLiveData<>();

    public TopicViewModel(@NonNull Application application) {
        super(application);
        repository = new TopicRepository(application);
        allTopics = repository.getAllTopics();
    }

    public void updateFavoriteStatus(int id, boolean isFavorite) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                repository.updateFavoriteStatus(id, isFavorite);
            } catch (Exception e) {
                Log.e(TAG, "Error updating favorite status", e);
                // Можно отправить ошибку в Crashlytics или показать уведомление
            }
        });
    }

    public LiveData<List<TopicEntity>> getAllTopics() {
        return allTopics;
    }

    public LiveData<List<TopicEntity>> getSearchResults() {
        return searchResults;
    }

    public void insertTopics(List<TopicEntity> topics) {
        repository.insertTopics(topics);
    }

    public void searchTopics(String query) {
        try {
            String safeQuery = "%" + query.replace("%", "\\%") + "%";
            repository.searchTopics(safeQuery).observeForever(topics -> {
                if (topics != null) {
                    searchResults.postValue(topics);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Search error", e);
            searchResults.postValue(new ArrayList<>());
        }
    }

    public void loadAllTopics() {
        allTopics = repository.getAllTopics();
    }

    public void saveUserNotes(int topicId, String notes) {
        repository.updateUserNotes(topicId, notes);
    }
}
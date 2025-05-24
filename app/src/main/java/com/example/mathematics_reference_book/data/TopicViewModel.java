package com.example.mathematics_reference_book.data;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

public class TopicViewModel extends AndroidViewModel {
    private final TopicRepository repository;
    private LiveData<List<TopicEntity>> allTopics;
    private final MutableLiveData<List<TopicEntity>> searchResults = new MutableLiveData<>();

    public TopicViewModel(@NonNull Application application) {
        super(application);
        repository = new TopicRepository(application);
        allTopics = repository.getAllTopics();
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

    public void updateFavoriteStatus(int id, boolean isFavorite) {
        repository.updateFavoriteStatus(id, isFavorite);
    }

    public void searchTopics(String query) {
        repository.searchTopics("%" + query + "%").observeForever(topics -> {
            searchResults.postValue(topics);
        });
    }

    public void loadAllTopics() {
        allTopics = repository.getAllTopics();
    }

    public void saveUserNotes(int topicId, String notes) {
        repository.updateUserNotes(topicId, notes);
    }
}
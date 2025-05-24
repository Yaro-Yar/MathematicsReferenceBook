package com.example.mathematics_reference_book;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mathematics_reference_book.adapters.TopicsAdapter;
import com.example.mathematics_reference_book.data.TopicEntity;
import com.example.mathematics_reference_book.data.TopicRepository;
import com.example.mathematics_reference_book.data.TopicViewModel;
import com.example.mathematics_reference_book.models.Topic;
import com.example.mathematics_reference_book.utils.TopicConverter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        TopicsAdapter.OnItemClickListener,
        TopicsAdapter.OnFavoriteClickListener {

    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "app_settings";
    private static final String DARK_MODE_KEY = "dark_mode";

    private TopicsAdapter adapter;
    private TopicViewModel topicViewModel;
    private SharedPreferences sharedPreferences;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSettings();
        setupRecyclerView();
        setupViewModel();
        handleSearchIntent(getIntent());
    }

    private void initSettings() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(DARK_MODE_KEY, false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.topicsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TopicsAdapter(new ArrayList<>(), this, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        topicViewModel = new ViewModelProvider(this).get(TopicViewModel.class);

        // Наблюдаем за всеми темами
        topicViewModel.getAllTopics().observe(this, topicEntities -> {
            if (topicEntities != null && !topicEntities.isEmpty()) {
                List<Topic> topics = convertToTopics(topicEntities);
                adapter.updateTopics(topics);
            } else {
                initializeDefaultTopics();
            }
        });

        // Наблюдаем за LiveData<List<TopicEntity>> из ViewModel
        topicViewModel.getAllTopics().observe(this, topicEntities -> {
            // Конвертируем TopicEntity → Topic и обновляем адаптер
            List<Topic> topics = TopicConverter.fromEntityList(topicEntities);
            adapter.updateTopics(topics); // UI автоматически обновится
        });

        topicViewModel = new ViewModelProvider(this).get(TopicViewModel.class);


        // Обработка поиска
        topicViewModel.getSearchResults().observe(this, topicEntities -> {
            List<Topic> topics = convertToTopics(topicEntities);
            adapter.updateTopics(topics);
        });
    }



    private List<Topic> convertToTopics(List<TopicEntity> entities) {
        List<Topic> topics = new ArrayList<>();
        for (TopicEntity entity : entities) {
            topics.add(new Topic.Builder(entity.getId(), entity.getTitle())
                    .description(entity.getDescription())
                    .formula(entity.getFormula())
                    .theory(entity.getTheory())
                    .category(entity.getCategory())
                    .difficultyLevel(entity.getDifficultyLevel())
                    .isFavorite(entity.isFavorite())
                    .build());
        }
        return topics;
    }

    private void initializeDefaultTopics() {
        List<TopicEntity> defaultTopics = new ArrayList<>();

        defaultTopics.add(new TopicEntity(
                5, getString(R.string.probability_title),
                getString(R.string.probability_description),
                getString(R.string.probability_formula),
                getString(R.string.probability_theory),
                "Математика", false, 3, ""));

        defaultTopics.add(new TopicEntity(
                1, getString(R.string.algebra_title),
                getString(R.string.algebra_description),
                getString(R.string.algebra_formula),
                getString(R.string.algebra_theory),
                "Математика", false, 3, ""));

        defaultTopics.add(new TopicEntity(
                3, getString(R.string.calculus_title),
                getString(R.string.calculus_description),
                getString(R.string.calculus_formula),
                getString(R.string.calculus_theory),
                "Высшая математика", false, 4, ""));

        defaultTopics.add(new TopicEntity(
                2, getString(R.string.geometry_title),
                getString(R.string.geometry_description),
                getString(R.string.geometry_formula),
                getString(R.string.geometry_theory),
                "Математика", false, 2, ""));

        topicViewModel.insertTopics(defaultTopics);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleSearchIntent(intent);
    }

    private void handleSearchIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            filterTopics(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        setupSearchView(menu);
        updateThemeMenuItem(menu);
        return true;
    }

    private void setupSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, MainActivity.class)));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterTopics(newText);
                return true;
            }
        });
    }

    private void filterTopics(String query) {
        if (query == null || query.isEmpty()) {
            topicViewModel.loadAllTopics();
        } else {
            topicViewModel.searchTopics(query);
        }
    }

    private void updateThemeMenuItem(Menu menu) {
        MenuItem themeItem = menu.findItem(R.id.action_theme);
        boolean isDark = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        themeItem.setTitle(isDark ? R.string.light_theme_label : R.string.dark_theme_label);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_theme) {
            toggleTheme();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleTheme() {
        boolean isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);

        sharedPreferences.edit()
                .putBoolean(DARK_MODE_KEY, !isDarkMode)
                .apply();

        invalidateOptionsMenu();
    }

    @Override
    public void onItemClick(Topic topic, int position) {
        startActivity(TopicActivity.newIntent(this, topic));
    }

    @Override
    public void onFavoriteClick(Topic topic, int position, boolean isFavorite) {
        topicViewModel.updateFavoriteStatus(topic.getId(), isFavorite);
        Toast.makeText(this,
                isFavorite ? R.string.add_to_favorites : R.string.remove_from_favorites,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}
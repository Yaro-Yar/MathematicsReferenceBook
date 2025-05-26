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
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mathematics_reference_book.adapters.TopicsAdapter;
import com.example.mathematics_reference_book.data.TopicEntity;
import com.example.mathematics_reference_book.data.TopicViewModel;
import com.example.mathematics_reference_book.models.Topic;
import com.example.mathematics_reference_book.utils.TopicConverter;
import java.util.ArrayList;
import java.util.List;
import android.text.InputType;

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

        // Инициализация обработчика кнопки "назад"
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (searchView != null && !searchView.isIconified()) {
                    searchView.setIconified(true);
                } else {
                    setEnabled(false);
                    MainActivity.super.getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

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


        topicViewModel.getAllTopics().observe(this, topicEntities -> {
            if (topicEntities != null) {
                List<Topic> topics = TopicConverter.fromEntityList(topicEntities);
                adapter.updateTopics(topics);
                if (topicEntities.isEmpty()) {
                    initializeDefaultTopics();
                }
            }
        });

        topicViewModel.getSearchResults().observe(this, topicEntities -> {
            if (topicEntities != null) {
                List<Topic> topics = TopicConverter.fromEntityList(topicEntities);
                adapter.updateTopics(topics);
            }
        });
    }

    private void initializeDefaultTopics() {
        List<TopicEntity> defaultTopics = new ArrayList<>();

        defaultTopics.add(new TopicEntity(
                1, getString(R.string.algebra_title),
                getString(R.string.algebra_description),
                getString(R.string.algebra_formula),
                getString(R.string.algebra_theory),
                "Математика", false, 3, ""));

        defaultTopics.add(new TopicEntity(
                2, getString(R.string.geometry_title),
                getString(R.string.geometry_description),
                getString(R.string.geometry_formula),
                getString(R.string.geometry_theory),
                "Математика", false, 2, ""));

        defaultTopics.add(new TopicEntity(
                3, getString(R.string.calculus_title),
                getString(R.string.calculus_description),
                getString(R.string.calculus_formula),
                getString(R.string.calculus_theory),
                "Высшая математика", false, 4, ""));

        defaultTopics.add(new TopicEntity(
                5, getString(R.string.probability_title),
                getString(R.string.probability_description),
                getString(R.string.probability_formula),
                getString(R.string.probability_theory),
                "Математика", false, 3, ""));

        defaultTopics.add(new TopicEntity(
                6, getString(R.string.trigonometry_title),
                getString(R.string.trigonometry_description),
                getString(R.string.trigonometry_formula),
                getString(R.string.trigonometry_theory),
                "Тригонометрия", false, 3, ""));

        topicViewModel.insertTopics(defaultTopics);
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        handleSearchIntent(intent);
    }

    private void handleSearchIntent(@NonNull Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (query != null) {
                filterTopics(query);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        setupSearchView(menu);
        updateThemeMenuItem(menu);
        return true;
    }

    private void setupSearchView(@NonNull Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();

            if (searchView != null && searchManager != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(
                        new ComponentName(this, MainActivity.class)));

                // Устанавливаем тип ввода
                searchView.setInputType(
                        InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS |
                                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if (query != null && !query.trim().isEmpty()) {
                            filterTopics(query.trim());
                        }
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (newText != null) {
                            filterTopics(newText);
                        }
                        return true;
                    }
                });
            }
        }
    }

    private void filterTopics(@NonNull String query) {
        try {
            if (query.isEmpty()) {
                topicViewModel.loadAllTopics();
            } else {
                // Нормализуем строку запроса
                String normalizedQuery = query.toLowerCase().trim();
                topicViewModel.searchTopics(normalizedQuery);
            }
        } catch (Exception e) {
            Log.e(TAG, "Search error", e);
            Toast.makeText(this, "Ошибка поиска", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateThemeMenuItem(@NonNull Menu menu) {
        MenuItem themeItem = menu.findItem(R.id.action_theme);
        if (themeItem != null) {
            boolean isDark = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
            themeItem.setTitle(isDark ? R.string.light_theme_label : R.string.dark_theme_label);
        }
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
        try {
            Intent intent = new Intent(this, TopicActivity.class);
            intent.putExtra(TopicActivity.EXTRA_TOPIC, topic);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
            Log.e("Navigation", "Failed to open topic", e);
            Toast.makeText(this, "Ошибка открытия темы", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFavoriteClick(Topic topic, int position, boolean isFavorite) {
        try {
            // Обновляем ViewModel
            topicViewModel.updateFavoriteStatus(topic.getId(), isFavorite);

            // Показываем уведомление
            Toast.makeText(this,
                    isFavorite ? R.string.add_to_favorites : R.string.remove_from_favorites,
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("FavoriteError", "Failed to update favorite", e);
            Toast.makeText(this, R.string.favorite_update_error, Toast.LENGTH_SHORT).show();
        }
    }

}
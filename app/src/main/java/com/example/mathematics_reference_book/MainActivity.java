package com.example.mathematics_reference_book;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mathematics_reference_book.adapters.TopicsAdapter;
import com.example.mathematics_reference_book.models.Topic;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        TopicsAdapter.OnItemClickListener,
        TopicsAdapter.OnFavoriteClickListener {

    private static final String PREFS_NAME = "app_settings";
    private static final String DARK_MODE_KEY = "dark_mode";

    private TopicsAdapter adapter;
    private List<Topic> allTopics;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeAppSettings();
        initializeTopics();
        setupRecyclerView();
        handleSearchIntent(getIntent());
    }

    private void initializeAppSettings() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(DARK_MODE_KEY, false);
        setAppTheme(isDarkMode);
    }

    private void setAppTheme(boolean isDarkMode) {
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    private void initializeTopics() {
        allTopics = new ArrayList<>();
        allTopics.add(new Topic.Builder(1, "Алгебра")
                .description("Основы алгебры, уравнения")
                .formula("ax² + bx + c = 0")
                .theory("Теория...")
                .category("")
                .difficultyLevel(3)
                .build());

        allTopics.add(new Topic.Builder(2, "Геометрия")
                .description("Фигуры, площади, объемы")
                .formula("S = πr²")
                .theory("Теория...")
                .category("")
                .isFavorite(true)
                .difficultyLevel(2)
                .build());

        allTopics.add(new Topic.Builder(3, "Тригонометрия")
                .description("Синусы, косинусы")
                .formula("sin²α + cos²α = 1")
                .theory("Теория...")
                .category("")
                .difficultyLevel(4)
                .build());
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.topicsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TopicsAdapter(allTopics, this, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleSearchIntent(intent);
    }

    private void handleSearchIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (query != null && adapter != null) {
                adapter.getFilter().filter(query);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        setupSearchView(menu);
        setupThemeToggle(menu);
        return true;
    }

    private void setupSearchView(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            ComponentName componentName = new ComponentName(this, MainActivity.class);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    private void setupThemeToggle(Menu menu) {
        MenuItem themeItem = menu.findItem(R.id.action_theme);
        boolean isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        themeItem.setTitle(isDarkMode ? R.string.light_theme_label : R.string.dark_theme_label);
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

        recreate();
    }

    @Override
    public void onItemClick(Topic topic, int position) {
        if (topic != null) {
            startActivity(TopicActivity.newIntent(this, topic));
        }
    }

    @Override
    public void onFavoriteClick(Topic topic, int position, boolean isFavorite) {
        if (topic != null) {
            topic.setFavorite(isFavorite);
            if (adapter != null) {
                adapter.notifyItemChanged(position);
            }
            showFavoriteToast(isFavorite);
        }
    }

    private void showFavoriteToast(boolean isFavorite) {
        String message = isFavorite ?
                getString(R.string.added_to_favorites) :
                getString(R.string.removed_from_favorites);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
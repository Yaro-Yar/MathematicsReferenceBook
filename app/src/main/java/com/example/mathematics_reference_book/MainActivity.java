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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mathematics_reference_book.adapters.TopicsAdapter;
import com.example.mathematics_reference_book.models.Topic;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements TopicsAdapter.OnItemClickListener,
        TopicsAdapter.OnFavoriteClickListener {

    private TopicsAdapter adapter;
    private List<Topic> allTopics;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "app_settings";
    private static final String DARK_MODE_KEY = "dark_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Настройка темы
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(DARK_MODE_KEY, false);
        setAppTheme(isDarkMode);

        initializeTopics();
        setupRecyclerView();
        handleSearchIntent(getIntent());
    }

    private void setAppTheme(boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void initializeTopics() {
        allTopics = new ArrayList<>();
        allTopics.add(new Topic(1, "Алгебра", "Основы алгебры, уравнения", "ax² + bx + c = 0", "Теория...", false));
        allTopics.add(new Topic(2, "Геометрия", "Фигуры, площади, объемы", "S = πr²", "Теория...", true));
        allTopics.add(new Topic(3, "Тригонометрия", "Синусы, косинусы", "sin²α + cos²α = 1", "Теория...", false));
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.topicsRecyclerView);
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
            adapter.getFilter().filter(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Настройка SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, MainActivity.class)));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        // Настройка переключателя темы
        MenuItem themeItem = menu.findItem(R.id.action_theme);
        boolean isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        themeItem.setTitle(isDarkMode ? R.string.light_theme_label : R.string.dark_theme_label);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

        // Сохраняем настройки
        sharedPreferences.edit()
                .putBoolean(DARK_MODE_KEY, !isDarkMode)
                .apply();

        recreate(); // Пересоздаем активность для применения темы
    }

    @Override
    public void onItemClick(Topic topic) {
        startActivity(TopicActivity.newIntent(this, topic));
    }

    @Override
    public void onFavoriteClick(int position, boolean isFavorite) {
        allTopics.get(position).setFavorite(isFavorite);
        adapter.notifyItemChanged(position);

        Toast.makeText(this,
                isFavorite ? "Добавлено в избранное" : "Удалено из избранного",
                Toast.LENGTH_SHORT).show();
    }
}
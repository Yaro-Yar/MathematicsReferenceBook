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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mathematics_reference_book.adapters.TopicsAdapter;
import com.example.mathematics_reference_book.models.Topic;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        TopicsAdapter.OnItemClickListener,
        TopicsAdapter.OnFavoriteClickListener {

    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "app_settings";
    private static final String DARK_MODE_KEY = "dark_mode";

    private TopicsAdapter adapter;
    private final List<Topic> allTopics = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSettings();
        initTopics();
        setupRecyclerView();
        handleSearchIntent(getIntent());
    }

    private void initSettings() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(DARK_MODE_KEY, false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    private void initTopics() {
        try {
            allTopics.add(new Topic.Builder(5, "Теория вероятностей")
                    .description("Раздел математики, изучающий случайные события и их вероятности.")
                    .formula("Основная формула вероятности: P(A) = n(A)/n(S)\n" +
                            "Формула полной вероятности: P(A) = Σ[P(A|Bᵢ)*P(Bᵢ)]\n" +
                            "Формула Байеса: P(A|B) = [P(B|A)*P(A)]/P(B)\n" +
                            "Математическое ожидание: E[X] = Σxᵢ*pᵢ")
                    .theory("Теория вероятностей - математическая дисциплина, которая:\n" +
                            "1. Изучает закономерности случайных явлений\n" +
                            "2. Позволяет количественно оценивать вероятность событий\n" +
                            "3. Широко применяется в статистике, физике, экономике и других науках\n\n" +
                            "Основные понятия:\n" +
                            "- Случайное событие\n" +
                            "- Вероятность (от 0 до 1)\n" +
                            "- Случайная величина\n" +
                            "- Распределение вероятностей")
                    .category("Математика")
                    .difficultyLevel(3)
                    .build());
            allTopics.add(new Topic.Builder(1, "Алгебра")
                    .description("Раздел математики, изучающий общие свойства операций и отношений.")
                    .formula("Квадратное уравнение: ax² + bx + c = 0\n" +
                            "Дискриминант: D = b² - 4ac\n" +
                            "Корни: x = (-b ± √D)/2a\n\n" +
                            "Свойства логарифмов:\n" +
                            "logₐ(xy) = logₐx + logₐy\n" +
                            "logₐ(x/y) = logₐx - logₐy\n" +
                            "logₐ(xᵇ) = b*logₐx")
                    .theory("Алгебра включает:\n" +
                            "1. Решение уравнений\n" +
                            "2. Работу с многочленами\n" +
                            "3. Изучение алгебраических структур\n\n" +
                            "Основные темы:\n" +
                            "- Линейная алгебра\n" +
                            "- Абстрактная алгебра\n" +
                            "- Алгебраическая геометрия")
                    .category("Математика")
                    .difficultyLevel(3)
                    .build());
            allTopics.add(new Topic.Builder(2, "Геометрия")
                    .description("Раздел математики, изучающий пространственные отношения и формы.")
                    .formula("Планиметрия:\n" +
                            "Площадь круга: S = πr²\n" +
                            "Теорема Пифагора: a² + b² = c²\n\n" +
                            "Стереометрия:\n" +
                            "Объем шара: V = (4/3)πr³\n" +
                            "Площадь сферы: S = 4πr²")
                    .theory("Основные разделы геометрии:\n" +
                            "1. Планиметрия (2D)\n" +
                            "2. Стереометрия (3D)\n" +
                            "3. Аналитическая геометрия\n\n" +
                            "Важные понятия:\n" +
                            "- Точки, линии, плоскости\n" +
                            "- Углы и расстояния\n" +
                            "- Геометрические преобразования")
                    .category("Математика")
                    .difficultyLevel(2)
                    .build());
            allTopics.add(new Topic.Builder(3, "Математический анализ")
                    .description("Раздел математики, изучающий функции, пределы, производные и интегралы.")
                    .formula("Производные:\n" +
                            "(xⁿ)' = nxⁿ⁻¹\n" +
                            "(sin x)' = cos x\n" +
                            "(eˣ)' = eˣ\n\n" +
                            "Интегралы:\n" +
                            "∫xⁿdx = xⁿ⁺¹/(n+1) + C (n ≠ -1)\n" +
                            "∫(1/x)dx = ln|x| + C")
                    .theory("Основные понятия анализа:\n" +
                            "1. Пределы и непрерывность\n" +
                            "2. Дифференциальное исчисление\n" +
                            "3. Интегральное исчисление\n\n" +
                            "Приложения:\n" +
                            "- Оптимизация\n" +
                            "- Физические модели\n" +
                            "- Экономические расчеты")
                    .category("Высшая математика")
                    .difficultyLevel(4)
                    .build());

        } catch (Exception e) {
            Log.e(TAG, "Topic initialization failed", e);
            Toast.makeText(this, "Error loading topics", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.topicsRecyclerView);
        if (recyclerView == null) {
            Log.e(TAG, "RecyclerView not found");
            return;
        }

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
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Настройка поиска
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            ComponentName componentName = new ComponentName(this, MainActivity.class);
            assert searchView != null;
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        }

        assert searchView != null;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterTopics(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterTopics(newText);
                return true;
            }
        });
    }

    private void filterTopics(String query) {
        if (adapter == null) return;

        if (query == null || query.isEmpty()) {
            adapter.updateTopics(new ArrayList<>(allTopics));
        } else {
            adapter.getFilter().filter(query.toLowerCase().trim());
        }
    }

    private void updateThemeMenuItem(Menu menu) {
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

        invalidateOptionsMenu(); // Обновляем меню
    }

    @Override
    public void onItemClick(Topic topic, int position) {
        if (topic == null) {
            Log.w(TAG, "Clicked null topic at position: " + position);
            return;
        }
        startActivity(TopicActivity.newIntent(MainActivity.this, topic));
    }

    @Override
    public void onFavoriteClick(Topic topic, int position, boolean isFavorite) {
        if (topic == null) return;

        topic.setFavorite(isFavorite);
        Toast.makeText(this,
                isFavorite ? R.string.add_to_favorites : R.string.remove_from_favorites,
                Toast.LENGTH_SHORT).show();

        // Обновляем данные (если используете БД или ViewModel)
        if (adapter != null) {
            adapter.notifyItemChanged(position);
        }
    }
}
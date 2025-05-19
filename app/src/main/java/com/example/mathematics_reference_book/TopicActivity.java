package com.example.mathematics_reference_book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.mathematics_reference_book.models.Topic;

public class TopicActivity extends AppCompatActivity {
    public static final String EXTRA_TOPIC = "topic";
    private Topic currentTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        initActionBar();
        initTopicData();
        initViews();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.topic_details);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
    }

    private void initTopicData() {
        if (getIntent() != null && getIntent().hasExtra(EXTRA_TOPIC)) {
            currentTopic = getIntent().getParcelableExtra(EXTRA_TOPIC);
        }
    }

    private void initViews() {
        if (currentTopic == null) {
            finish();
            return;
        }

        TextView titleView = findViewById(R.id.topicDetailTitle);
        TextView descriptionView = findViewById(R.id.topicDescription);
        TextView formulaView = findViewById(R.id.topicFormula);
        TextView theoryView = findViewById(R.id.topicTheory);
        View descDivider = findViewById(R.id.descDivider);
        View formulaDivider = findViewById(R.id.formulaDivider);

        // Установка основных данных
        titleView.setText(currentTopic.getTitle());
        descriptionView.setText(currentTopic.getDescription());

        // Обработка формул
        String formulas = currentTopic.getFormula();
        if (!TextUtils.isEmpty(formulas)) {
            formulaView.setText(formatFormulas(formulas));
            formulaView.setVisibility(View.VISIBLE);
            formulaDivider.setVisibility(View.VISIBLE);
        } else {
            formulaView.setVisibility(View.GONE);
            formulaDivider.setVisibility(View.GONE);
        }

        // Обработка теории
        String theory = currentTopic.getTheory();
        if (!TextUtils.isEmpty(theory)) {
            theoryView.setText(formatTheory(theory));
            theoryView.setVisibility(View.VISIBLE);
            descDivider.setVisibility(View.VISIBLE);
        } else {
            theoryView.setVisibility(View.GONE);
            descDivider.setVisibility(View.GONE);
        }
    }

    private String formatTheory(String theory) {
        if (theory == null || theory.isEmpty()) {
            return "";
        }

        return theory.replace("1.", "\n1.")
                .replace("2.", "\n2.")
                .replace("3.", "\n3.")
                .replace("- ", "\n• ")
                .replace("<br>", "\n")
                .replace("<br/>", "\n");
    }

    private String formatFormulas(String formulas) {
        if (formulas == null || formulas.isEmpty()) {
            return "";
        }
        return "Формулы:\n• " + formulas.replace("\n", "\n• ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        updateThemeMenuItem(menu);
        return true;
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
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_theme) {
            toggleTheme();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleTheme() {
        boolean isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES
        );
        recreate();
    }

    public static Intent newIntent(Context context, Topic topic) {
        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra(EXTRA_TOPIC, topic);
        return intent;
    }
}
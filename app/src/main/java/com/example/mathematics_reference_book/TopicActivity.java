package com.example.mathematics_reference_book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.text.HtmlCompat;

import com.example.mathematics_reference_book.models.Topic;

public class TopicActivity extends AppCompatActivity {

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
        currentTopic = getIntent().getParcelableExtra("topic");
        if (currentTopic == null) {
            finish();
        }
    }

    private void initViews() {
        TextView titleView = findViewById(R.id.topicDetailTitle);
        TextView formulaView = findViewById(R.id.topicFormula);
        TextView theoryView = findViewById(R.id.topicTheory);

        titleView.setText(currentTopic.getTitle());
        formulaView.setText(currentTopic.getFormula());
        setHtmlContentIfNeeded(theoryView, currentTopic.getTheory());
    }

    private void setHtmlContentIfNeeded(TextView textView, String content) {
        if (content.contains("<") && content.contains(">")) {
            textView.setText(HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(content);
        }
    }
    private void initializeViews() {
        // Get references to views
        TextView titleView = findViewById(R.id.topicDetailTitle);
        TextView formulaView = findViewById(R.id.topicFormula);
        TextView theoryView = findViewById(R.id.topicTheory);

        // Set text content
        titleView.setText(currentTopic.getTitle());
        formulaView.setText(currentTopic.getFormula());
        theoryView.setText(currentTopic.getTheory());

        // Set text direction if needed
        titleView.setTextDirection(View.TEXT_DIRECTION_LOCALE);
        formulaView.setTextDirection(View.TEXT_DIRECTION_LOCALE);
        theoryView.setTextDirection(View.TEXT_DIRECTION_LOCALE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Update theme menu item title based on current mode
        MenuItem themeItem = menu.findItem(R.id.action_theme);
        if (themeItem != null) {
            boolean isDark = getResources().getConfiguration().uiMode ==
                    android.content.res.Configuration.UI_MODE_NIGHT_YES;
            themeItem.setTitle(isDark ? R.string.light_theme_label : R.string.dark_theme_label);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Handle back button in action bar
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        } else if (id == R.id.action_theme) {
            // Handle theme toggle
            toggleTheme();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleTheme() {
        boolean isDarkMode = getResources().getConfiguration().uiMode ==
                android.content.res.Configuration.UI_MODE_NIGHT_YES;

        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES
        );

        // Restart activity to apply theme changes
        recreate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Add custom transition if needed
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    // Helper method to create intent for starting this activity
    public static Intent newIntent(Context context, Topic topic) {
        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra("topic", topic);
        return intent;
    }
}
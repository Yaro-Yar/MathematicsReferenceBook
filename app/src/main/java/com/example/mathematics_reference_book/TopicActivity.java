package com.example.mathematics_reference_book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mathematics_reference_book.models.Topic;

public class TopicActivity extends AppCompatActivity {
    private static final String EXTRA_TOPIC = "extra_topic";

    public static Intent newIntent(@NonNull Context context, @NonNull Topic topic) {
        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra(EXTRA_TOPIC, topic);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Topic topic = getIntent().getParcelableExtra(EXTRA_TOPIC);
        if (topic == null) {
            finish();
            return;
        }

        initViews(topic);
    }

    private void initViews(@NonNull Topic topic) {
        TextView titleView = findViewById(R.id.topicDetailTitle);
        TextView formulaView = findViewById(R.id.topicFormula);
        TextView theoryView = findViewById(R.id.topicTheory);

        titleView.setText(topic.getTitle());
        formulaView.setText(topic.getFormula());
        theoryView.setText(topic.getTheory());
    }
}
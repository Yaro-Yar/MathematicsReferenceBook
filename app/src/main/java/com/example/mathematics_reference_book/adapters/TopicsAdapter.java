package com.example.mathematics_reference_book.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mathematics_reference_book.R;
import com.example.mathematics_reference_book.models.Topic;
import java.util.ArrayList;
import java.util.List;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.TopicViewHolder> implements Filterable {
    private List<Topic> topics;
    private List<Topic> topicsFull;
    private final OnItemClickListener listener;
    private final OnFavoriteClickListener favoriteClickListener;
    private final Filter topicsFilter = new TopicsFilter();

    public interface OnItemClickListener {
        void onItemClick(Topic topic, int position);

        void onFavoriteClick(int position, boolean isFavorite);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Topic topic, int position, boolean isFavorite);
    }

    public TopicsAdapter(List<Topic> topics,
                         OnItemClickListener listener,
                         OnFavoriteClickListener favoriteClickListener) {
        this.topics = new ArrayList<>(topics);
        this.topicsFull = new ArrayList<>(topics);
        this.listener = listener;
        this.favoriteClickListener = favoriteClickListener;
    }

    public void updateTopics(List<Topic> newTopics) {
        this.topics = new ArrayList<>(newTopics);
        this.topicsFull = new ArrayList<>(newTopics);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic topic = topics.get(position);
        holder.bind(topic);

        holder.favoriteIcon.setOnClickListener(v -> {
            boolean newFavoriteState = !topic.isFavorite();
            topic.setFavorite(newFavoriteState);
            holder.favoriteIcon.setImageResource(
                    newFavoriteState ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border
            );
            if (favoriteClickListener != null) {
                favoriteClickListener.onFavoriteClick(topic, position, newFavoriteState);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(topic, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    @Override
    public Filter getFilter() {
        return topicsFilter;
    }

    private class TopicsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Topic> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(topicsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Topic topic : topicsFull) {
                    if (topic.getTitle().toLowerCase().contains(filterPattern) ||
                            topic.getDescription().toLowerCase().contains(filterPattern) ||
                            topic.getCategory().toLowerCase().contains(filterPattern)) {
                        filteredList.add(topic);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            topics.clear();
            topics.addAll((List<Topic>) results.values);
            notifyDataSetChanged();
        }
    }

    static class TopicViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView descTextView;
        private final ImageView favoriteIcon;

        TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.topicTitle);
            descTextView = itemView.findViewById(R.id.topicDesc);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
        }

        void bind(Topic topic) {
            titleTextView.setText(topic.getTitle());
            descTextView.setText(topic.getDescription());
            favoriteIcon.setImageResource(
                    topic.isFavorite() ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border
            );
            favoriteIcon.setContentDescription(
                    itemView.getContext().getString(
                            topic.isFavorite() ? R.string.remove_from_favorites : R.string.add_to_favorites
                    )
            );
        }
    }
}
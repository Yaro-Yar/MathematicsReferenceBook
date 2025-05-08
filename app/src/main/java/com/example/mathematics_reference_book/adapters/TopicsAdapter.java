package com.example.mathematics_reference_book.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mathematics_reference_book.R;
import com.example.mathematics_reference_book.models.Topic;
import java.util.ArrayList;
import java.util.List;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.TopicViewHolder> implements Filterable {
    private List<Topic> topics;
    private List<Topic> topicsFull;
    private final OnItemClickListener itemClickListener;
    private final OnFavoriteClickListener favoriteClickListener;

    public interface OnItemClickListener {
        void onItemClick(Topic topic, int position);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Topic topic, int position, boolean isFavorite);
    }

    public TopicsAdapter(List<Topic> topics, OnItemClickListener itemListener, OnFavoriteClickListener favoriteListener) {
        this.topics = topics != null ? new ArrayList<>(topics) : new ArrayList<>();
        this.topicsFull = new ArrayList<>(this.topics);
        this.itemClickListener = itemListener;
        this.favoriteClickListener = favoriteListener;
    }

    public void updateTopics(List<Topic> newTopics) {
        if (newTopics == null) {
            newTopics = new ArrayList<>();
        }

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new TopicDiffCallback(this.topics, newTopics));
        this.topics = new ArrayList<>(newTopics);
        this.topicsFull = new ArrayList<>(newTopics);
        diffResult.dispatchUpdatesTo(this);
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
        if (topic == null) return;

        holder.bind(topic);

        holder.favoriteIcon.setOnClickListener(v -> {
            boolean newFavoriteState = !topic.isFavorite();
            topic.setFavorite(newFavoriteState);
            holder.updateFavoriteIcon(newFavoriteState);
            if (favoriteClickListener != null) {
                favoriteClickListener.onFavoriteClick(topic, position, newFavoriteState);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(topic, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topics != null ? topics.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return new TopicsFilter();
    }

    private class TopicsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Topic> filteredList = new ArrayList<>();

            if (topicsFull == null) {
                topicsFull = new ArrayList<>();
            }

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(topicsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Topic topic : topicsFull) {
                    if (topic == null) continue;

                    String title = topic.getTitle() != null ? topic.getTitle().toLowerCase() : "";
                    String desc = topic.getDescription() != null ? topic.getDescription().toLowerCase() : "";
                    String category = topic.getCategory() != null ? topic.getCategory().toLowerCase() : "";

                    if (title.contains(filterPattern) ||
                            desc.contains(filterPattern) ||
                            category.contains(filterPattern)) {
                        filteredList.add(topic);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values == null) return;

            List<Topic> filteredList = (List<Topic>) results.values;
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new TopicDiffCallback(topics, filteredList));
            topics.clear();
            topics.addAll(filteredList);
            diffResult.dispatchUpdatesTo(TopicsAdapter.this);
        }
    }

    static class TopicDiffCallback extends DiffUtil.Callback {
        private final List<Topic> oldList;
        private final List<Topic> newList;

        TopicDiffCallback(List<Topic> oldList, List<Topic> newList) {
            this.oldList = oldList != null ? oldList : new ArrayList<>();
            this.newList = newList != null ? newList : new ArrayList<>();
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            Topic oldItem = oldList.get(oldItemPosition);
            Topic newItem = newList.get(newItemPosition);
            return oldItem != null && newItem != null && oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Topic oldItem = oldList.get(oldItemPosition);
            Topic newItem = newList.get(newItemPosition);
            return oldItem != null && oldItem.equals(newItem);
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
            if (topic == null) return;

            titleTextView.setText(topic.getTitle());
            descTextView.setText(topic.getDescription());
            updateFavoriteIcon(topic.isFavorite());
        }

        void updateFavoriteIcon(boolean isFavorite) {
            favoriteIcon.setImageResource(
                    isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border
            );
            favoriteIcon.setContentDescription(
                    itemView.getContext().getString(
                            isFavorite ? R.string.remove_from_favorites : R.string.add_to_favorites
                    )
            );
        }
    }
}
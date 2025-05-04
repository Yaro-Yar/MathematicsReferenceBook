package com.example.mathematics_reference_book.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Objects;

public class Topic implements Parcelable {
    public static final int MIN_DIFFICULTY = 1;
    public static final int MAX_DIFFICULTY = 5;
    public static final int DEFAULT_DIFFICULTY = 1;

    private final int id;
    @NonNull private final String title;
    @NonNull private final String description;
    @NonNull private final String formula;
    @NonNull private final String theory;
    @NonNull private final String category;
    private boolean isFavorite;
    private final int difficultyLevel;

    private Topic(Builder builder) {
        this.id = builder.id;
        this.title = Objects.requireNonNull(builder.title, "Title cannot be null");
        this.description = Objects.requireNonNull(builder.description, "Description cannot be null");
        this.formula = Objects.requireNonNull(builder.formula, "Formula cannot be null");
        this.theory = Objects.requireNonNull(builder.theory, "Theory cannot be null");
        this.category = Objects.requireNonNull(builder.category, "Category cannot be null");
        this.isFavorite = builder.isFavorite;
        this.difficultyLevel = validateDifficulty(builder.difficultyLevel);
    }

    protected Topic(@NonNull Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.formula = in.readString();
        this.theory = in.readString();
        this.category = in.readString();
        this.isFavorite = in.readByte() != 0;
        this.difficultyLevel = in.readInt();
    }

    private int validateDifficulty(int level) {
        return Math.max(MIN_DIFFICULTY, Math.min(MAX_DIFFICULTY, level));
    }

    // Builder class
    public static class Builder {
        private final int id;
        private final String title;
        private String description = "";
        private String formula = "";
        private String theory = "";
        private String category = "";
        private boolean isFavorite = false;
        private int difficultyLevel = DEFAULT_DIFFICULTY;

        public Builder(int id, @NonNull String title) {
            this.id = id;
            this.title = title;
        }

        public Builder description(@NonNull String description) {
            this.description = description;
            return this;
        }

        public Builder formula(@NonNull String formula) {
            this.formula = formula;
            return this;
        }

        public Builder theory(@NonNull String theory) {
            this.theory = theory;
            return this;
        }

        public Builder category(@NonNull String category) {
            this.category = category;
            return this;
        }

        public Builder isFavorite(boolean isFavorite) {
            this.isFavorite = isFavorite;
            return this;
        }

        public Builder difficultyLevel(int difficultyLevel) {
            this.difficultyLevel = difficultyLevel;
            return this;
        }

        public Topic build() {
            return new Topic(this);
        }
    }

    // Copy constructor for creating modified copies
    public Topic copyWithFavorite(boolean isFavorite) {
        return new Builder(this.id, this.title)
                .description(this.description)
                .formula(this.formula)
                .theory(this.theory)
                .category(this.category)
                .difficultyLevel(this.difficultyLevel)
                .isFavorite(isFavorite)
                .build();
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    // Getters
    public int getId() { return id; }
    @NonNull public String getTitle() { return title; }
    @NonNull public String getDescription() { return description; }
    @NonNull public String getFormula() { return formula; }
    @NonNull public String getTheory() { return theory; }
    @NonNull public String getCategory() { return category; }
    public boolean isFavorite() { return isFavorite; }
    public int getDifficultyLevel() { return difficultyLevel; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(formula);
        dest.writeString(theory);
        dest.writeString(category);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeInt(difficultyLevel);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Topic topic = (Topic) obj;
        return id == topic.id &&
                isFavorite == topic.isFavorite &&
                difficultyLevel == topic.difficultyLevel &&
                title.equals(topic.title) &&
                description.equals(topic.description) &&
                formula.equals(topic.formula) &&
                theory.equals(topic.theory) &&
                category.equals(topic.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, formula, theory,
                category, isFavorite, difficultyLevel);
    }

    @NonNull
    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", difficulty=" + difficultyLevel +
                ", favorite=" + isFavorite +
                '}';
    }
}
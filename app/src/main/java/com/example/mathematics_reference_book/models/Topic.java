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
    private static final String DEFAULT_STRING = "";

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
        this.title = ensureNotNull(builder.title, "Title");
        this.description = ensureNotNull(builder.description, "Description");
        this.formula = ensureNotNull(builder.formula, "Formula");
        this.theory = ensureNotNull(builder.theory, "Theory");
        this.category = ensureNotNull(builder.category, "Category");
        this.isFavorite = builder.isFavorite;
        this.difficultyLevel = validateDifficulty(builder.difficultyLevel);
    }

    protected Topic(@NonNull Parcel in) {
        this.id = in.readInt();
        this.title = in.readString() != null ? Objects.requireNonNull(in.readString()) : DEFAULT_STRING;
        this.description = in.readString() != null ? Objects.requireNonNull(in.readString()) : DEFAULT_STRING;
        this.formula = in.readString() != null ? Objects.requireNonNull(in.readString()) : DEFAULT_STRING;
        this.theory = in.readString() != null ? Objects.requireNonNull(in.readString()) : DEFAULT_STRING;
        this.category = in.readString() != null ? Objects.requireNonNull(in.readString()) : DEFAULT_STRING;
        this.isFavorite = in.readByte() != 0;
        this.difficultyLevel = in.readInt();
    }

    @NonNull
    private String ensureNotNull(String value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
        return value;
    }

    private int validateDifficulty(int level) {
        return Math.max(MIN_DIFFICULTY, Math.min(MAX_DIFFICULTY, level));
    }

    public static class Builder {
        private final int id;
        private final String title;
        private String description = DEFAULT_STRING;
        private String formula = DEFAULT_STRING;
        private String theory = DEFAULT_STRING;
        private String category = DEFAULT_STRING;
        private boolean isFavorite = false;
        private int difficultyLevel = DEFAULT_DIFFICULTY;

        public Builder(int id, @NonNull String title) {
            this.id = id;
            this.title = title;
        }

        public Builder description(@Nullable String description) {
            this.description = description != null ? description : DEFAULT_STRING;
            return this;
        }

        public Builder formula(@Nullable String formula) {
            this.formula = formula != null ? formula : DEFAULT_STRING;
            return this;
        }

        public Builder theory(@Nullable String theory) {
            this.theory = theory != null ? theory : DEFAULT_STRING;
            return this;
        }

        public Builder category(@Nullable String category) {
            this.category = category != null ? category : DEFAULT_STRING;
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

    public static final Creator<Topic> CREATOR = new Creator<>() {
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
    @NonNull public String getDescription() { return description; }
    @NonNull
    public String getTitle() { return title; }

    @NonNull
    public String getFormula() { return formula; }

    @NonNull
    public String getTheory() { return theory; }
    @NonNull public String getCategory() { return category; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
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
package com.example.mathematics_reference_book.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Objects;

public class Topic implements Parcelable {
    private final int id;
    @NonNull private final String title;
    @NonNull private final String description;
    @NonNull private final String formula;
    @NonNull private final String theory;
    @NonNull private final String category;
    private boolean isFavorite;
    private final int difficultyLevel; // 1-5

    public Topic(int id, @NonNull String title, @NonNull String description,
                 @NonNull String formula, @NonNull String theory,
                 @NonNull String category, boolean isFavorite, int difficultyLevel) {
        this.id = id;
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.formula = Objects.requireNonNull(formula, "Formula cannot be null");
        this.theory = Objects.requireNonNull(theory, "Theory cannot be null");
        this.category = Objects.requireNonNull(category, "Category cannot be null");
        this.isFavorite = isFavorite;
        this.difficultyLevel = validateDifficulty(difficultyLevel);
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
        return Math.max(1, Math.min(5, level));
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

    // Геттеры
    public int getId() { return id; }
    @NonNull public String getTitle() { return title; }
    @NonNull public String getDescription() { return description; }
    @NonNull public String getFormula() { return formula; }
    @NonNull public String getTheory() { return theory; }
    @NonNull public String getCategory() { return category; }
    public boolean isFavorite() { return isFavorite; }
    public int getDifficultyLevel() { return difficultyLevel; }

    // Сеттеры (только для изменяемых полей)
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

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
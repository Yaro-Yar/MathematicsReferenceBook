package com.example.mathematics_reference_book.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Objects;

@Entity(tableName = "topics")
public class TopicEntity {
    @PrimaryKey
    private int id;

    @NonNull
    private String title;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;

    @NonNull
    @ColumnInfo(name = "formula")
    private String formula;

    @NonNull
    @ColumnInfo(name = "theory")
    private String theory;

    @NonNull
    @ColumnInfo(name = "category")
    private String category;


    @ColumnInfo(name = "difficulty_level")
    private int difficultyLevel;

    @ColumnInfo(name = "user_notes")
    private String userNotes = "";

    public TopicEntity(int id, @NonNull String title, @NonNull String description,
                       @NonNull String formula, @NonNull String theory,
                       @NonNull String category, boolean isFavorite,
                       int difficultyLevel, String userNotes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.formula = formula;
        this.theory = theory;
        this.category = category;
        this.isFavorite = isFavorite;
        this.difficultyLevel = difficultyLevel;
        this.userNotes = userNotes != null ? userNotes : "";
    }

    // Геттеры и сеттеры для всех полей
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getTitle() { return title; }
    public void setTitle(@NonNull String title) { this.title = title; }

    @NonNull
    public String getDescription() { return description; }
    public void setDescription(@NonNull String description) { this.description = description; }

    @NonNull
    public String getFormula() { return formula; }
    public void setFormula(@NonNull String formula) { this.formula = formula; }

    @NonNull
    public String getTheory() { return theory; }
    public void setTheory(@NonNull String theory) { this.theory = theory; }

    @NonNull
    public String getCategory() { return category; }
    public void setCategory(@NonNull String category) { this.category = category; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public int getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(int difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public String getUserNotes() { return userNotes; }
    public void setUserNotes(String userNotes) { this.userNotes = userNotes != null ? userNotes : ""; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicEntity that = (TopicEntity) o;
        return id == that.id &&
                isFavorite == that.isFavorite &&
                difficultyLevel == that.difficultyLevel &&
                title.equals(that.title) &&
                description.equals(that.description) &&
                formula.equals(that.formula) &&
                theory.equals(that.theory) &&
                category.equals(that.category) &&
                userNotes.equals(that.userNotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, formula, theory, category, isFavorite, difficultyLevel, userNotes);
    }
}
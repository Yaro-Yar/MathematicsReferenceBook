package com.example.mathematics_reference_book.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TopicEntity.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TopicDao topicDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS topics_new (" +
                    "id INTEGER PRIMARY KEY NOT NULL, " +
                    "title TEXT NOT NULL, " +
                    "description TEXT NOT NULL, " +
                    "formula TEXT NOT NULL, " +
                    "theory TEXT NOT NULL, " +
                    "category TEXT NOT NULL, " +
                    "is_favorite INTEGER NOT NULL DEFAULT 0, " +
                    "difficulty_level INTEGER NOT NULL, " +
                    "user_notes TEXT)");

            database.execSQL("INSERT INTO topics_new (id, title, description, formula, " +
                    "theory, category, is_favorite, difficulty_level, user_notes) " +
                    "SELECT id, title, description, formula, theory, category, " +
                    "isFavorite, difficultyLevel, user_notes FROM topics");

            database.execSQL("DROP TABLE topics");
            database.execSQL("ALTER TABLE topics_new RENAME TO topics");
        }
    };

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE topics ADD COLUMN user_notes TEXT DEFAULT ''");
        }
    };

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "math_topics.db")
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                            .fallbackToDestructiveMigration() // Изменено с deprecated метода
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
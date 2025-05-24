package com.example.mathematics_reference_book;

import android.app.Application;
import android.util.Log;
import com.example.mathematics_reference_book.data.AppDatabase;

public class MathApp extends Application {
    private static final String TAG = "MathApplication";
    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();

        // Инициализация базы данных
        database = AppDatabase.getInstance(this);
        Log.d(TAG, "Application and database created");

        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            Log.e(TAG, "Uncaught exception", ex);
            System.exit(1);
        });
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}
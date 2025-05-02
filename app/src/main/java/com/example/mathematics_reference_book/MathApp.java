package com.example.mathematics_reference_book;

import android.app.Application;
import android.util.Log;

public class MathApp extends Application {
    private static final String TAG = "MathApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        // Инициализация здесь
        Log.d(TAG, "Application created");

        // Обработчик неотловленных исключений
        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            Log.e(TAG, "Uncaught exception", ex);
            // Здесь можно добавить отправку логов на сервер
            System.exit(1); // Завершаем процесс приложения
        });
    }
}
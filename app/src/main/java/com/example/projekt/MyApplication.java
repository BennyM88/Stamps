package com.example.projekt;

import android.app.Application;

import com.example.projekt.db.AppDatabase;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.init(this);
    }
}

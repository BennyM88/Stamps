package com.example.projekt.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.projekt.photo.PhotoDB;
import com.example.projekt.photo.PhotosDAO;

@Database(entities = {CountryDB.class, PhotoDB.class}, version = 8)
public abstract class AppDB extends RoomDatabase {
    public abstract CountryDAO countryDAO();
    public abstract PhotosDAO photosDAO();
}

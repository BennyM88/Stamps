package com.example.projekt.photo;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "photos")
public class PhotoDB {
    @PrimaryKey(autoGenerate = true)
    private final int id;
    private final int countryID;
    private final String path;

    public PhotoDB(int id, int countryID, String path) {
        this.id = id;
        this.countryID = countryID;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public int getCountryID() {
        return countryID;
    }

    public String getPath() {
        return path;
    }
}

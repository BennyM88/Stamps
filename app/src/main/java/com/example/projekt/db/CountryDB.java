package com.example.projekt.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "countries")
public class CountryDB {
    @PrimaryKey(autoGenerate = true)
    private final int id;
    private final String name;
    private final String continent;
    private final boolean visited;
    private final boolean wantToSee;
    private final String note;
    private final String photo;

    public CountryDB(int id, String name, String continent, boolean visited, boolean wantToSee, String note, String photo) {
        this.id = id;
        this.name = name;
        this.continent = continent;
        this.visited = visited;
        this.wantToSee = wantToSee;
        this.note = note;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContinent() {
        return continent;
    }

    public boolean isVisited() {
        return visited;
    }

    public boolean isWantToSee() {
        return wantToSee;
    }

    public String getNote() {
        return note;
    }

    public String getPhoto() {
        return photo;
    }

    @Override
    public String toString() {
        return name;
    }
}

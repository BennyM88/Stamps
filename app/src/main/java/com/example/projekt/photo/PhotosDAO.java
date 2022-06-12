package com.example.projekt.photo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao()
public interface PhotosDAO {
    @Insert()
    public void insertPhoto (PhotoDB photoDB);
    @Query("select * from photos where countryID=:countryID")
    LiveData<List<PhotoDB>> getPhotos(int countryID);
    @Query("delete from photos where id=:id")
    void delete(int id);
}

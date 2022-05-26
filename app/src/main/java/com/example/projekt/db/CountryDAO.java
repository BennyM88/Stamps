package com.example.projekt.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CountryDAO {
    @Insert
    void insert (CountryDB country);
    @Insert
    void insert (List<CountryDB> country);
    @Query("select * from countries")
    List<CountryDB> getAllCountries ();
    @Query("select * from countries where id=:id limit 1")
    LiveData<CountryDB> getCountryByID (int id);
    @Query("select * from countries")
    LiveData<List<CountryDB>> getLiveDataWithAllCountries ();
    @Query("select * from countries where name like :countryName || '%'")
    LiveData<List<CountryDB>> getLiveDataWithCountriesStartsWith (String countryName);
    @Query("update countries set note =:note where id=:id")
    void updateCountryNote(int id, String note);
    @Query("update countries set wantToSee =:isChecked where id=:id")
    void updateCountryStatus(int id, boolean isChecked);
    @Query("select * from countries where wantToSee = 1")
    LiveData<List<CountryDB>> getLiveDataWithWantToSeeCountries();
}

package com.example.projekt.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.projekt.photo.PhotosDAO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class AppDatabase {
    private static AppDatabase instance;

    public CountryDAO getCountryDAO() {
        return countryDAO;
    }

    private final CountryDAO countryDAO;
    private final PhotosDAO photosDAO;

    public PhotosDAO getPhotosDAO() {
        return photosDAO;
    }

    private AppDatabase(Context context)
    {
        AppDB db = Room.databaseBuilder(context,
                AppDB.class, "database-countries")
                .allowMainThreadQueries()
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            List<CountryJson> countries = getCountriesFromFile(context);
                            loadData(countries,countryDAO);
                        });
                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                    }

                    @Override
                    public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
                        super.onDestructiveMigration(db);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            List<CountryJson> countries = getCountriesFromFile(context);
                            loadData(countries,countryDAO);
                        });
                    }
                })
                .fallbackToDestructiveMigration()
                .build();
        countryDAO = db.countryDAO();
        photosDAO = db.photosDAO();
    }
    public static void init(Context context)
    {
        if(instance==null)
        {
            instance=new AppDatabase(context);
        }
    }
    public static AppDatabase getInstance()
    {
        return instance;
    }
    private List<CountryJson> getCountriesFromFile(Context context)
    {
        String json = null;
        try {
            InputStream is = context.getAssets().open("Countries.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Type reviewType = new TypeToken<List<CountryJson>>() {}.getType();
        List<CountryJson> countries = new Gson()
                .fromJson(json,reviewType);
        return countries;
    }
    private void loadData(List<CountryJson> countries, CountryDAO dao)
    {
        List<CountryDB> items = new ArrayList<>();
        for(CountryJson country:countries)
        {
            CountryDB item = new CountryDB(0,country.getName(),country.getContinent(),false,false,"","");
            items.add(item);
        }
        dao.insert(items);
    }
}

package com.example.projekt;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projekt.db.AppDatabase;
import com.example.projekt.db.CountryDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class HomeFragment extends Fragment
{
    TextView textView;
    TextView textView2;
    TextView textView3;
    ProgressBar loading;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        textView = (TextView) view.findViewById(R.id.textViewCounter);
        textView2 = (TextView) view.findViewById(R.id.textViewWorld);
        textView3 = (TextView) view.findViewById(R.id.textViewContinents);
        loading = (ProgressBar) view.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        AppDatabase.getInstance().getCountryDAO().getLiveDataWithAllCountries().observe(getViewLifecycleOwner(), this::fillUI);
        return view;
    }

    private void fillUI(List<CountryDB> countryDBS) {
        int counter = 0;
        HashMap<String, List<CountryDB>> resultMap = new HashMap<>();
        for(CountryDB countryDB: countryDBS) {
            if(countryDB.isWantToSee())
            {
                counter++;
            }
            List<CountryDB> items = resultMap.get(countryDB.getContinent());
            if(items == null) {
                items = new ArrayList<>();
                items.add(countryDB);
                resultMap.put(countryDB.getContinent(), items);
            }
            else {
                items.add(countryDB);
            }
        }
        textView.setText(String.format(Locale.ROOT,"%d / %d", counter, countryDBS.size()));
        int w = Math.round(((counter*1.0f)/countryDBS.size())*100);
        textView2.setText(String.format(Locale.ROOT, "World : % d %%", w));
        for(Map.Entry<String, List<CountryDB>> entry : resultMap.entrySet()) {
            int c = Math.round((calculatePercent(entry.getValue()))*100);
            textView3.append(String.format(Locale.ROOT,entry.getKey() + " : % d %% \n", c));
        }
        loading.setVisibility(View.GONE);
    }

    private float calculatePercent(List<CountryDB> countryDBS) {
        int counter = 0;
        for(CountryDB countryDB: countryDBS) {
            if(countryDB.isWantToSee())
            {
                counter++;
            }
        }
        return (counter*1.0f)/countryDBS.size();
    }
}

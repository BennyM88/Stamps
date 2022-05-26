package com.example.projekt.country;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.projekt.R;
import com.example.projekt.databinding.FragmentCountryBinding;
import com.example.projekt.db.AppDatabase;
import com.example.projekt.db.CountryDB;
import com.example.projekt.details.DetailsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountryFragment extends Fragment
{
    private FragmentCountryBinding binding;
    private CountriesAdapter adapter;
    private LiveData<List<CountryDB>> items;
    private final MutableLiveData<String> searchText = new MutableLiveData<>("");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = Transformations.switchMap(searchText, input -> AppDatabase.getInstance().getCountryDAO().getLiveDataWithCountriesStartsWith(input));
        adapter = new CountriesAdapter(new CountriesAdapterListener() {
            @Override
            public void countrySelected(CountryItem item) {
                navigateToDetails(item);
            }

            @Override
            public void changeCountryStatus(CountryItem countryItem, boolean isChecked) {
                AppDatabase.getInstance()
                        .getCountryDAO()
                        .updateCountryStatus(countryItem.getId(), isChecked);
            }
        });

    }

    private void navigateToDetails(CountryItem countryItem) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, DetailsFragment.newInstance(countryItem.getId()))
                .addToBackStack(null)
                .commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        FragmentCountryBinding binding = FragmentCountryBinding.inflate(inflater, container, false);
        this.binding = binding;

        setHasOptionsMenu(true);

        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterCountries(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterCountries(newText);
                return false;
            }
        });
        binding.loading.setVisibility(View.VISIBLE);
        items.observe(getViewLifecycleOwner(), this::fillAdapter);
        return binding.getRoot();
    }

    private void filterCountries(String newText) {
        searchText.setValue(newText);
    }

    @Override
   public void onDestroy()
   {
       super.onDestroy();
   }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.listview.setAdapter(adapter);
    }
    private void fillAdapter(List<CountryDB> countryDBS) {
        HashMap<String, List<CountryDB>> map = new HashMap<>();
        for (CountryDB country: countryDBS) {
            List<CountryDB> item = map.get(country.getContinent());
            if (item == null) {
                List<CountryDB> items = new ArrayList<>();
                items.add(country);
                map.put(country.getContinent(), items);
            }
            else {
                item.add(country);
            }
        }
        List<ItemType> itemsUI = new ArrayList<>();
        for (Map.Entry<String, List<CountryDB>> entry: map.entrySet()) {
            itemsUI.add(new ContinentItem(entry.getKey()));
            for (CountryDB country: entry.getValue()){
                itemsUI.add(new CountryItem(country.getName(), country.getId(), country.isWantToSee()));
            }
        }
        adapter.updateItems(itemsUI);
        binding.loading.setVisibility(View.GONE);
    }
}

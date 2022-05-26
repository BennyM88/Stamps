package com.example.projekt.country;

public interface CountriesAdapterListener {
    void countrySelected(CountryItem item);

    void changeCountryStatus(CountryItem countryItem, boolean isChecked);
}

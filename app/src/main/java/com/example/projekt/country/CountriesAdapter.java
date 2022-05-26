package com.example.projekt.country;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt.databinding.ItemCountryBinding;
import com.example.projekt.databinding.ItemHeaderBinding;

import java.util.ArrayList;
import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {
    private final List<ItemType> items = new ArrayList<>();
    private final CountriesAdapterListener listener;

    public CountriesAdapter(CountriesAdapterListener listener){
        this.listener = listener;
    }

    public void updateItems (List <ItemType> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0) {
            return new HeaderView(ItemHeaderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }
        else {
            return new CountryView(ItemCountryBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemType data = items.get(position);
        if(data instanceof CountryItem) {
            CountryItem item = (CountryItem) data;
            CountryView view = (CountryView) holder;
            view.binding.countryTextView.setText(item.getName());
            view.binding.getRoot().setOnClickListener(v -> listener.countrySelected(item));
            view.binding.countryStatus.setOnCheckedChangeListener(null);
            view.binding.countryStatus.setChecked(item.isWantToSee());
            view.binding.countryStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(item.isWantToSee() != isChecked) {
                    listener.changeCountryStatus(item, isChecked);
                }
            });
        }
        else {
            ContinentItem item = (ContinentItem) data;
            HeaderView view = (HeaderView) holder;
            view.binding.headerTextView.setText(item.getName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType() == EItemType.Header? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HeaderView extends RecyclerView.ViewHolder
    {
        private final ItemHeaderBinding binding;
        public HeaderView(@NonNull ItemHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    static class CountryView extends RecyclerView.ViewHolder
    {
        private final ItemCountryBinding binding;
        public CountryView(@NonNull ItemCountryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

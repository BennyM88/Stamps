package com.example.projekt.details;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projekt.R;
import com.example.projekt.databinding.FragmentDetailsBinding;
import com.example.projekt.db.AppDatabase;
import com.example.projekt.db.CountryDB;
import com.example.projekt.photo.PhotoDB;

public class DetailsFragment extends Fragment {
    private FragmentDetailsBinding binding;
    private CountryDB countryDB;
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(), result -> {
                try {
                    AppDatabase.getInstance().getPhotosDAO().insertPhoto(new PhotoDB(0, countryDB.getId(), result.toString()));
                }
                catch (Throwable e) {

                }
            });
    private PhotosAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PhotosAdapter(getActivity().getContentResolver());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        FragmentDetailsBinding binding = FragmentDetailsBinding.inflate(inflater, container, false);
        this.binding = binding;

        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done) {
            Toast.makeText(getActivity().getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();
            updateCountry();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateCountry() {
        if (countryDB != null) {
            String note = binding.countryNote.getText().toString();
            AppDatabase.getInstance().getCountryDAO().updateCountryNote(countryDB.getId(), note);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.images.setAdapter(adapter);
        int id = getArguments().getInt("id");
        AppDatabase.getInstance()
                .getCountryDAO()
                .getCountryByID(id)
                .observe(getViewLifecycleOwner(), countryDBS -> {
                    countryDB = countryDBS;
                    fillDetails(countryDBS);
                });
        loadPhotosForCountry(id);
        binding.addPhotoButton.setOnClickListener(v -> selectPhoto());
    }

    private void loadPhotosForCountry(int id) {
        AppDatabase.getInstance().getPhotosDAO().getPhotos(id).observe(getViewLifecycleOwner(), photoDBS -> {
            adapter.updateItems(photoDBS);
        });
    }

    private void selectPhoto() {
        mGetContent.launch("image/*");
    }

    private void fillDetails(CountryDB countryDBS) {
        binding.countryName.setText(countryDBS.getName());
        binding.countryNote.setText(countryDBS.getNote());
    }

    public static DetailsFragment newInstance(int id) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }
}

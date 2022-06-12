package com.example.projekt.details;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt.databinding.ItemPhotoBinding;
import com.example.projekt.photo.PhotoDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PhotosAdapter extends RecyclerView.Adapter <PhotosAdapter.PhotoView> {
    private final List<PhotoDB> items = new ArrayList<>();
    private final PhotosAdapterListener listener;
    private final ContentResolver contentResolver;

    public PhotosAdapter(PhotosAdapterListener listener, ContentResolver contentResolver){
        this.listener = listener;
        this.contentResolver = contentResolver;
    }



    public void updateItems (List <PhotoDB> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PhotoView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoView(ItemPhotoBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoView holder, int position) {
        try {
            PhotoDB data = items.get(position);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(data.getPath()));
            holder.binding.countryImage.setImageBitmap(bitmap);
            holder.itemView.setOnLongClickListener(v -> {
                listener.delete(data.getId());
                return true;
            });
            ExifInterface exif = new ExifInterface(contentResolver.openInputStream(Uri.parse(data.getPath())));
            float [] latLong = new float[2];
            boolean hasGPS = exif.getLatLong(latLong);
            if(hasGPS){

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(holder.itemView.getContext(), Locale.getDefault());

                addresses = geocoder.getFromLocation(latLong[0], latLong[1], 1);

                String city = addresses.get(0).getLocality();
                String country = addresses.get(0).getCountryName();
                holder.binding.location.setText(String.format(Locale.ENGLISH,"\nlat: %.4f lng: %.4f\ncountry: %s ", latLong[0], latLong[1], country));
            }
            else{
                holder.binding.location.setText("Nie ma wspolrzednych");
            }
        }
        catch (Throwable e) {

        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    static class PhotoView extends RecyclerView.ViewHolder
    {
        private final ItemPhotoBinding binding;
        public PhotoView(@NonNull ItemPhotoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

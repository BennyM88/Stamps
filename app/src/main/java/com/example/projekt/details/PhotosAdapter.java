package com.example.projekt.details;

import android.content.ContentResolver;
import android.graphics.Bitmap;
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

public class PhotosAdapter extends RecyclerView.Adapter <PhotosAdapter.PhotoView> {
    private final List<PhotoDB> items = new ArrayList<>();
    private final ContentResolver contentResolver;

    public PhotosAdapter(ContentResolver contentResolver){
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

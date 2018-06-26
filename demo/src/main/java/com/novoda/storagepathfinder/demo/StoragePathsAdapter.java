package com.novoda.storagepathfinder.demo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.novoda.storagepathfinder.StoragePath;

import java.util.List;

class StoragePathsAdapter extends RecyclerView.Adapter<StoragePathViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<StoragePath> deviceStoragePaths;
    private final StoragePathViewHolder.Listener onAddFileClicked;

    StoragePathsAdapter(LayoutInflater layoutInflater,
                        List<StoragePath> deviceStoragePaths,
                        StoragePathViewHolder.Listener onAddFileClicked) {
        this.layoutInflater = layoutInflater;
        this.deviceStoragePaths = deviceStoragePaths;
        this.onAddFileClicked = onAddFileClicked;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public StoragePathViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = layoutInflater.inflate(R.layout.item_device_storage_root, parent, false);
        return new StoragePathViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull StoragePathViewHolder holder, int position) {
        StoragePath deviceStoragePath = deviceStoragePaths.get(position);
        holder.bind(deviceStoragePath, onAddFileClicked);
    }

    @Override
    public int getItemCount() {
        return deviceStoragePaths.size();
    }

    @Override
    public long getItemId(int position) {
        return deviceStoragePaths.get(position).hashCode();
    }
}

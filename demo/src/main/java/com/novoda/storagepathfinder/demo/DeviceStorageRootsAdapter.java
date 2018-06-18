package com.novoda.storagepathfinder.demo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.novoda.storagepathfinder.DeviceStorageRoot;

import java.util.List;

class DeviceStorageRootsAdapter extends RecyclerView.Adapter<DeviceStorageRootViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<DeviceStorageRoot> deviceStorageRoots;

    DeviceStorageRootsAdapter(LayoutInflater layoutInflater, List<DeviceStorageRoot> deviceStorageRoots) {
        this.layoutInflater = layoutInflater;
        this.deviceStorageRoots = deviceStorageRoots;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public DeviceStorageRootViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = layoutInflater.inflate(R.layout.item_device_storage_root, parent, false);
        return new DeviceStorageRootViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceStorageRootViewHolder holder, int position) {
        DeviceStorageRoot deviceStorageRoot = deviceStorageRoots.get(position);
        holder.bind(deviceStorageRoot);
    }

    @Override
    public int getItemCount() {
        return deviceStorageRoots.size();
    }

    @Override
    public long getItemId(int position) {
        return deviceStorageRoots.get(position).hashCode();
    }
}

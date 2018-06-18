package com.novoda.storagepathfinder.demo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.novoda.storagepathfinder.DeviceStorageRoot;

class DeviceStorageRootViewHolder extends RecyclerView.ViewHolder {

    private final TextView absolutePath;

    DeviceStorageRootViewHolder(View root) {
        super(root);
        absolutePath = root.findViewById(R.id.device_storage_root);
    }

    void bind(DeviceStorageRoot deviceStorageRoot) {
        absolutePath.setText(deviceStorageRoot.getAbsolutePath());
    }

}

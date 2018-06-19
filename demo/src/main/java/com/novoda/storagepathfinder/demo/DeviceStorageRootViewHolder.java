package com.novoda.storagepathfinder.demo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.novoda.storagepathfinder.DeviceStorageRoot;

class DeviceStorageRootViewHolder extends RecyclerView.ViewHolder {

    private final TextView absolutePath;
    private final View addFile;

    DeviceStorageRootViewHolder(View root) {
        super(root);
        absolutePath = root.findViewById(R.id.device_storage_root);
        addFile = root.findViewById(R.id.add_file);
    }

    void bind(DeviceStorageRoot deviceStorageRoot, Listener listener) {
        absolutePath.setText(deviceStorageRoot.getAbsolutePath());
        addFile.setOnClickListener(v -> listener.onAddFileClickedFor(deviceStorageRoot));
    }

    interface Listener {
        void onAddFileClickedFor(DeviceStorageRoot deviceStorageRoot);
    }
}

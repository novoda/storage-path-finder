package com.novoda.storagepathfinder.demo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.novoda.storagepathfinder.StoragePath;

class StoragePathViewHolder extends RecyclerView.ViewHolder {

    private final TextView absolutePath;
    private final View addFile;

    StoragePathViewHolder(View root) {
        super(root);
        absolutePath = root.findViewById(R.id.device_storage_root);
        addFile = root.findViewById(R.id.add_file);
    }

    void bind(StoragePath deviceStoragePath, Listener listener) {
        absolutePath.setText(deviceStoragePath.asString());
        addFile.setOnClickListener(v -> listener.onAddFileClickedFor(deviceStoragePath));
    }

    interface Listener {
        void onAddFileClickedFor(StoragePath deviceStoragePath);
    }
}

package com.novoda.storagepathfinder.demo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.novoda.storagepathfinder.StoragePath

internal class StoragePathsAdapter(private val layoutInflater: LayoutInflater,
                                   private val deviceStoragePaths: List<StoragePath>,
                                   private val onAddFileClicked: StoragePathViewHolder.Listener) : RecyclerView.Adapter<StoragePathViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoragePathViewHolder {
        return StoragePathViewHolder(layoutInflater.inflate(R.layout.item_device_storage_root, parent, false))
    }

    override fun onBindViewHolder(holder: StoragePathViewHolder, position: Int) {
        holder.bind(deviceStoragePaths[position], onAddFileClicked)
    }

    override fun getItemCount(): Int {
        return deviceStoragePaths.size
    }

    override fun getItemId(position: Int): Long {
        return deviceStoragePaths[position].hashCode().toLong();
    }
}

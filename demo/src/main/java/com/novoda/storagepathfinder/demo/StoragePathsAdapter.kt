package com.novoda.storagepathfinder.demo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.novoda.storagepathfinder.StoragePath

typealias Listener<T> = (T) -> Unit

internal class StoragePathsAdapter(private val layoutInflater: LayoutInflater,
                                   private val deviceStoragePaths: List<StoragePath>,
                                   private val onAddFileClicked: Listener<StoragePath>) : RecyclerView.Adapter<StoragePathViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoragePathViewHolder =
            StoragePathViewHolder(layoutInflater.inflate(R.layout.item_device_storage_root, parent, false))

    override fun onBindViewHolder(holder: StoragePathViewHolder, position: Int) {
        holder.bind(deviceStoragePaths[position], onAddFileClicked)
    }

    override fun getItemCount(): Int = deviceStoragePaths.size

    override fun getItemId(position: Int): Long = deviceStoragePaths[position].hashCode().toLong()

}

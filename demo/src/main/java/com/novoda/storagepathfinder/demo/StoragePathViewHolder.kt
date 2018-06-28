package com.novoda.storagepathfinder.demo

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

import com.novoda.storagepathfinder.StoragePath

internal class StoragePathViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val absolutePath: TextView = root.findViewById(R.id.device_storage_root)
    private val addFile: View = root.findViewById(R.id.add_file)

    fun bind(item: StoragePath, listener: (StoragePath) -> Unit) = with(itemView) {
        itemView
        itemTitle.text = item.title
        itemImage.loadUrl(item.url)
        setOnClickListener { listener(item) }
    }

    fun bind(deviceStoragePath: StoragePath, listener: Listener) = with(root) {
        
        

        absolutePath.text = deviceStoragePath.pathAsString
        addFile.setOnClickListener { v -> listener.onAddFileClickedFor(deviceStoragePath) }
    }

    internal interface Listener {
        fun onAddFileClickedFor(deviceStoragePath: StoragePath)
    }
}

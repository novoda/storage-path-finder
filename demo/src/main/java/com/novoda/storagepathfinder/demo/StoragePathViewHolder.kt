package com.novoda.storagepathfinder.demo

import android.support.v7.widget.RecyclerView
import android.view.View
import com.novoda.storagepathfinder.StoragePath
import kotlinx.android.synthetic.main.include_device_storage_root.view.*

internal class StoragePathViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: StoragePath, listener: Listener) = with(itemView) {
        itemView?.device_storage_root?.text = item.pathAsString;
        itemView?.add_file?.setOnClickListener { listener.onAddFileClickedFor(item) }
    }

    internal interface Listener {
        fun onAddFileClickedFor(deviceStoragePath: StoragePath)
    }
}

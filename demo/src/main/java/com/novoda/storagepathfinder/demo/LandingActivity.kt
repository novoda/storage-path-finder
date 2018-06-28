package com.novoda.storagepathfinder.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.novoda.storagepathfinder.StoragePath
import kotlinx.android.synthetic.main.activity_landing.*
import java.util.*

class LandingActivity : AppCompatActivity() {

    private var assetCloner: AssetCloner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        assetCloner = DemoDependenciesFactory.createAssetCloner(applicationContext)
        val storageInspector = DemoDependenciesFactory.createStorageInspector(applicationContext)

        val deviceStoragePaths = ArrayList<StoragePath>()
        deviceStoragePaths.add(storageInspector.primaryStorageBasePath)
        deviceStoragePaths.add(storageInspector.primaryStorageApplicationPath)
        deviceStoragePaths.addAll(storageInspector.secondaryStorageBasePath)
        deviceStoragePaths.addAll(storageInspector.secondaryStorageApplicationPath)

        device_storage_roots.layoutManager = LinearLayoutManager(applicationContext)
        device_storage_roots.adapter = StoragePathsAdapter(
                layoutInflater,
                deviceStoragePaths,
                onAddFileClicked
        )
    }

    private val onAddFileClicked: (StoragePath) -> Unit = { deviceStoragePath ->
        val pathToCloneTo = deviceStoragePath.pathAsString + "/" + ASSET_NAME
        assetCloner?.cloneAsset(ASSET_NAME, pathToCloneTo)
    }

    companion object {
        private const val ASSET_NAME = "10MB.zip"
    }

}

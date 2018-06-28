package com.novoda.storagepathfinder.demo

import android.content.res.AssetManager
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executor

internal class AssetCloner(private val assetManager: AssetManager, private val executor: Executor) {

    fun cloneAsset(assetName: String, pathToCloneTo: String) {
        executor.execute {
            File(pathToCloneTo).also {
                it.parentFile?.takeIf { !it.exists() }?.also { it.mkdirs() }
            }.also { copyAssetToFile(assetName, it) }
        }
    }

    private fun copyAssetToFile(assetName: String, outputFile: File) = try {
        assetManager.open(assetName)
                .copyTo(FileOutputStream(outputFile, true))
    } catch (e: IOException) {
        Log.e(javaClass.simpleName, "Failed to close streams.", e)
    }

}

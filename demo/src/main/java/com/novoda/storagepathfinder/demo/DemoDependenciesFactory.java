package com.novoda.storagepathfinder.demo;

import android.content.Context;
import android.content.res.AssetManager;

import com.novoda.storagepathfinder.AndroidDeviceStorageInspector;

import java.util.concurrent.Executors;

public final class DemoDependenciesFactory {

    private DemoDependenciesFactory() {
        // Uses static factory methods.
    }

    public static AssetCloner createAssetCloner(Context context) {
        AssetManager assetManager = context.getAssets();
        return new AssetCloner(assetManager, Executors.newSingleThreadExecutor());
    }

    public static AndroidDeviceStorageInspector createStorageInspector(Context context) {
        return AndroidDeviceStorageInspector.builder(context).build();
    }
}

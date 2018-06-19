package com.novoda.storagepathfinder.demo;

import android.content.Context;
import android.content.res.AssetManager;

import com.novoda.storagepathfinder.AndroidApplicationDirectories;
import com.novoda.storagepathfinder.AndroidDeviceFeatures;
import com.novoda.storagepathfinder.AndroidDeviceStorageInspector;
import com.novoda.storagepathfinder.AndroidFileSystem;
import com.novoda.storagepathfinder.AndroidSystem;
import com.novoda.storagepathfinder.CommonDirectories;
import com.novoda.storagepathfinder.DeviceFeatures;
import com.novoda.storagepathfinder.FileSystem;

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
        CommonDirectories commonDirectories = new AndroidApplicationDirectories(context.getApplicationContext());
        FileSystem fileSystem = new AndroidFileSystem(commonDirectories);
        DeviceFeatures deviceFeatures = new AndroidDeviceFeatures();
        AndroidSystem androidSystem = new AndroidSystem();
        return new AndroidDeviceStorageInspector(
                context.getApplicationContext(),
                fileSystem,
                deviceFeatures,
                androidSystem
        );
    }
}

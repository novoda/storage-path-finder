package com.novoda.storagepathfinder.demo;

import android.content.Context;
import android.content.res.AssetManager;

import java.util.concurrent.Executors;

public final class DemoDependenciesFactory {

    private DemoDependenciesFactory() {
        // Uses static factory methods.
    }

    public static AssetCloner createAssetCloner(Context context) {
        AssetManager assetManager = context.getAssets();
        return new AssetCloner(assetManager, Executors.newSingleThreadExecutor());
    }
}

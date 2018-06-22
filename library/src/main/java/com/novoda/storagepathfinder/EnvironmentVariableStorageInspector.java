package com.novoda.storagepathfinder;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnvironmentVariableStorageInspector implements SecondaryDeviceStorageInspector {

    private static final StoragePath.Type SECONDARY = StoragePath.Type.SECONDARY;
    @Nullable
    private String secondaryStoragePath;

    EnvironmentVariableStorageInspector(AndroidSystem system) {
        secondaryStoragePath = system.getEnv("SECONDARY_STORAGE");   // NOT ALL MANUFACTURERS SET THIS....  Also it can be null :(
    }

    @Override
    public List<StoragePath> getSecondaryDeviceStorageBasePaths() {
        List<StoragePath> secondaryStorageRoot = new ArrayList<>(1);
        if (secondaryStoragePath != null) {
            secondaryStorageRoot.add(DeviceStoragePath.create(secondaryStoragePath, SECONDARY));
        }
        return secondaryStorageRoot;
    }

    @Override
    public List<StoragePath> getSecondaryDeviceStorageApplicationPaths() {
        return Collections.emptyList();
    }

}

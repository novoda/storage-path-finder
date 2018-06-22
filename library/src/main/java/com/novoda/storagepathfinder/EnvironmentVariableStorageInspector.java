package com.novoda.storagepathfinder;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnvironmentVariableStorageInspector implements SecondaryDeviceStorageInspector {

    private static final StoragePath.Type SECONDARY = StoragePath.Type.SECONDARY;
    @Nullable
    private final String secondaryStoragePath;

    EnvironmentVariableStorageInspector(AndroidSystem system) {
        secondaryStoragePath = system.getEnv("SECONDARY_STORAGE");   // NOT ALL MANUFACTURERS SET THIS....  Also it can be null :(
    }

    // TODO: Not sure what to do about this now that we have base and absolute path.
    @Override
    public List<DeviceStorageRoot> getSecondaryDeviceStorageRoots() {
        ArrayList<DeviceStorageRoot> secondaryStorageRoot = new ArrayList<>(1);
        if (secondaryStoragePath != null) {
            secondaryStorageRoot.add(new DeviceStorageRoot(secondaryStoragePath, secondaryStoragePath));
        }
        return secondaryStorageRoot;
    }

    @Override
    public List<DeviceStorageRoot> getSecondaryDeviceStorageBasePaths() {
        ArrayList<DeviceStorageRoot> secondaryStorageRoot = new ArrayList<>(1);
        if (secondaryStoragePath != null) {
            secondaryStorageRoot.add(new DeviceStorageRoot(secondaryStoragePath, secondaryStoragePath));
        }
        return secondaryStorageRoot;
    }

    @Override
    public List<DeviceStorageRoot> getSecondaryDeviceStorageApplicationPaths() {
        return Collections.emptyList();
    }



    @Override
    public List<StoragePath> getSecondaryDeviceStorageBasePathsss() {
        List<StoragePath> secondaryStorageRoot = new ArrayList<>(1);
        if (secondaryStoragePath != null) {
            secondaryStorageRoot.add(DeviceStoragePath.create(secondaryStoragePath, SECONDARY));
        }
        return secondaryStorageRoot;
    }

    @Override
    public List<StoragePath> getSecondaryDeviceStorageApplicationPathsss() {
        return Collections.emptyList();
    }

}

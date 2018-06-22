package com.novoda.storagepathfinder;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentVariableStorageInspector implements SecondaryDeviceStorageInspector {

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

}

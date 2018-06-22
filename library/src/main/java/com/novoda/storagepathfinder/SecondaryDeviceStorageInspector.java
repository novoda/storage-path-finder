package com.novoda.storagepathfinder;

import java.util.List;

public interface SecondaryDeviceStorageInspector {

    List<DeviceStorageRoot> getSecondaryDeviceStorageRoots();

    List<DeviceStorageRoot> getSecondaryDeviceStorageBasePaths();
    List<DeviceStorageRoot> getSecondaryDeviceStorageApplicationPaths();

    List<StoragePath> getSecondaryDeviceStorageBasePathsss();
    List<StoragePath> getSecondaryDeviceStorageApplicationPathsss();

}

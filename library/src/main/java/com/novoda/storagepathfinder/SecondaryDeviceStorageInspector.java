package com.novoda.storagepathfinder;

import java.util.List;

public interface SecondaryDeviceStorageInspector {

    List<StoragePath> getSecondaryDeviceStorageBasePaths();

    List<StoragePath> getSecondaryDeviceStorageApplicationPaths();
}

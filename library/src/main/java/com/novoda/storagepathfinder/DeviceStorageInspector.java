package com.novoda.storagepathfinder;

import java.util.List;

public interface DeviceStorageInspector {

    List<DeviceStorageRoot> getDeviceStorageRoots();

    StoragePath getPrimaryStorageBasePath();

    StoragePath getSecondaryStorageBasePath();

    StoragePath getPrimaryStorageApplicationPath();

    StoragePath getSecondaryStorageApplicationPath();

}

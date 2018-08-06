package com.novoda.storagepathfinder;

import java.util.List;

interface PrimaryDeviceStorageInspector {

    List<StoragePath> getPrimaryDeviceStorageBasePath();

    List<StoragePath> getPrimaryDeviceStorageApplicationPath();
}

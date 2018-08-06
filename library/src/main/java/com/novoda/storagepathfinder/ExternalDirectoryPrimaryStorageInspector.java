package com.novoda.storagepathfinder;

import java.io.File;
import java.util.Collections;
import java.util.List;

class ExternalDirectoryPrimaryStorageInspector implements PrimaryDeviceStorageInspector {

    private static final StoragePath.Type PRIMARY = StoragePath.Type.PRIMARY;
    private final ExternalStorageDirectories externalStorageDirectories;

    ExternalDirectoryPrimaryStorageInspector(ExternalStorageDirectories externalStorageDirectories) {
        this.externalStorageDirectories = externalStorageDirectories;
    }

    @Override
    public List<StoragePath> getPrimaryDeviceStorageBasePath() {
        File externalStorageDirectoryBasePath = externalStorageDirectories.getExternalStorageDirectoryBasePath();
        if (externalStorageDirectoryBasePath == null) {
            return Collections.emptyList();
        }

        StoragePath storagePath = DeviceStoragePath.create(externalStorageDirectoryBasePath.getPath(), PRIMARY);
        return Collections.singletonList(storagePath);
    }

    @Override
    public List<StoragePath> getPrimaryDeviceStorageApplicationPath() {
        File externalStorageDirectoryApplicationPath = externalStorageDirectories.getExternalStorageDirectoryApplicationPath();
        if (externalStorageDirectoryApplicationPath == null) {
            return Collections.emptyList();
        }

        StoragePath storagePath = DeviceStoragePath.create(externalStorageDirectoryApplicationPath.getPath(), PRIMARY);
        return Collections.singletonList(storagePath);
    }
}

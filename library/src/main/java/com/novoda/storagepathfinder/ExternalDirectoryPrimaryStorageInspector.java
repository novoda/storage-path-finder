package com.novoda.storagepathfinder;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

class ExternalDirectoryPrimaryStorageInspector implements PrimaryDeviceStorageInspector {

    private static final StoragePath.Type PRIMARY = StoragePath.Type.PRIMARY;
    private static final String EMPTY_PATH = "";
    private final ExternalStorageDirectories externalStorageDirectories;

    ExternalDirectoryPrimaryStorageInspector(ExternalStorageDirectories externalStorageDirectories) {
        this.externalStorageDirectories = externalStorageDirectories;
    }

    @Override
    public List<StoragePath> getPrimaryDeviceStorageBasePath() {
        File externalStorageDirectoryBasePath = externalStorageDirectories.getExternalStorageDirectoryBasePath();
        if (externalStorageDirectoryBasePath == null || canonicalOrEmpty(externalStorageDirectoryBasePath).isEmpty()) {
            return Collections.emptyList();
        }

        StoragePath storagePath = DeviceStoragePath.create(externalStorageDirectoryBasePath.getPath(), PRIMARY);
        return Collections.singletonList(storagePath);
    }

    @Override
    public List<StoragePath> getPrimaryDeviceStorageApplicationPath() {
        File externalStorageDirectoryApplicationPath = externalStorageDirectories.getExternalStorageDirectoryApplicationPath();
        if (externalStorageDirectoryApplicationPath == null || canonicalOrEmpty(externalStorageDirectoryApplicationPath).isEmpty()) {
            return Collections.emptyList();
        }

        StoragePath storagePath = DeviceStoragePath.create(externalStorageDirectoryApplicationPath.getPath(), PRIMARY);
        return Collections.singletonList(storagePath);
    }

    private String canonicalOrEmpty(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Could not retrieve canonical path.", e);
        }
        return EMPTY_PATH;
    }
}

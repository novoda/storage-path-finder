package com.novoda.storagepathfinder;

import java.io.File;

public class AndroidFileSystem implements FileSystem {

    private final ExternalStorageDirectories externalStorageDirectories;

    public AndroidFileSystem(ExternalStorageDirectories externalStorageDirectories) {
        this.externalStorageDirectories = externalStorageDirectories;
    }

    @Override
    public boolean exists(File file) {
        return file.exists();
    }

    @Override
    public ExternalStorageDirectories getCommonDirectories() {
        return externalStorageDirectories;
    }
}

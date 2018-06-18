package com.novoda.storagepathfinder;

import java.io.File;

public class AndroidFileSystem implements FileSystem {

    private final CommonDirectories commonDirectories;

    public AndroidFileSystem(CommonDirectories commonDirectories) {
        this.commonDirectories = commonDirectories;
    }

    @Override
    public boolean exists(File file) {
        return file.exists();
    }

    @Override
    public CommonDirectories getCommonDirectories() {
        return commonDirectories;
    }
}

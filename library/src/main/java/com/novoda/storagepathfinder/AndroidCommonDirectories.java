package com.novoda.storagepathfinder;

import android.os.Environment;

import java.io.File;

/**
 * Common directory paths from the Android system
 * I left a few extra ones in here for examples
 * <p>
 * Also interface is a must, as the `Enviroment.` static calls are not to be called in tests.
 */
public class AndroidCommonDirectories implements CommonDirectories {

    @Override
    public boolean isPrimaryStorageRemovable() {
        return Environment.isExternalStorageRemovable();
    }

    @Override
    public boolean isPrimaryStorageMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public File getDataDirectory() {
        return Environment.getDataDirectory();
    }

    @Override
    public File getExternalStorageDirectory() {
        return Environment.getExternalStorageDirectory();
    }
}

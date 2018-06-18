package com.novoda.storagepathfinder;

import java.io.File;

public interface CommonDirectories {

    boolean isPrimaryStorageRemovable();

    boolean isPrimaryStorageMounted();

    File getDataDirectory();

    File getExternalStorageDirectory();
}

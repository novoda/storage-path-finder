package com.novoda.storagepathfinder;

import java.util.List;

public interface DeviceStorageInspector {

    /**
     * The system storage path of your primary storage
     * (this is most likely built in phone storage)
     */
    StoragePath getPrimaryStorageBasePath();

    /**
     * The system storage path of your secondary storage
     * (this is most likely your SD Card)
     */
    List<StoragePath> getSecondaryStorageBasePath();

    /**
     * The application storage path of your primary storage
     * (this is most likely built in phone storage)
     */
    StoragePath getPrimaryStorageApplicationPath();

    /**
     * The application storage path of your secondary storage
     * (this is most likely your SD Card)
     */
    List<StoragePath> getSecondaryStorageApplicationPath();

}

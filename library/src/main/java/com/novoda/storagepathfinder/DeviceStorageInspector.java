package com.novoda.storagepathfinder;

import android.content.Context;

import java.util.List;

/**
 * The inspector that can give all storage roots, both Primary and Secondary. (Internal External and External External)
 * Lots of sanitisation has to go on in these for null checks / empty lists etc.
 * <p>
 * Note: It uses a set of secondary storage inspectors to find it in anyway it can.
 */
public interface DeviceStorageInspector {

    /**
     * The system storage path of your primary storage.
     * (this is most likely built in phone storage)
     */
    StoragePath getPrimaryStorageBasePath();

    /**
     * The system storage path of your secondary storage.
     * (this is most likely your SD Card)
     */
    List<StoragePath> getSecondaryStorageBasePath();

    /**
     * The application storage path of your primary storage.
     * (this is most likely built in phone storage)
     */
    StoragePath getPrimaryStorageApplicationPath();

    /**
     * The application storage path of your secondary storage.
     * (this is most likely your SD Card)
     */
    List<StoragePath> getSecondaryStorageApplicationPath();

    static DeviceStorageInspectorBuilder builder(Context context) {
        return DeviceStorageInspectorBuilder.newInstance(context);
    }

}

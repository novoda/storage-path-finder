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

    static Builder builder(Context context) {
        return Builder.newInstance(context);
    }

    final class Builder {

        private final Context context;
        private FileSystem fileSystem;
        private DeviceFeatures deviceFeatures;
        private AndroidSystem androidSystem;

        private static Builder newInstance(Context context) {
            Context applicationContext = context.getApplicationContext();
            ExternalStorageDirectories externalStorageDirectories = new AndroidExternalStorageDirectories(applicationContext);
            FileSystem fileSystem = new AndroidFileSystem(externalStorageDirectories);
            DeviceFeatures deviceFeatures = new AndroidDeviceFeatures();
            AndroidSystem androidSystem = new AndroidSystem();
            return new Builder(
                    applicationContext,
                    fileSystem,
                    deviceFeatures,
                    androidSystem
            );
        }

        private Builder(Context context, FileSystem fileSystem, DeviceFeatures deviceFeatures, AndroidSystem androidSystem) {
            this.context = context;
            this.fileSystem = fileSystem;
            this.deviceFeatures = deviceFeatures;
            this.androidSystem = androidSystem;
        }

        public Builder withFileSystem(FileSystem fileSystem) {
            this.fileSystem = fileSystem;
            return this;
        }

        public Builder withDeviceFeatures(DeviceFeatures deviceFeatures) {
            this.deviceFeatures = deviceFeatures;
            return this;
        }

        public Builder withAndroidSystem(AndroidSystem androidSystem) {
            this.androidSystem = androidSystem;
            return this;
        }

        public DeviceStorageInspector build() {
            return new AndroidDeviceStorageInspector(
                    context,
                    fileSystem,
                    deviceFeatures,
                    androidSystem
            );
        }
    }

}

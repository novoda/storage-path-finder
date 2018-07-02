package com.novoda.storagepathfinder;

import android.content.Context;

public final class DeviceStorageInspectorBuilder {

    private final Context context;
    private FileSystem fileSystem;
    private DeviceFeatures deviceFeatures;
    private AndroidSystem androidSystem;

    static DeviceStorageInspectorBuilder newInstance(Context context) {
        Context applicationContext = context.getApplicationContext();
        ExternalStorageDirectories externalStorageDirectories = new AndroidExternalStorageDirectories(applicationContext);
        FileSystem fileSystem = new AndroidFileSystem(externalStorageDirectories);
        DeviceFeatures deviceFeatures = new AndroidDeviceFeatures();
        AndroidSystem androidSystem = new AndroidSystem();
        return new DeviceStorageInspectorBuilder(
                applicationContext,
                fileSystem,
                deviceFeatures,
                androidSystem
        );
    }

    private DeviceStorageInspectorBuilder(Context context, FileSystem fileSystem, DeviceFeatures deviceFeatures, AndroidSystem androidSystem) {
        this.context = context;
        this.fileSystem = fileSystem;
        this.deviceFeatures = deviceFeatures;
        this.androidSystem = androidSystem;
    }

    public DeviceStorageInspectorBuilder withFileSystem(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
        return this;
    }

    public DeviceStorageInspectorBuilder withDeviceFeatures(DeviceFeatures deviceFeatures) {
        this.deviceFeatures = deviceFeatures;
        return this;
    }

    public DeviceStorageInspectorBuilder withAndroidSystem(AndroidSystem androidSystem) {
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

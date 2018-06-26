package com.novoda.storagepathfinder;

class ExternalDirectoryPrimaryStorageInspector implements PrimaryDeviceStorageInspector {

    private static final StoragePath.Type PRIMARY = StoragePath.Type.PRIMARY;
    private final ExternalStorageDirectories externalStorageDirectories;

    ExternalDirectoryPrimaryStorageInspector(ExternalStorageDirectories externalStorageDirectories) {
        this.externalStorageDirectories = externalStorageDirectories;
    }

    @Override
    public StoragePath getPrimaryDeviceStorageBasePath() {
        String path = externalStorageDirectories.getExternalStorageDirectoryBasePath().getPath();
        return DeviceStoragePath.create(path, PRIMARY);
    }

    @Override
    public StoragePath getPrimaryDeviceStorageApplicationPath() {
        String path = externalStorageDirectories.getExternalStorageDirectoryApplicationPath().getPath();
        return DeviceStoragePath.create(path, PRIMARY);
    }
}

package com.novoda.storagepathfinder;

class ExternalDirectoryPrimaryStorageInspector implements PrimaryDeviceStorageInspector {

    private static final StoragePath.Type PRIMARY = StoragePath.Type.PRIMARY;
    private CommonDirectories commonDirectories;

    ExternalDirectoryPrimaryStorageInspector(CommonDirectories commonDirectories) {
        this.commonDirectories = commonDirectories;
    }

    @Override
    public StoragePath getPrimaryDeviceStorageBasePath() {
        String path = commonDirectories.getExternalStorageDirectoryBasePath().getPath();
        return DeviceStoragePath.create(path, PRIMARY);
    }

    @Override
    public StoragePath getPrimaryDeviceStorageApplicationPath() {
        String path = commonDirectories.getExternalStorageDirectoryApplicationPath().getPath();
        return DeviceStoragePath.create(path, PRIMARY);
    }
}

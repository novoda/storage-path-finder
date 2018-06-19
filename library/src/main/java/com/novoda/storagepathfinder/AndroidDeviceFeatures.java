package com.novoda.storagepathfinder;

import android.os.Build;

public class AndroidDeviceFeatures implements DeviceFeatures {

    private static final int PLATFORM_VERSION = Build.VERSION.SDK_INT;

    @Override
    public boolean canReportExternalFileDirectories() {
        return !isOlderThanKitKat();
    }

    private boolean isOlderThanKitKat() {
        return PLATFORM_VERSION < Build.VERSION_CODES.KITKAT;
    }
}

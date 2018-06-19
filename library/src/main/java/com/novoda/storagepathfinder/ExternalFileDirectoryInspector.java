package com.novoda.storagepathfinder;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An Secondary (SD Card) path inspector that uses the getExternalFilesDirs() method call introduced in API 19
 */
public class ExternalFileDirectoryInspector implements SecondaryDeviceStorageInspector {

    private static final String ANDROID_PATTERN = "/.?Android/";

    private final Context context;
    private final DeviceFeatures deviceFeatures;
    private final String primaryStoragePath;

    ExternalFileDirectoryInspector(Context context, DeviceFeatures deviceFeatures, String primaryStoragePath) {
        this.context = context;
        this.deviceFeatures = deviceFeatures;
        this.primaryStoragePath = primaryStoragePath;
    }

    @Override
    public List<DeviceStorageRoot> getSecondaryDeviceStorageRoots() {
        return deviceFeatures.canReportExternalFileDirectories() ?
                checkExternalFileDirectoriesForRemovableStorage() : Collections.emptyList();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)    // NO LUCK API 16-18
    private List<DeviceStorageRoot> checkExternalFileDirectoriesForRemovableStorage() {
        File[] externalFileDirectories = filterOutNullValuesFrom(context.getExternalFilesDirs(null));
        List<DeviceStorageRoot> storageRoots = new ArrayList<>();
        storageRoots.addAll(extractSecondaryStorageRootsFrom(externalFileDirectories));
        return storageRoots;
    }

    private File[] filterOutNullValuesFrom(File... externalFilesDirs) {  // YES They return a list with null values in it...
        List<File> fileDirs = new ArrayList<>(externalFilesDirs.length);
        for (File externalFilesDir : externalFilesDirs) {
            if (externalFilesDir != null) {
                fileDirs.add(externalFilesDir);
            }
        }
        File[] dirs = new File[fileDirs.size()];
        return fileDirs.toArray(dirs);
    }

    // This is a small loop and its what we want to do. Only going to ever have 1/2 created
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private List<DeviceStorageRoot> extractSecondaryStorageRootsFrom(File... fileDirectories) {
        List<DeviceStorageRoot> storageRoots = new ArrayList<>();
        for (File file : fileDirectories) {
            String basePath = getDirectoryPathAboveTheAndroidFolderFrom(file);
            if (isValidPathForSecondaryStorage(basePath)) {
                storageRoots.add(new DeviceStorageRoot(basePath, file.getAbsolutePath(), DeviceStorageRoot.Type.SECONDARY));
            }
        }
        return storageRoots;
    }

    private String getDirectoryPathAboveTheAndroidFolderFrom(File file) {
        String path = file.getAbsolutePath().split(ANDROID_PATTERN)[0];
        return path == null ? "" : path;
    }

    private boolean isValidPathForSecondaryStorage(String absolutePath) {
        return !absolutePath.isEmpty() && !absolutePath.equals(primaryStoragePath);
    }
}

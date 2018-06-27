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

    private static final StoragePath.Type SECONDARY = StoragePath.Type.SECONDARY;
    private static final String ANDROID_PATTERN = "/(Android)";

    public enum Filter {   // TODO kill this soon
        APPLICATION,
        BASE
    }

    private final Context context;
    private final DeviceFeatures deviceFeatures;
    private final String primaryStoragePath;

    ExternalFileDirectoryInspector(Context context, DeviceFeatures deviceFeatures, String primaryStoragePath) {
        this.context = context;
        this.deviceFeatures = deviceFeatures;
        this.primaryStoragePath = primaryStoragePath;
    }

    @Override
    public List<StoragePath> getSecondaryDeviceStorageBasePaths() {
        return deviceFeatures.canReportExternalFileDirectories()
                ? checkExternalFileDirectoriesForRemovableStorage(Filter.BASE) : Collections.emptyList();
    }

    @Override
    public List<StoragePath> getSecondaryDeviceStorageApplicationPaths() {
        return deviceFeatures.canReportExternalFileDirectories()
                ? checkExternalFileDirectoriesForRemovableStorage(Filter.APPLICATION) : Collections.emptyList();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private List<StoragePath> checkExternalFileDirectoriesForRemovableStorage(Filter base) {
        File[] externalFileDirectories = filterOutNullValuesFrom(context.getExternalFilesDirs(null));
        List<StoragePath> storageRoots = new ArrayList<>();
        storageRoots.addAll(extractSecondaryStorageRootsFrom(base, externalFileDirectories));
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
    private List<StoragePath> extractSecondaryStorageRootsFrom(Filter base, File... fileDirectories) {
        List<StoragePath> storageRoots = new ArrayList<>();
        for (File file : fileDirectories) {
            // TODO I don't want this Filter.TYPE thing, but its the fastest way to split the logic for now. More refactoring later
            String path = "";
            if (base.equals(Filter.BASE)) {
                path = getDirectoryPathAboveTheAndroidFolderFrom(file);
            }
            if (base.equals(Filter.APPLICATION)) {
                path = file.getAbsolutePath();
            }
            if (isValidPathForSecondaryStorage(path, base)) {
                storageRoots.add(DeviceStoragePath.create(path, SECONDARY));
            }
        }
        return storageRoots;
    }

    private String getDirectoryPathAboveTheAndroidFolderFrom(File file) {
        String path = file.getAbsolutePath().split(ANDROID_PATTERN)[0];
        return path == null ? "" : path;
    }

    private boolean isValidPathForSecondaryStorage(String absolutePath, Filter base) {
        if (base == Filter.APPLICATION) {
            absolutePath = getDirectoryPathAboveTheAndroidFolderFrom(new File(absolutePath));
        }
        return !absolutePath.isEmpty() && !absolutePath.equals(primaryStoragePath);
    }
}

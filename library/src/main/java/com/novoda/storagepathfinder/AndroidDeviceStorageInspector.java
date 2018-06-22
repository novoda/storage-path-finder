package com.novoda.storagepathfinder;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * The inspector that can give all storage roots. both Primary and Secondary. (Internal External and External External)
 * Lots of sanitisation has to go on in these for null checks / empty lists etc.
 * <p>
 * Not it uses a set of secondary storage inspectors to find it in anyway it can.
 */
public class AndroidDeviceStorageInspector implements DeviceStorageInspector {

    private final List<SecondaryDeviceStorageInspector> secondaryStorageInspectors = new ArrayList<>();
    private final FileSystem fileSystem;

    public AndroidDeviceStorageInspector(
            Context context,
            FileSystem fileSystem,
            DeviceFeatures deviceFeatures,
            AndroidSystem androidSystem
    ) {
        this.fileSystem = fileSystem;
        String primaryStoragePath = getPrimaryStoragePath();
        String basePath = getDirectoryPathAboveTheAndroidFolderFrom(primaryStoragePath);    // TODO: try to remove the duplicated code.

        secondaryStorageInspectors.add(new ExternalFileDirectoryInspector(context, deviceFeatures, basePath));
        secondaryStorageInspectors.add(new EnvironmentVariableStorageInspector(androidSystem));
    }

    @Override
    public List<DeviceStorageRoot> getDeviceStorageRoots() {
        List<DeviceStorageRoot> storageRoots = new ArrayList<>(2);

        String primaryStoragePath = getPrimaryStoragePath();
        String basePath = getDirectoryPathAboveTheAndroidFolderFrom(primaryStoragePath);
        DeviceStorageRoot primaryStorageRoot = new DeviceStorageRoot(basePath, getPrimaryStoragePath(), DeviceStorageRoot.Type.PRIMARY);
        storageRoots.add(primaryStorageRoot);

        Set<DeviceStorageRoot> secondaryStorageRoots = findActiveSecondaryStorageRoots();
        storageRoots.addAll(secondaryStorageRoots);

        return Collections.unmodifiableList(storageRoots);
    }

    private void getPrimaryRoot() {
        String primaryStoragePath = getPrimaryStoragePath();
        String basePath = getDirectoryPathAboveTheAndroidFolderFrom(primaryStoragePath);
        DeviceStorageRoot primaryStorageRoot = new DeviceStorageRoot(basePath, getPrimaryStoragePath(), DeviceStorageRoot.Type.PRIMARY);
    }

    private void getSecondaryRoot() {

    }

    @Override
    public StoragePath getPrimaryStorageBasePath() {
        getPrimaryRoot();
        // An object, you can get THE path, type, and in different formats
        return null;
    }

    @Override
    public StoragePath getSecondaryStorageBasePath() {
        getSecondaryRoot();
        return null;
    }

    @Override
    public StoragePath getPrimaryStorageApplicationPath() {
        return null;
    }

    @Override
    public StoragePath getSecondaryStorageApplicationPath() {
        return null;
    }

    // getExternalStorageDirectory() is the Internal External   (NOT SD CARD)
    private String getPrimaryStoragePath() {
        return fileSystem.getCommonDirectories().getExternalStorageDirectory().getPath();
    }

    private String getDirectoryPathAboveTheAndroidFolderFrom(String path) {
        String basePath = path.split("/.?Android/")[0];
        return basePath == null ? "" : basePath;
    }

    private Set<DeviceStorageRoot> findActiveSecondaryStorageRoots() {
        return filterToOnlyRootsThatExist(findAllSecondaryStorageRoots());
    }

    private Set<DeviceStorageRoot> findAllSecondaryStorageRoots() {
        Set<DeviceStorageRoot> roots = new TreeSet<>(withStorageRootPathComparator());
        for (SecondaryDeviceStorageInspector inspector : secondaryStorageInspectors) {
            roots.addAll(inspector.getSecondaryDeviceStorageRoots());
        }
        return roots;
    }

    private Set<DeviceStorageRoot> filterToOnlyRootsThatExist(Set<DeviceStorageRoot> allRoots) {
        Set<DeviceStorageRoot> filteredRoots = new HashSet<>();
        for (DeviceStorageRoot root : allRoots) {
            if (fileSystem.exists(root.asFile())) {
                filteredRoots.add(root);
            }
        }
        return filteredRoots;
    }

    private Comparator<DeviceStorageRoot> withStorageRootPathComparator() {
        return (lhs, rhs) -> {
            if (lhs.getBasePath().equals(rhs.getBasePath())) {
                return 0;
            }
            return lhs.hashCode() - rhs.hashCode();
        };
    }
}

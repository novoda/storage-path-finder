package com.novoda.storagepathfinder;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

class AndroidDeviceStorageInspector implements DeviceStorageInspector {

    private final PrimaryDeviceStorageInspector primaryStorageInspector;
    private final List<SecondaryDeviceStorageInspector> secondaryStorageInspectors = new ArrayList<>();
    private final FileSystem fileSystem;

    AndroidDeviceStorageInspector(
            Context context,
            FileSystem fileSystem,
            DeviceFeatures deviceFeatures,
            AndroidSystem androidSystem
    ) {
        this.fileSystem = fileSystem;
        ExternalStorageDirectories externalStorageDirectories = fileSystem.getCommonDirectories();
        primaryStorageInspector = new ExternalDirectoryPrimaryStorageInspector(externalStorageDirectories);

        List<StoragePath> primaryBasePaths = primaryStorageInspector.getPrimaryDeviceStorageBasePath();
        secondaryStorageInspectors.add(new EnvironmentVariableStorageInspector(androidSystem));
        secondaryStorageInspectors.add(new ExternalFileDirectoryInspector(context, deviceFeatures, primaryBasePaths));
    }

    @Override
    public List<StoragePath> getPrimaryStorageBasePath() {
        return primaryStorageInspector.getPrimaryDeviceStorageBasePath();

    }

    @Override
    public List<StoragePath> getPrimaryStorageApplicationPath() {
        return primaryStorageInspector.getPrimaryDeviceStorageApplicationPath();
    }

    @Override
    public List<StoragePath> getSecondaryStorageBasePath() {
        List<StoragePath> storageRoots = new ArrayList<>(findActiveSecondaryStorageBasePaths());
        return Collections.unmodifiableList(storageRoots);
    }

    @Override
    public List<StoragePath> getSecondaryStorageApplicationPath() {
        List<StoragePath> storageRoots = new ArrayList<>(findActiveSecondaryStorageApplicationPaths());
        return Collections.unmodifiableList(storageRoots);
    }

    private Set<StoragePath> findActiveSecondaryStorageBasePaths() {
        return filterToOnlyRootsThatExist(findAllSecondaryStorageBasePaths());
    }

    private Set<StoragePath> findAllSecondaryStorageBasePaths() {
        Set<StoragePath> paths = new TreeSet<>(withStorageRootPathComparator());
        for (SecondaryDeviceStorageInspector inspector : secondaryStorageInspectors) {
            paths.addAll(inspector.getSecondaryDeviceStorageBasePaths());
        }
        return paths;
    }

    private Set<StoragePath> findActiveSecondaryStorageApplicationPaths() {
        return filterToOnlyRootsThatExist(findAllSecondaryStorageApplicationPaths());
    }

    private Set<StoragePath> findAllSecondaryStorageApplicationPaths() {
        Set<StoragePath> paths = new TreeSet<>(withStorageRootPathComparator());
        for (SecondaryDeviceStorageInspector inspector : secondaryStorageInspectors) {
            paths.addAll(inspector.getSecondaryDeviceStorageApplicationPaths());
        }
        return paths;
    }

    private Set<StoragePath> filterToOnlyRootsThatExist(Set<StoragePath> allPaths) {
        Set<StoragePath> filteredPaths = new HashSet<>();
        for (StoragePath path : allPaths) {
            if (fileSystem.exists(path.getPathAsFile())) {
                filteredPaths.add(path);
            }
        }
        return filteredPaths;
    }

    private Comparator<StoragePath> withStorageRootPathComparator() {
        return (lhs, rhs) -> {
            if (lhs.getPathAsString().equals(rhs.getPathAsString())) {
                return 0;
            }
            return lhs.hashCode() - rhs.hashCode();
        };
    }
}

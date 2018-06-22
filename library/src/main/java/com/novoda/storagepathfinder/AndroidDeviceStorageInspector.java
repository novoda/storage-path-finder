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

    private final PrimaryDeviceStorageInspector primaryStorageInspector;
    private final List<SecondaryDeviceStorageInspector> secondaryStorageInspectors = new ArrayList<>();
    private final FileSystem fileSystem;

    public AndroidDeviceStorageInspector(
            Context context,
            FileSystem fileSystem,
            DeviceFeatures deviceFeatures,
            AndroidSystem androidSystem
    ) {
        this.fileSystem = fileSystem;
        CommonDirectories commonDirectories = fileSystem.getCommonDirectories();
        primaryStorageInspector = new ExternalDirectoryPrimaryStorageInspector(commonDirectories);

        StoragePath primaryBasePath = primaryStorageInspector.getPrimaryDeviceStorageBasePath();
        secondaryStorageInspectors.add(new ExternalFileDirectoryInspector(context, deviceFeatures, primaryBasePath.asString()));
        secondaryStorageInspectors.add(new EnvironmentVariableStorageInspector(androidSystem));
    }

    @Override
    public StoragePath getPrimaryStorageBasePath() {
        return primaryStorageInspector.getPrimaryDeviceStorageBasePath();

    }

    @Override
    public StoragePath getPrimaryStorageApplicationPath() {
        return primaryStorageInspector.getPrimaryDeviceStorageApplicationPath();
    }

    @Override
    public List<StoragePath> getSecondaryStorageBasePath() {
        List<StoragePath> storageRoots = new ArrayList<>(2);
        Set<StoragePath> secondaryStorageRoots = findActiveSecondaryStorageBasePaths();
        storageRoots.addAll(secondaryStorageRoots);
        return Collections.unmodifiableList(storageRoots);
    }

    @Override
    public List<StoragePath> getSecondaryStorageApplicationPath() {
        List<StoragePath> storageRoots = new ArrayList<>(2);
        Set<StoragePath> secondaryStorageRoots = findActiveSecondaryStorageApplicationPaths();
        storageRoots.addAll(secondaryStorageRoots);
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
        return filterToOnlyRootsThatExist(findAllSecondaryStorageApplicaitonPaths());
    }

    private Set<StoragePath> findAllSecondaryStorageApplicaitonPaths() {
        Set<StoragePath> paths = new TreeSet<>(withStorageRootPathComparator());
        for (SecondaryDeviceStorageInspector inspector : secondaryStorageInspectors) {
            paths.addAll(inspector.getSecondaryDeviceStorageApplicationPaths());
        }
        return paths;
    }

    private Set<StoragePath> filterToOnlyRootsThatExist(Set<StoragePath> allPaths) {
        Set<StoragePath> filteredPaths = new HashSet<>();
        for (StoragePath path : allPaths) {
            if (fileSystem.exists(path.asFile())) {
                filteredPaths.add(path);
            }
        }
        return filteredPaths;
    }

    private Comparator<StoragePath> withStorageRootPathComparator() {
        return (lhs, rhs) -> {
            if (lhs.asString().equals(rhs.asString())) {
                return 0;
            }
            return lhs.hashCode() - rhs.hashCode();
        };
    }
}

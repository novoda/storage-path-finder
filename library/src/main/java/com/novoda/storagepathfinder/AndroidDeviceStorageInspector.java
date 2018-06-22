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
        List<StoragePath> secondaryStoragePaths = new ArrayList<>(1);
        List<DeviceStorageRoot> secondaryRoots = getSecondaryRoots();
        for (DeviceStorageRoot secondaryRoot : secondaryRoots) {
            secondaryStoragePaths.add(DeviceStoragePath.create(secondaryRoot.getBasePath(), StoragePath.Type.SECONDARY));
        }
        return Collections.unmodifiableList(secondaryStoragePaths);
    }

    @Override
    public List<StoragePath> getSecondaryStorageApplicationPath() {
        List<StoragePath> secondaryStoragePaths = new ArrayList<>(1);
        List<DeviceStorageRoot> secondaryRoots = getSecondaryRoots();
        for (DeviceStorageRoot secondaryRoot : secondaryRoots) {
            secondaryStoragePaths.add(DeviceStoragePath.create(secondaryRoot.getApplicationPath(), StoragePath.Type.SECONDARY));
        }
        return Collections.unmodifiableList(secondaryStoragePaths);
    }

    private List<DeviceStorageRoot> getSecondaryRoots() {
        List<DeviceStorageRoot> storageRoots = new ArrayList<>(2);
        Set<DeviceStorageRoot> secondaryStorageRoots = findActiveSecondaryStorageRoots();
        storageRoots.addAll(secondaryStorageRoots);
        return Collections.unmodifiableList(storageRoots);
    }

    // TODO all below needs to be considered of where it belongs?
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
            if (fileSystem.exists(root.basePathAsFile())) {
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

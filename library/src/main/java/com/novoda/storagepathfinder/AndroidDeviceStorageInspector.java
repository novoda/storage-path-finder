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
        ExternalStorageDirectories externalStorageDirectories = fileSystem.getCommonDirectories();
        primaryStorageInspector = new ExternalDirectoryPrimaryStorageInspector(externalStorageDirectories);

        StoragePath primaryBasePath = primaryStorageInspector.getPrimaryDeviceStorageBasePath();
        secondaryStorageInspectors.add(new EnvironmentVariableStorageInspector(androidSystem));
        secondaryStorageInspectors.add(new ExternalFileDirectoryInspector(context, deviceFeatures, primaryBasePath.getPathAsString()));
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

    public static Builder builder(Context context) {
        return Builder.newInstance(context);
    }

    public static class Builder {

        private Context context;
        private FileSystem fileSystem;
        private DeviceFeatures deviceFeatures;
        private AndroidSystem androidSystem;

        private static Builder newInstance(Context context) {
            Context applicationContext = context.getApplicationContext();
            ExternalStorageDirectories externalStorageDirectories = new AndroidExternalStorageDirectories(applicationContext);
            FileSystem fileSystem = new AndroidFileSystem(externalStorageDirectories);
            DeviceFeatures deviceFeatures = new AndroidDeviceFeatures();
            AndroidSystem androidSystem = new AndroidSystem();
            return new Builder(
                applicationContext,
                fileSystem,
                deviceFeatures,
                androidSystem
            );
        }

        private Builder(Context context, FileSystem fileSystem, DeviceFeatures deviceFeatures, AndroidSystem androidSystem) {
            this.context = context;
            this.fileSystem = fileSystem;
            this.deviceFeatures = deviceFeatures;
            this.androidSystem = androidSystem;
        }

        public Builder withFileSystem(FileSystem fileSystem) {
            this.fileSystem = fileSystem;
            return this;
        }

        public Builder withDeviceFeatures(DeviceFeatures deviceFeatures) {
            this.deviceFeatures = deviceFeatures;
            return this;
        }

        public Builder withAndroidSystem(AndroidSystem androidSystem) {
            this.androidSystem = androidSystem;
            return this;
        }

        public AndroidDeviceStorageInspector build() {
            return new AndroidDeviceStorageInspector(
                context,
                fileSystem,
                deviceFeatures,
                androidSystem
            );
        }
    }
}

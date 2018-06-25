package com.novoda.storagepathfinder;

import java.util.List;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class EnvironmentVariableStorageInspectorTest {

    private static final StoragePath SECONDARY_STORAGE_PATH = DeviceStoragePath.create("storage/MicroSD", StoragePath.Type.SECONDARY);
    private static final String NO_SECONDARY_STORAGE_PATH = null;

    @Test
    public void returnsSecondaryDeviceStorageBasePathsWhenEnvironmentVariableIsAvailable() {
        EnvironmentVariableStorageInspector storageInspector = createEnvironmentStorageInspectorWithPath(SECONDARY_STORAGE_PATH.getPathAsString());

        List<StoragePath> secondaryDeviceStorageRoots = storageInspector.getSecondaryDeviceStorageBasePaths();

        assertThat(secondaryDeviceStorageRoots.contains(SECONDARY_STORAGE_PATH)).isTrue();
    }

    @Test
    public void returnsNoSecondaryDeviceStorageBasePathsWhenEnvironmentVariableIsNotAvailable() {
        EnvironmentVariableStorageInspector storageInspector = createEnvironmentStorageInspectorWithPath(NO_SECONDARY_STORAGE_PATH);

        List<StoragePath> secondaryDeviceStorageRoots = storageInspector.getSecondaryDeviceStorageBasePaths();

        assertThat(secondaryDeviceStorageRoots.isEmpty()).isTrue();
    }

    @Test
    public void returnsNoSecondaryStorageApplicationPathsWhenEnvironmentVariableIsAvailable() {
        EnvironmentVariableStorageInspector storageInspector = createEnvironmentStorageInspectorWithPath(SECONDARY_STORAGE_PATH.getPathAsString());

        List<StoragePath> secondaryDeviceStorageRoots = storageInspector.getSecondaryDeviceStorageApplicationPaths();

        assertThat(secondaryDeviceStorageRoots.isEmpty()).isTrue();
    }

    @Test
    public void returnsNoSecondaryStorageApplicationPathsWhenEnvironmentVariableIsNotAvailable() {
        EnvironmentVariableStorageInspector storageInspector = createEnvironmentStorageInspectorWithPath(NO_SECONDARY_STORAGE_PATH);

        List<StoragePath> secondaryDeviceStorageRoots = storageInspector.getSecondaryDeviceStorageApplicationPaths();

        assertThat(secondaryDeviceStorageRoots.isEmpty()).isTrue();
    }

    private static EnvironmentVariableStorageInspector createEnvironmentStorageInspectorWithPath(String storagePath) {
        AndroidSystem androidSystem = mock(AndroidSystem.class);
        given(androidSystem.getEnv(anyString())).willReturn(storagePath);
        return new EnvironmentVariableStorageInspector(androidSystem);
    }
}

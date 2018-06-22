package com.novoda.storagepathfinder;

import org.junit.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class EnvironmentVariableStorageInspectorTest {

    private static final String SECONDARY_STORAGE_PATH = "storage/MicroSD";
    private static final String NO_SECONDARY_STORAGE_PATH = null;

    //TODO to fix...
    @Test
    public void returnsSecondaryDeviceStorageRoots() {
//        EnvironmentVariableStorageInspector storageInspector = createEnvironmentStorageInspectorWithPath(SECONDARY_STORAGE_PATH);
//
//        List<DeviceStorageRoot> secondaryDeviceStorageRoots = storageInspector.getSecondaryDeviceStorageRoots();
//
//        List<DeviceStorageRoot> expectedRoots = Collections.singletonList(
//                new DeviceStorageRoot(SECONDARY_STORAGE_PATH, SECONDARY_STORAGE_PATH, DeviceStorageRoot.Type.SECONDARY)
//        );
//        assertThat(secondaryDeviceStorageRoots).isEqualTo(expectedRoots);
    }

    @Test
    public void returnsNoSecondaryDeviceStorageRoots() {
//        EnvironmentVariableStorageInspector storageInspector = createEnvironmentStorageInspectorWithPath(NO_SECONDARY_STORAGE_PATH);
//
//        List<DeviceStorageRoot> secondaryDeviceStorageRoots = storageInspector.getSecondaryDeviceStorageRoots();
//
//        assertThat(secondaryDeviceStorageRoots).isEmpty();
    }

    private static EnvironmentVariableStorageInspector createEnvironmentStorageInspectorWithPath(String storagePath) {
        AndroidSystem androidSystem = mock(AndroidSystem.class);
        given(androidSystem.getEnv(anyString())).willReturn(storagePath);
        return new EnvironmentVariableStorageInspector(androidSystem);
    }
}

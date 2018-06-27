package com.novoda.storagepathfinder;

import android.content.Context;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.novoda.storagepathfinder.StoragePath.Type.PRIMARY;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ExternalFileDirectoryInspectorTest {

    private static final String PRIMARY_STORAGE_BASE_PATH_STRING = "/primary-storage-base-path";
    private static final StoragePath PRIMARY_STORAGE_BASE_PATH = DeviceStoragePath.create(PRIMARY_STORAGE_BASE_PATH_STRING, PRIMARY);
    private static final StoragePath.Type SECONDARY = StoragePath.Type.SECONDARY;
    private static final StoragePath SECONDARY_BATH_PATH_1 = DeviceStoragePath.create("secondary-base-path-1", SECONDARY);
    private static final StoragePath SECONDARY_APPLICATION_PATH_1 = DeviceStoragePath.create("secondary-appplication-path-1", SECONDARY);

    private Context context = mock(Context.class);
    private DeviceFeatures deviceFeatures = mock(DeviceFeatures.class);

    private ExternalFileDirectoryInspector inspector;

    @Before
    public void setUp() {
        inspector = new ExternalFileDirectoryInspector(context, deviceFeatures, PRIMARY_STORAGE_BASE_PATH_STRING);
    }

    @Test
    public void returnsBasePath_whenDeviceHasSecondaryStorage_andFileDirectoriesAreAvailable() {
        given(deviceFeatures.canReportExternalFileDirectories()).willReturn(true);
        givenExternalFileDirectoriesWillReturnPaths(PRIMARY_STORAGE_BASE_PATH, SECONDARY_BATH_PATH_1);

        List<StoragePath> storagePaths = inspector.getSecondaryDeviceStorageBasePaths();

        assertThat(storagePaths.size()).isEqualTo(1);
        assertThat(storagePaths.contains(SECONDARY_BATH_PATH_1));
    }

    @Test
    public void returnsNoBasePaths_whenDeviceHasSecondaryStorage_andFileDirectoriesAreNotAvailable() {
        given(deviceFeatures.canReportExternalFileDirectories()).willReturn(false);
        givenExternalFileDirectoriesWillReturnPaths(PRIMARY_STORAGE_BASE_PATH, SECONDARY_BATH_PATH_1);

        List<StoragePath> storagePaths = inspector.getSecondaryDeviceStorageBasePaths();

        assertThat(storagePaths.size()).isEqualTo(0);
    }

    @Test
    public void returnsApplicationPath_whenDeviceHasSecondaryStorage_andFileDirectoriesAreAvailable() {
        given(deviceFeatures.canReportExternalFileDirectories()).willReturn(true);
        givenExternalFileDirectoriesWillReturnPaths(PRIMARY_STORAGE_BASE_PATH, SECONDARY_APPLICATION_PATH_1);

        List<StoragePath> storagePaths = inspector.getSecondaryDeviceStorageApplicationPaths();

        assertThat(storagePaths.size()).isEqualTo(1);
        assertThat(storagePaths.contains(SECONDARY_APPLICATION_PATH_1));
    }

    @Test
    public void returnsNoApplicationPaths_whenDeviceHasSecondaryStorage_andFileDirectoriesAreNotAvailable() {
        given(deviceFeatures.canReportExternalFileDirectories()).willReturn(false);
        givenExternalFileDirectoriesWillReturnPaths(PRIMARY_STORAGE_BASE_PATH, SECONDARY_APPLICATION_PATH_1);

        List<StoragePath> storagePaths = inspector.getSecondaryDeviceStorageApplicationPaths();

        assertThat(storagePaths.size()).isEqualTo(0);
    }

    @Test
    public void returnsNoApplicationPaths_whenDeviceHasNoSecondaryStorage_andFileDirectoriesAreAvailable() {
        given(deviceFeatures.canReportExternalFileDirectories()).willReturn(true);
        givenExternalFileDirectoriesWillReturnPaths(PRIMARY_STORAGE_BASE_PATH);

        List<StoragePath> storagePaths = inspector.getSecondaryDeviceStorageApplicationPaths();

        assertThat(storagePaths.size()).isEqualTo(0);
    }

    @Test
    public void returnsNoBasePaths_whenDeviceHasNoSecondaryStorage_andFileDirectoriesAreAvailable() {
        given(deviceFeatures.canReportExternalFileDirectories()).willReturn(true);
        givenExternalFileDirectoriesWillReturnPaths(PRIMARY_STORAGE_BASE_PATH);

        List<StoragePath> storagePaths = inspector.getSecondaryDeviceStorageBasePaths();

        assertThat(storagePaths.size()).isEqualTo(0);
    }

    private void givenExternalFileDirectoriesWillReturnPaths(StoragePath... storagePaths) {
        File[] files = new File[storagePaths.length];
        for (int i = 0; i < files.length; i++) {
            files[i] = storagePaths[i].getPathAsFile();
        }
        given(context.getExternalFilesDirs(null)).willReturn(files);
    }

}

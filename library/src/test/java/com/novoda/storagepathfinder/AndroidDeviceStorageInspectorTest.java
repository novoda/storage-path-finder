package com.novoda.storagepathfinder;

import android.content.Context;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

public class AndroidDeviceStorageInspectorTest {

    private static final StoragePath.Type PRIMARY = StoragePath.Type.PRIMARY;
    private static final StoragePath.Type SECONDARY = StoragePath.Type.SECONDARY;

    private static final StoragePath PRIMARY_BASE_PATH = DeviceStoragePath.create("/primary-base-path", PRIMARY);
    private static final StoragePath PRIMARY_APPLICATION_PATH = DeviceStoragePath.create("/primary-application-path", PRIMARY);

    private static final StoragePath SECONDARY_BASE_PATH_1 = DeviceStoragePath.create("/secondary-base-path-1", SECONDARY);
    private static final StoragePath SECONDARY_BASE_PATH_2 = DeviceStoragePath.create("/secondary-base-path-2", SECONDARY);
    private static final StoragePath SECONDARY_APPLICATION_PATH_1 = DeviceStoragePath.create("/secondary-application-path-1", SECONDARY);

    private final Context context = mock(Context.class);
    private final AndroidSystem androidSystem = mock(AndroidSystem.class);
    private final FileSystem filesystem = mock(FileSystem.class);
    private final DeviceFeatures devicesFeatures = mock(DeviceFeatures.class);

    private final CommonDirectories commonDirectories = new CommonDirectories() {
        @Override
        public File getExternalStorageDirectoryBasePath() {
            return PRIMARY_BASE_PATH.getPathAsFile();
        }

        @Override
        public File getExternalStorageDirectoryApplicationPath() {
            return PRIMARY_APPLICATION_PATH.getPathAsFile();
        }
    };

    private DeviceStorageInspector storageInspector;

    @Before
    public void setup() {
        given(filesystem.getCommonDirectories()).willReturn(commonDirectories);
        given(androidSystem.getEnv(anyString())).willReturn(SECONDARY_BASE_PATH_1.getPathAsString());

        storageInspector = new AndroidDeviceStorageInspector(context, filesystem, devicesFeatures, androidSystem);
    }

    @Test
    public void canGetThePrimaryStorageBasePath() {
        StoragePath basePath = storageInspector.getPrimaryStorageBasePath();

        assertThat(basePath).isEqualTo(PRIMARY_BASE_PATH);
    }

    @Test
    public void canGetThePrimaryStorageApplicationPath() {
        StoragePath basePath = storageInspector.getPrimaryStorageApplicationPath();

        assertThat(basePath).isEqualTo(PRIMARY_APPLICATION_PATH);
    }

    @Test
    public void canGetASecondaryStorageBasePathFromTheExternalDirectoryInspector() {
        givenFileSystemHasPath(SECONDARY_BASE_PATH_1);
        givenDeviceCanReportExternalFileDirectories();
        givenExternalFileDirectoriesWillReturnPaths(SECONDARY_BASE_PATH_1);

        List<StoragePath> storagePaths = storageInspector.getSecondaryStorageBasePath();

        assertThat(storagePaths.size()).isEqualTo(1);
        assertThat(storagePaths.get(0)).isEqualTo(SECONDARY_BASE_PATH_1);
    }

    @Test
    public void canGetASecondaryStorageApplicationPathFromTheExternalDirectoryInspector() {
        givenDeviceCanReportExternalFileDirectories();
        givenFileSystemHasPath(SECONDARY_APPLICATION_PATH_1);
        givenExternalFileDirectoriesWillReturnPaths(SECONDARY_APPLICATION_PATH_1);

        List<StoragePath> storagePaths = storageInspector.getSecondaryStorageApplicationPath();

        assertThat(storagePaths.size()).isEqualTo(1);
        assertThat(storagePaths.get(0)).isEqualTo(SECONDARY_APPLICATION_PATH_1);
    }

    @Test
    public void canGetASecondaryStorageBasePathFromTheEnvironmentVariableInspector() {
        givenFileSystemHasPath(SECONDARY_BASE_PATH_1);

        List<StoragePath> storagePaths = storageInspector.getSecondaryStorageBasePath();

        assertThat(storagePaths.size()).isEqualTo(1);
        assertThat(storagePaths.get(0)).isEqualTo(SECONDARY_BASE_PATH_1);
    }

    @Test
    public void noSecondaryStorageApplicationPathFromTheEnvironmentVariableInspector() {
        givenFileSystemHasPath(SECONDARY_APPLICATION_PATH_1);

        List<StoragePath> storagePaths = storageInspector.getSecondaryStorageApplicationPath();

        assertThat(storagePaths.size()).isEqualTo(0);
    }

    @Test
    public void canHandleDevicesWithMultipleSecondaryStoragePaths() {
        givenFileSystemHasPath(SECONDARY_BASE_PATH_1);
        givenFileSystemHasPath(SECONDARY_BASE_PATH_2);

        givenDeviceCanReportExternalFileDirectories();
        givenExternalFileDirectoriesWillReturnPaths(SECONDARY_BASE_PATH_1, SECONDARY_BASE_PATH_2);

        List<StoragePath> storagePaths = storageInspector.getSecondaryStorageBasePath();

        assertThat(storagePaths.size()).isEqualTo(2);
        assertThat(storagePaths.contains(SECONDARY_BASE_PATH_1)).isEqualTo(true);
        assertThat(storagePaths.contains(SECONDARY_BASE_PATH_2)).isEqualTo(true);
    }

    private void givenExternalFileDirectoriesWillReturnPaths(StoragePath... storagePaths) {
        File[] files = new File[storagePaths.length];
        for (int i = 0; i < files.length; i++) {
            files[i] = storagePaths[i].getPathAsFile();
        }
        given(context.getExternalFilesDirs(null)).willReturn(files);
    }

    private void givenDeviceCanReportExternalFileDirectories() {
        given(devicesFeatures.canReportExternalFileDirectories()).willReturn(true);
    }

    private void givenFileSystemHasPath(StoragePath storagePath) {
        given(filesystem.exists(storagePath.getPathAsFile())).willReturn(true);
    }

}

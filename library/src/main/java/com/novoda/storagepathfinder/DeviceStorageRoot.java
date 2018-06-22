package com.novoda.storagepathfinder;

import java.io.File;

/**
 * Wrapper around the base and application Path strings for a Storage root.
 */
public class DeviceStorageRoot {

    private final String basePath;
    private final String applicationPath;

    DeviceStorageRoot(String basePath, String applicationPath) {
        this.basePath = basePath;
        this.applicationPath = applicationPath;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getApplicationPath() {
        return applicationPath;
    }

    public File basePathAsFile(){
        return new File(basePath);
    }

}

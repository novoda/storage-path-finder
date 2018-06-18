package com.novoda.storagepathfinder;

import java.io.File;

/**
 *   Useful to wrap the file system if we want to add unit tests around this.
 */
public interface FileSystem {

    boolean exists(File file);

    CommonDirectories getCommonDirectories();
}

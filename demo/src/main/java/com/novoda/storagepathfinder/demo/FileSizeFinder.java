package com.novoda.storagepathfinder.demo;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

final class FileSizeFinder {

    private FileSizeFinder() {
        // Uses static factory methods.
    }

    /**
     * Counts the size of a directory recursively (sum of the length of all files).
     *
     * @param directory directory to inspect, must not be {@code null}
     * @return size of directory in bytes, 0 if directory is security restricted, a negative number when the real total
     * is greater than {@link Long#MAX_VALUE}.
     * @throws NullPointerException if the directory is {@code null}
     */
    static long sizeOfDirectory(File directory) {
        File[] files = directory.listFiles();

        if (files == null) {
            return 0L;
        }

        long size = 0;

        for (final File file : files) {
            try {
                if (!FileUtils.isSymlink(file)) {
                    size += sizeOf(file);
                }
            } catch (IOException exception) {
                Log.e(FileSizeFinder.class.getSimpleName(), "Ignoring exceptions caught from symlink.", exception);
            }
        }
        return size;
    }

    /**
     * Returns the size of the specified file or directory. If the provided
     * {@link File} is a regular file, then the file's length is returned.
     * If the argument is a directory, then the size of the directory is
     * calculated recursively. If a directory or subdirectory is security
     * restricted, its size will not be included.
     *
     * @param file the regular file or directory to return the size
     *             of (must not be {@code null}).
     * @return the length of the file, or recursive size of the directory,
     * provided (in bytes).
     * @throws NullPointerException if the file is {@code null}
     */
    private static long sizeOf(@NonNull File file) {
        if (file.isDirectory()) {
            return sizeOfDirectory(file);
        } else {
            return file.length();
        }
    }
}

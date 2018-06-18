package com.novoda.storagepathfinder;

import java.io.File;

/**
 * Wrapper around a string for a Storage root.
 */
public class DeviceStorageRoot {

    public enum Type {
        PRIMARY,
        SECONDARY
    }

    private final String basePath;
    private final String absolutePath;
    private final Type type;

    DeviceStorageRoot(String basePath, String absolutePath, Type type) {
        this.basePath = basePath;
        this.absolutePath = absolutePath;
        this.type = type;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public Type getType() {
        return type;
    }

    public File asFile() {
        return new File(absolutePath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeviceStorageRoot that = (DeviceStorageRoot) o;

        if (basePath != null ? !basePath.equals(that.basePath) : that.basePath != null) {
            return false;
        }
        if (absolutePath != null ? !absolutePath.equals(that.absolutePath) : that.absolutePath != null) {
            return false;
        }
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = basePath != null ? basePath.hashCode() : 0;
        result = 31 * result + (absolutePath != null ? absolutePath.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DeviceStorageRoot{"
                + "basePath='" + basePath + '\''
                + ", absolutePath='" + absolutePath + '\''
                + ", type=" + type
                + '}';
    }
}

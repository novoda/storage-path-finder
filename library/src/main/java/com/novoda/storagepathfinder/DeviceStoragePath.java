package com.novoda.storagepathfinder;

import java.io.File;

/**
 * Wrapper around a string for a Storage path with a type.
 */
class DeviceStoragePath implements StoragePath {

    public static StoragePath create(String path, DeviceStoragePath.Type type) {
        return new DeviceStoragePath(path, type);
    }

    private final String path;
    private final Type type;

    private DeviceStoragePath(String path, Type type) {
        this.path = path;
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String asString() {
        return path;
    }

    @Override
    public File asFile() {
        return new File(path);
    }

    @Override
    public boolean equals(Object objectComparingTo) {
        if (this == objectComparingTo) {
            return true;
        }
        if (objectComparingTo == null || getClass() != objectComparingTo.getClass()) {
            return false;
        }

        DeviceStoragePath that = (DeviceStoragePath) objectComparingTo;

        if (path != null ? !path.equals(that.path) : that.path != null) {
            return false;
        }
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DeviceStorageRoot{"
                + "path='" + path + '\''
                + ", type=" + type
                + '}';
    }
}

package com.novoda.storagepathfinder;

import java.io.File;

/**
 * Wrapper around a string for a Storage root.
 */
class DeviceStoragePath implements StoragePath {

    public enum Type {
        PRIMARY,
        SECONDARY
    }

    private String path;
    private final Type type;

    DeviceStoragePath(String path, Type type) {
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

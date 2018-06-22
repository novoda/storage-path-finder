package com.novoda.storagepathfinder;

import java.io.File;

public interface StoragePath {

    DeviceStoragePath.Type getType();

    String asString();

    File asFile();
}

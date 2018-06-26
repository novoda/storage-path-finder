package com.novoda.storagepathfinder;

import java.io.File;

public interface StoragePath {

    String getPathAsString();

    File getPathAsFile();

    Type getType();

    enum Type {
        PRIMARY,
        SECONDARY
    }

}

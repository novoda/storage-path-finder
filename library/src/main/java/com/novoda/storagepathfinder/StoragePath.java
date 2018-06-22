package com.novoda.storagepathfinder;

import java.io.File;

public interface StoragePath {

    String asString();

    File asFile();

    Type getType();

    enum Type {
        PRIMARY,
        SECONDARY
    }

}

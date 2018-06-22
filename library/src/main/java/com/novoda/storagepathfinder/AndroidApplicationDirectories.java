package com.novoda.storagepathfinder;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import java.io.File;

/**
 * Common directory paths from the Android system
 * I left a few extra ones in here for examples
 * <p>
 * Also interface is a must, as the `Enviroment.` static calls are not to be called in tests.
 */
public class AndroidApplicationDirectories implements CommonDirectories {

    private final Context context;

    public AndroidApplicationDirectories(Context context) {
        this.context = context;
    }

    @Override
    public File getExternalStorageDirectoryApplicationPath() {
        return ContextCompat.getExternalFilesDirs(context, null)[0];
    }

    @Override
    public File getExternalStorageDirectoryBasePath() {
        return Environment.getExternalStorageDirectory();
    }
}

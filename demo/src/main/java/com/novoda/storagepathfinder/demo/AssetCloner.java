package com.novoda.storagepathfinder.demo;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class AssetCloner {

    private static final int BUFFER_SIZE = 1024;
    private static final byte[] BUFFER = new byte[BUFFER_SIZE];

    private final AssetManager assetManager;

    AssetCloner(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void cloneAsset(String assetName, String pathToCloneTo) {
        File outputFile = new File(pathToCloneTo);
        createFileIfDoesNotExist(outputFile);
        copyAssetToFile(assetName, outputFile);
    }

    private void createFileIfDoesNotExist(File outputFile) {
        boolean parentPathDoesNotExist = !outputFile.getParentFile().exists();
        if (parentPathDoesNotExist) {
            Log.w(getClass().getSimpleName(), String.format("path: %s doesn't exist, creating parent directories...", outputFile.getAbsolutePath()));
            parentPathDoesNotExist = !outputFile.getParentFile().mkdirs();
        }

        if (parentPathDoesNotExist) {
            throw new IllegalArgumentException("Unable to create path: " + outputFile.getParentFile().getAbsolutePath());
        }
    }

    private void copyAssetToFile(String assetName, File outputFile) {
        InputStream inputStream = null;
        OutputStream myOutput = null;
        int length;
        try {
            inputStream = assetManager.open(assetName);
            myOutput = new FileOutputStream(outputFile, true);
            while ((length = inputStream.read(BUFFER)) > 0) {
                myOutput.write(BUFFER, 0, length);
            }
            Log.d(getClass().getSimpleName(), "Copied asset: " + assetName);
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Failed to copy asset: " + assetName, e);
        } finally {
            try {
                if (myOutput != null) {
                    myOutput.close();
                    myOutput.flush();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), "Failed to close streams.", e);
            }
        }
    }
}

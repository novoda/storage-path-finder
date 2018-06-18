package com.novoda.storagepathfinder.demo;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.novoda.storagepathfinder.AndroidApplicationDirectories;
import com.novoda.storagepathfinder.AndroidDeviceFeatures;
import com.novoda.storagepathfinder.AndroidDeviceStorageInspector;
import com.novoda.storagepathfinder.AndroidFileSystem;
import com.novoda.storagepathfinder.AndroidSystem;
import com.novoda.storagepathfinder.CommonDirectories;
import com.novoda.storagepathfinder.DeviceFeatures;
import com.novoda.storagepathfinder.DeviceStorageRoot;
import com.novoda.storagepathfinder.FileSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class LandingActivity extends AppCompatActivity {

    private static final String ASSET_NAME = "10MB.zip";
    private static final int BUFFER_SIZE = 1024;
    private static final byte[] BUFFER = new byte[BUFFER_SIZE];

    private AssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        assetManager = getApplicationContext().getAssets();

        CommonDirectories commonDirectories = new AndroidApplicationDirectories(getApplicationContext());
        FileSystem fileSystem = new AndroidFileSystem(commonDirectories);
        DeviceFeatures deviceFeatures = new AndroidDeviceFeatures();
        AndroidSystem androidSystem = new AndroidSystem();
        AndroidDeviceStorageInspector storageInspector = new AndroidDeviceStorageInspector(
                getApplicationContext(),
                fileSystem,
                deviceFeatures,
                androidSystem
        );
        List<DeviceStorageRoot> deviceStorageRoots = storageInspector.getDeviceStorageRoots();
        Log.e(getClass().getSimpleName(), "storageRoots: " + deviceStorageRoots);

        Log.e(getClass().getSimpleName(), "internal file persistence base: " + getApplicationContext().getFilesDir().getAbsolutePath());

        RecyclerView recyclerView = findViewById(R.id.device_storage_roots);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DeviceStorageRootsAdapter deviceStorageRootsAdapter = new DeviceStorageRootsAdapter(
                getLayoutInflater(),
                deviceStorageRoots,
                onAddFileClicked
        );
        recyclerView.setAdapter(deviceStorageRootsAdapter);
    }

    private final DeviceStorageRootViewHolder.Listener onAddFileClicked = deviceStorageRoot -> {
        File outputFile = new File(deviceStorageRoot.getAbsolutePath() + "/" + ASSET_NAME);
        createFileIfDoesNotExist(outputFile);
        copyAssetToFile(ASSET_NAME, outputFile);
    };

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

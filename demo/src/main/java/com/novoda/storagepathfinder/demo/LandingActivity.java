package com.novoda.storagepathfinder.demo;

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

import java.util.List;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

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
        // Add the file to that location.
    };
}

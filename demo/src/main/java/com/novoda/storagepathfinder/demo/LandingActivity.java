package com.novoda.storagepathfinder.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.novoda.storagepathfinder.DeviceStorageInspector;
import com.novoda.storagepathfinder.StoragePath;

import java.util.ArrayList;
import java.util.List;

public class LandingActivity extends AppCompatActivity {

    private static final String ASSET_NAME = "10MB.zip";

    private AssetCloner assetCloner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        assetCloner = DemoDependenciesFactory.createAssetCloner(getApplicationContext());

        DeviceStorageInspector storageInspector = DeviceStorageInspector.builder(this).build();

        List<StoragePath> deviceStoragePaths = new ArrayList<>();

        deviceStoragePaths.addAll(storageInspector.getPrimaryStorageBasePaths());
        deviceStoragePaths.addAll(storageInspector.getPrimaryStorageApplicationPaths());
        deviceStoragePaths.addAll(storageInspector.getSecondaryStorageBasePaths());
        deviceStoragePaths.addAll(storageInspector.getSecondaryStorageApplicationPaths());

        RecyclerView recyclerView = findViewById(R.id.device_storage_roots);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        StoragePathsAdapter storagePathsAdapter = new StoragePathsAdapter(
                getLayoutInflater(),
                deviceStoragePaths,
                onAddFileClicked
        );
        recyclerView.setAdapter(storagePathsAdapter);
    }

    private final StoragePathViewHolder.Listener onAddFileClicked = deviceStoragePath -> {
        String pathToCloneTo = deviceStoragePath.getPathAsString() + "/" + ASSET_NAME;
        assetCloner.cloneAsset(ASSET_NAME, pathToCloneTo);
    };

}
